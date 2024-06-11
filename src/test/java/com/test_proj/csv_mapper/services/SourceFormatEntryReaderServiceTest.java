package com.test_proj.csv_mapper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.utils.FileIoUtils;

public class SourceFormatEntryReaderServiceTest {
  

  @Mock
  File mockFile;

  @Mock
  BufferedReader mockBufferedReader;

  private static final String FIELD_SEP = ",";

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  @DisplayName("Verify converts all valid lines")
  void convertsAllValidLines(){

    List<SourceFormatEntry> expected = List.of(
      TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY),
      TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY),
      TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2),
      TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2)
        );
    String fileContent = String.join(System.lineSeparator(),
      TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY,
      TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY,
      TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2,
      TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2
        );
      
    FileMetadata fm = new FileMetadata(mockFile, FIELD_SEP, SourceFormatEntry.class);
    SourceFormatEntryReaderService clers = new SourceFormatEntryReaderService();

    when(mockFile.canRead()).thenReturn(true);

    try(MockedStatic<FileIoUtils> mockFileIoUtils = mockStatic(FileIoUtils.class)) {
     
      mockFileIoUtils.when(() -> FileIoUtils.createBufferedFileReaderFrom(any()))
        .thenReturn(new BufferedReader(new StringReader(fileContent)));
      
     List<SourceFormatEntry> result =  clers.readFileIntoEntryList(fm);

     assertEquals(expected, result);

    } catch (IOException e) {
      throw new RuntimeException("something went wrong while mocking FileIoUtils for SourceFormatEntryReaderServiceTest");
    }

  }


  @Test
  @DisplayName("Verify filters out invalid lines")
  void shouldFilterOutInvalidCsvLine(){

    List<SourceFormatEntry> expected = new ArrayList<>(4);
      expected.add(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY));
      expected.add(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY));
      expected.add(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2));
      expected.add(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2));

    String fileContent = String.join(System.lineSeparator(),
      TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY,
      TestingUtils.SAMPLE_SOURCE_FIELD5_INVALID, // should get filtered out
      TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY,
      TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2,
      TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2
        );

    FileMetadata fm = new FileMetadata(mockFile, FIELD_SEP, SourceFormatEntry.class);
    SourceFormatEntryReaderService clers = new SourceFormatEntryReaderService();

    when(mockFile.canRead()).thenReturn(true);

    try(MockedStatic<FileIoUtils> mockFileIoUtils = mockStatic(FileIoUtils.class)) {
     
      mockFileIoUtils.when(() -> FileIoUtils.createBufferedFileReaderFrom(any()))
        .thenReturn(new BufferedReader(new StringReader(fileContent)));
      
     List<SourceFormatEntry> result =  clers.readFileIntoEntryList(fm);

     expected.sort((a,b) -> a.getInternalId().compareTo(b.getInternalId()));
     result.sort((a,b) -> a.getInternalId().compareTo(b.getInternalId()));
     assertEquals(expected.size(), result.size());
     assertEquals(expected, result, String.format("\n\nExpect = [[%s]]\n\nGot    = [[%s]]\n\n", expected.toString(), result.toString()));

    } catch (IOException e) {
      throw new RuntimeException("something went wrong while mocking FileIoUtils for SourceFormatEntryReaderServiceTest");
    }
  }

}
