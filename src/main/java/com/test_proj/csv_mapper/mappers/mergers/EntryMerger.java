package com.test_proj.csv_mapper.mappers.mergers;

import java.util.List;

import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.mappers.EntryMapper;

public interface EntryMerger<S extends CsvEntry,D extends CsvEntry> extends EntryMapper<S,D>{
  List<D> merge(List<S> src);

}
