package com.test_proj.csv_mapper.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.utils.FileIoUtils;

public class SourceFormatEntryReaderService implements ReaderService<SourceFormatEntry> {

  private static final Logger log = LoggerFactory.getLogger(SourceFormatEntryReaderService.class);

  @Override
  public List<SourceFormatEntry> readFileIntoEntryList(FileMetadata fileMetadata)  throws FileNotFoundException{
    File f = fileMetadata.getFile();
    if (!f.canRead()) {
      throw new IllegalArgumentException(
          String.format("Unreadable File: %s", f.getAbsolutePath()));
    }
    // validating a csv string requires splitting so will use the array from that
    // step to construct instead of splitting again in a fromCsvFactorymethod
    try ( BufferedReader br = getFileReader(fileMetadata) ) {
      return br.lines()
        .peek(line -> log.debug("Read [{}]", line))
        .map(l -> SourceFormatEntry.validateCsvString(l))
        .filter(opt -> opt.isPresent())
        .map(arrOptional -> {
          String[] arr = arrOptional.get();
          return new SourceFormatEntry(
              arr[0],
              arr[1],
              arr[2],
              arr[3],
              arr[4],
              arr[5],
              arr[6]);
        })
        .peek(ent -> log.debug("Line to entry: [{}]", ent))
      .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong while reading csv into SourceFormatEntry list");
    }
  }
  private BufferedReader getFileReader(FileMetadata fm) throws IOException{
    return FileIoUtils.createBufferedFileReaderFrom(fm);
  }
}
