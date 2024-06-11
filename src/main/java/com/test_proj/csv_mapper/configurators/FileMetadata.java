package com.test_proj.csv_mapper.configurators;

import java.io.File;
import java.io.IOException;

import com.test_proj.csv_mapper.entities.CsvEntry;

public class FileMetadata {

  private final File file;
  private final String fieldSeparator;
  private final String lineSeparator = System.lineSeparator();
  private Class<? extends CsvEntry> csvFormat;

  //private HashMap<String, CsvEntry> srcEntries = new HashMap<>();
  //private HashMap<String, CsvEntry> intermediaryEntries = new HashMap<>();
  //private HashMap<String, CsvEntry> targetEntries = new HashMap<>();

  public FileMetadata(String fileName,
      String fieldSeparator,
      Class<? extends CsvEntry> csvFormat){

    this(
        new File(fileName),
        fieldSeparator,
        csvFormat
        );
  }

  public FileMetadata(File file,
      String fieldSeparator,
      Class<? extends CsvEntry> csvFormat){

    this.file = file;
    this.fieldSeparator = fieldSeparator;
    this.csvFormat = csvFormat;
  }

  public File getFile() {
    return file;
  }

  public String getFieldSeparator() {
    return fieldSeparator;
  }

  public String getLineSeparator() {
    return lineSeparator;
  }

  public Class<? extends CsvEntry> getCsvFormat() {
    return csvFormat;
  }

  public boolean fileExists(){
    return file.isFile();
  }

  public boolean isReadable(){
    return file.canRead();
  }

  public boolean isWritable(){
    return file.canWrite();
  }

  /**
   * Throws if the location exist but is not a file. Tries to create a file if
   * none exist.
   * 
   * @return true if the file exists or has been created, and is writable
   */
  public boolean tryToCreateWritableFile(){
    if (file.exists() && !file.isFile()) {
      throw new RuntimeException("exits but is not file");
    }else if (!file.exists()) {
      try {
        return file.createNewFile() && file.canWrite();
      } catch (IOException e) {
        throw new RuntimeException("could not create new file");
      }      
    }
    return file.canWrite();
  }

}
