package com.test_proj.csv_mapper.services;

import java.util.List;

import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.CsvEntry;

// was going to parametrize the interface but there is acrually no need
public interface WriterService {

  public void writeLines(FileMetadata destFileMetadata, List<? extends CsvEntry> entries);

  /**
   * @return true if the list of entries is not empty and only contains the
   *         type(s) expected by the dest file.
   */
  public boolean areEntriesValid(Class<? extends CsvEntry> destFileMetadata, List<? extends CsvEntry> entries);
}
