package com.test_proj.csv_mapper.mappers.splitters;

import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.mappers.EntryMapper;

public interface EntrySplitter<S extends CsvEntry,D extends CsvEntry> extends EntryMapper<S,D>{
  D split(S src);

}
