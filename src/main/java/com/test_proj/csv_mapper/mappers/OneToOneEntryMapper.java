package com.test_proj.csv_mapper.mappers;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface OneToOneEntryMapper<S extends CsvEntry,D extends CsvEntry> {
  
	 D convert(S src);
  <S extends CsvEntry> boolean accepts(S srcClass);

  Class<S> getSourceType();
  Class<D> getTargetType();
}
