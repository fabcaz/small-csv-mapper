package com.test_proj.csv_mapper.services;

import java.util.List;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface MappingService<S extends CsvEntry, T extends CsvEntry> {
  
  List<T> transformEntries(List<S> srcEntries);
}
