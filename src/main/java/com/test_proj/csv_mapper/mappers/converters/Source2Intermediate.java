package com.test_proj.csv_mapper.mappers.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.mappers.OneToOneEntryMapper;
import com.test_proj.csv_mapper.values.Labels;

public class Source2Intermediate implements OneToOneEntryMapper<SourceFormatEntry, IntermediateEntry>{

  private static final Logger log = LoggerFactory.getLogger(Source2Intermediate.class);

	@Override
	public IntermediateEntry convert(SourceFormatEntry src) {
      // should only be used on entries with Labels.BLUE because it is the use case
      // but will accept any SourceFormatEntry
    IntermediateEntry res = new IntermediateEntry(src.getTs(),
        src.getInternalId(),
        src.getField4(),
        src.getField5(),
        Math.abs(src.getField6())
        );

    log.debug("Converted [{}] to [{}]", src, res);
    return res;
	}

	@Override
	public <S extends CsvEntry> boolean accepts(S srcClass) {
		return srcClass instanceof SourceFormatEntry 
      && srcClass.isValid()
      && ((SourceFormatEntry)srcClass).getField2().equals(Labels.BLUE);
	}

	@Override
	public Class<SourceFormatEntry> getSourceType() {
		return SourceFormatEntry.class;
	}

	@Override
	public Class<IntermediateEntry> getTargetType() {
		return IntermediateEntry.class;
	}

  
}
