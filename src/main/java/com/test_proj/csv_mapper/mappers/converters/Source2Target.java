package com.test_proj.csv_mapper.mappers.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.mappers.OneToOneEntryMapper;
import com.test_proj.csv_mapper.values.Labels;

public class Source2Target implements OneToOneEntryMapper<SourceFormatEntry, TargetFormatEntry>{

  private static final Logger log = LoggerFactory.getLogger(Source2Target.class);
	@Override
	public TargetFormatEntry convert(SourceFormatEntry src) {

    TargetFormatEntry res =  new TargetFormatEntry(src.getInternalId(),
        src.getTs(),
        "",
        src.getField5(),
        Double.valueOf(0),
        src.getField6(),
        src.getField2());

    log.debug("Converted [{}] into [{}]", src, res);
    return res;
	}

	@Override
	public <S extends CsvEntry> boolean accepts(S srcClass) {
		return srcClass instanceof SourceFormatEntry 
      && srcClass.isValid()
      && ((SourceFormatEntry)srcClass).getField2().equals(Labels.GREEN);
	}

	@Override
	public Class<SourceFormatEntry> getSourceType() {
		return SourceFormatEntry.class;
	}

	@Override
	public Class<TargetFormatEntry> getTargetType() {
		return TargetFormatEntry.class;
	}
  
}
