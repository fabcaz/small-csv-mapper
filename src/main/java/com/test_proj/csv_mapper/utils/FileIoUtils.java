package com.test_proj.csv_mapper.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.test_proj.csv_mapper.configurators.FileMetadata;

/** This class is used to mock FileWriter/Reader according to the advice given 
 * in Mockito docs section [16. Real partial mocks](https://javadoc.io/static/org.mockito/mockito-core/5.1.1/org/mockito/Mockito.html#partial_mocks)
 * 
 */
public class FileIoUtils {
  
  public static FileWriter createFileWriterFrom(FileMetadata fm) throws IOException{
    return new FileWriter(fm.getFile());
  }

  public static BufferedReader createBufferedFileReaderFrom(FileMetadata fm) throws FileNotFoundException{
    return new BufferedReader(new FileReader(fm.getFile()));
  }
}
