package com.test_proj.csv_mapper.services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.utils.FileIoUtils;


/**
 * {@link WriterService} implementation that gets the {@link CsvEntry} type to
 * write to file from {@link FileMetadata} field
 */
public class GeneralWriterService implements WriterService{

  private static final Logger log = LoggerFactory.getLogger(GeneralWriterService.class);

  public void writeLines(FileMetadata destFileMetaData, List<? extends CsvEntry> entries){
    String lineSep = destFileMetaData.getLineSeparator();

    if(!areEntriesValid(destFileMetaData.getCsvFormat(), entries)){
      throw new RuntimeException("Cannot write to dest file: invalid entry type");
    }
    boolean fileIsWritable = destFileMetaData.tryToCreateWritableFile();
    if (!fileIsWritable) {
      throw new RuntimeException("Cannot write to dest file: file not writable");
    }

    try (FileWriter fw = getFileWriter(destFileMetaData)) {

      try {
        fw.write(TargetFormatEntry.HEADERS + lineSep);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      entries.stream()
        .peek(ent -> log.debug("About to attempt writing entry: [{}]", ent))
        .forEach(ent -> {
          try {
            fw.write(ent.toCsvString() + lineSep);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong while writing entries to dest file");
    }
  }

  public boolean areEntriesValid(Class<? extends CsvEntry> format, List<? extends CsvEntry> entries){
    return entries.stream()
      .allMatch(ent -> ent.getClass().isAssignableFrom(format));
  }

  /**
   * returns a FileWriter; only exists for mocking in tests.
   * @param fm
   * @return
   * @throws IOException
   */
  private FileWriter getFileWriter(FileMetadata fm) throws IOException{
    // Since the configurations are stored in the FileMetadata obj
    // and I don't expect to write to anything other than a File,
    // let's just extract constructing a FileWriter to a static method in a new
    // object and call it here instead of injecting an object implementing
    // some kind of WriterProvider interface in the service
    return FileIoUtils.createFileWriterFrom(fm);
  }

}
