package com.test_proj.csv_mapper.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.mappers.converters.Source2Intermediate;
import com.test_proj.csv_mapper.mappers.converters.Source2Target;
import com.test_proj.csv_mapper.mappers.mergers.Intermediate2Target;
import com.test_proj.csv_mapper.values.Labels;


public class MappingServiceImpl implements MappingService<SourceFormatEntry, TargetFormatEntry>{

  private static final Logger log = LoggerFactory.getLogger(MappingServiceImpl.class);

  //mapper impls could have chains/list of mappers
  private Source2Target s2t;
  private Source2Intermediate s2i;
  private Intermediate2Target i2t;

    public MappingServiceImpl(
      Source2Target s2t,
      Source2Intermediate s2i,
      Intermediate2Target i2t
      ) {
    this.s2t = s2t;
    this.s2i = s2i;
    this.i2t = i2t;
      }

  public List<TargetFormatEntry> transformEntries(List<SourceFormatEntry> srcEntries){
    // could be a map if there is an unknown number of intermediate sets but will
    // need to cast every element of every set before each merge until all indermediate
    // sets are empty could make the merging converter responsible for the casting
    // after filtering for the converter that accepts the given entry.
    // Map<String, List<CsvEntry>> intermediateEntryLists = new HashMap<>();
    List<IntermediateEntry> intermediateEntryList = new ArrayList<>();
    List<TargetFormatEntry> mergeResult = new ArrayList<>();
    List<TargetFormatEntry> targetEntries = new ArrayList<>();


    targetEntries.addAll(
        srcEntries.stream()
        .filter(ent -> ent.getField2().equals(Labels.GREEN))
        .map(ent -> s2t.convert(ent))
        .collect(Collectors.toList())
        );
    log.info("Converted {} sources into {} targets.",
        srcEntries.size(), targetEntries.size());
    log.debug("Sources to targets result: [{}]",  targetEntries);

    intermediateEntryList.addAll(
        srcEntries.stream()
        .filter(ent -> ent.getField2().equals(Labels.BLUE))
        .map(ent -> s2i.convert(ent))
        .collect(Collectors.toList())
        );
    log.info("Converted {} sources into {} intermediates.",
        srcEntries.size(), intermediateEntryList.size());
    log.debug("Sources to intermediates result: [{}]", intermediateEntryList);
    
    mergeResult.addAll(
        pairIntermediateEntries(intermediateEntryList)
          .values()
          .stream()
          .map(l -> i2t.merge(l))
          .collect(Collectors.toList())
        );
    log.info("Merged intermediates into {} pairs.", mergeResult.size());
    log.debug("Merged intermediates result: [{}]", mergeResult);

    targetEntries.addAll(mergeResult);
    targetEntries.sort(TargetFormatEntry.comparator);
    return targetEntries;
  }

  private Map<String, List<IntermediateEntry>> 
    pairIntermediateEntries(List<IntermediateEntry> src){

    log.info("Start pairing intermediateEntryList of size {}", src.size());
    Map<String, List<IntermediateEntry>> m = new HashMap<>();
    src.stream()
      .forEach(ent -> {
        String id = ent.getInternalId();
        if(m.containsKey(id)){
          List<IntermediateEntry> l = m.get(id);
          if(l.size() >= 2){
            throw new RuntimeException(String.format("There are too many entries with id [%s]", id));
          }
          l.add(ent);
        }else{
          List<IntermediateEntry> l = new ArrayList<>();
          l.add(ent);
          m.put(id, l);
        }
      });
    log.info("End pairing with {} pairs", m.values().size());

	  return m;
  }
}
