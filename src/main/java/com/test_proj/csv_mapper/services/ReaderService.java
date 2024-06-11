package com.test_proj.csv_mapper.services;

import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.CsvEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.test_proj.csv_mapper.entities.CsvEntry;

public interface ReaderService<T extends CsvEntry> {
  //could make this a class and have a Class<? extends CsvEntry> but then will
  //likely need to cast CsvEntry instances to map them to a diff type

  /**Reads the given file with implementation defaults.
   * @param f 
   * @return List of CsvEntry implementations
   */
  public List<T> readFileIntoEntryList(FileMetadata fileMetadata) throws FileNotFoundException;

}
