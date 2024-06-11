package com.test_proj.csv_mapper.mappers;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface EntryMapper<S extends CsvEntry,D extends CsvEntry> {
  
  <S extends CsvEntry> boolean acceptsPair(S srcClass);

  Class<S> getSourceType();
  Class<D> getTargetType();
}
