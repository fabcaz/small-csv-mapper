package com.test_proj.csv_mapper.mappers;

import java.util.List;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface OneToManyEntryMapper<S extends CsvEntry> {

	List<? extends CsvEntry> merge(S src);
  boolean accepts(S src);
  
  Class<S> getSourceType();
  Class<List<? extends CsvEntry>> getTargetTypes();
}
