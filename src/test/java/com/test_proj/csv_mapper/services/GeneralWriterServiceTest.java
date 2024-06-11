package com.test_proj.csv_mapper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.lenient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.utils.FileIoUtils;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeneralWriterServiceTest {

  @Mock
  File mockFile;

  @Mock
  FileWriter mockFileWriter;

  @Mock
  FileIoUtils mockFileIUtils;

  private static final String FIELD_SEP = ",";

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  @DisplayName("Verify accepts if List contains only given Class<T>")
  void verifyAcceptsListContainingOnlyGivenClass(){
    List<IntermediateEntry> validList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_2),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_2));

    List<TargetFormatEntry> validList2 = List.of(
    TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_2),
    TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_K_GREEN ));

    //file does not need to exist for this test
    FileMetadata fm1 = new FileMetadata("fileName", FIELD_SEP, IntermediateEntry.class);
    FileMetadata fm2 = new FileMetadata("fileName", FIELD_SEP, TargetFormatEntry.class);

    GeneralWriterService gns = new GeneralWriterService();


    boolean validateList1 = gns.areEntriesValid(fm1.getCsvFormat(), validList1);
    boolean validateList2 = gns.areEntriesValid(fm2.getCsvFormat(), validList2);

    assertTrue(validateList1, "validList1 should be valid");
    assertTrue(validateList2, "validList2 should be valid");
  }

  @Test
  @DisplayName("Verify rejects if List contains an instance of Class<?> other than given Class<T>")
  void verifyRejectsListContainingWrongClassMixed(){
    List<CsvEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_2),
    TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_K_GREEN ));


    //file does not need to exist for this test
    FileMetadata fm = new FileMetadata("fileName", FIELD_SEP, TargetFormatEntry.class);
    GeneralWriterService gns = new GeneralWriterService();


    boolean invalidateList1 = gns.areEntriesValid(fm.getCsvFormat(), invalidList1);

    assertFalse(invalidateList1, "validList1 should NOT be valid");
  }

  @Test
  @DisplayName("Verify rejects if List<E> given Class<T>")
  void verifyRejectsListofWrongClass(){
    List<IntermediateEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_2),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_2));


    //file does not need to exist for this test
    FileMetadata fm = new FileMetadata("fileName", FIELD_SEP, TargetFormatEntry.class);
    GeneralWriterService gns = new GeneralWriterService();


    boolean invalidateList1 = gns.areEntriesValid(fm.getCsvFormat(), invalidList1);

    assertFalse(invalidateList1, "validList1 should NOT be valid");
  }


  @Test
  @DisplayName("Verify writes to file")
  void verifyWritesToFile(){

    FileMetadata fm = new FileMetadata(mockFile, FIELD_SEP, TargetFormatEntry.class);
    String lineSeparator = fm.getLineSeparator();

    List<TargetFormatEntry> validList = List.of(
        TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_2),
        TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_K_GREEN));

    List<String> expectedCsvStrings = List.of(
        TargetFormatEntry.HEADERS + lineSeparator,
        TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_2 + lineSeparator,
        TestingUtils.SAMPLE_K_GREEN + lineSeparator);

    ArgumentCaptor<String> strCaptor = ArgumentCaptor.forClass(String.class);

    lenient().when(mockFile.canWrite()).thenReturn(true);
    when(mockFile.exists()).thenReturn(true);
    when(mockFile.isFile()).thenReturn(true);
    try {
      lenient().when(mockFile.createNewFile()).thenReturn(true);
    } catch (IOException e) {
      throw new RuntimeException("mockFile.createNewFile() stubbing");
    }

    try(MockedStatic<FileIoUtils> mockFileIoUtils = mockStatic(FileIoUtils.class)) {
     
      mockFileIoUtils.when(() -> FileIoUtils.createFileWriterFrom(any()))
        .thenReturn(mockFileWriter);
      
      GeneralWriterService gws = new GeneralWriterService();

      gws.writeLines(fm, validList);

      verify(mockFileWriter, times(3)).write(strCaptor.capture());

    } catch (IOException e) {
      throw new RuntimeException("something went wrong while verify()ing the args to mockFileWriter.write(String)");
    }
    List<String> capturedCsvstrings = strCaptor.getAllValues();

    assertEquals(validList.size() + 1, capturedCsvstrings.size(), String.format("Expected %s strings to be written but got %s", validList.size(), capturedCsvstrings.size()));
    assertEquals(expectedCsvStrings.size(), capturedCsvstrings.size(), String.format("Expected %s strings to be written but got %s", validList.size(), capturedCsvstrings.size()));

    assertEquals(expectedCsvStrings, capturedCsvstrings);
    
    assertTrue(expectedCsvStrings.get(0).equals(capturedCsvstrings.get(0)), String.format("Expected [%s]  got [%s]", expectedCsvStrings .get(0), capturedCsvstrings.get(0)));
    assertTrue(expectedCsvStrings.get(1).equals(capturedCsvstrings.get(1)), String.format("Expected [%s]  got [%s]", expectedCsvStrings .get(1), capturedCsvstrings.get(1)));
  }
}
