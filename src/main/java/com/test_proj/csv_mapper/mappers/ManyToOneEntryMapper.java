package com.test_proj.csv_mapper.mappers;

import java.util.List;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface ManyToOneEntryMapper<T extends CsvEntry> {
  
	T merge(List<? extends CsvEntry> src);
  boolean accepts(List<? extends CsvEntry> srcList);

  List<Class<? extends CsvEntry>> getSourceTypes();
  Class<T> getTargetType();
}
