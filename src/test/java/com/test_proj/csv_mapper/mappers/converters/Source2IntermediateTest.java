package com.test_proj.csv_mapper.mappers.converters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;

public class Source2IntermediateTest {

  private static Source2Intermediate s2i;

  @BeforeAll
  static void setup(){
    s2i = new Source2Intermediate();
  }

  @Test
  @DisplayName("Verify accepts SourceFormatEntry and Labels.BLUE")
  void acceptsBlueLabels(){
    assertTrue(s2i.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY)));
  }

  @Test
  @DisplayName("Verify rejects SourceFormatEntry and Labels.GREEN")
  void rejectsGreenLabels(){
    assertFalse(s2i.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY)));
  }

  @Test
  @DisplayName("Verify rejects malformed SourceFormatEntry")
  void rejectsMalformedEntry(){
    assertFalse(s2i.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_FIELD5_INVALID)));
  }

  @Test
  @DisplayName("Verify rejects non-SourceFormatEntry and Labels.BLUE")
  void rejectsNonSourceFormatEntrys(){
    TargetFormatEntry targFmtEnt = TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_1);
    assertFalse(s2i.accepts(targFmtEnt));
  }

  @Test
  @DisplayName("Verify successfully convert valid SourceFormatEntry")
  void successfullyConvertValidEntry(){
    SourceFormatEntry srcEastEntry = TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2);
    SourceFormatEntry srcWestEntry = TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2);

    IntermediateEntry expectedEastConversion = TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2_CONVERTED2INTERM);
    IntermediateEntry expectedWestConversion = TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2_CONVERTED2INTERM);

    IntermediateEntry actualEastConversion = s2i.convert(srcEastEntry);
    IntermediateEntry actualWestConversion = s2i.convert(srcWestEntry);

    assertTrue(expectedEastConversion.getInternalId().equals(actualEastConversion.getInternalId()), String.format("InternalId: expected [%s] actual [%s]", expectedEastConversion.getInternalId(), actualEastConversion.getInternalId()));
    assertTrue(expectedEastConversion.getTs().equals(actualEastConversion.getTs()), String.format("Ts: expected [%s] actual [%s]", expectedEastConversion.getTs(), actualEastConversion.getTs()));
    assertTrue(expectedEastConversion.getField2().equals(actualEastConversion.getField2()), String.format("Field2: expected [%s] actual [%s]", expectedEastConversion.getField2(), actualEastConversion.getField2()));
    assertTrue(expectedEastConversion.getField3().equals(actualEastConversion.getField3()), String.format("Field3: expected [%s] actual [%s]", expectedEastConversion.getField3(), actualEastConversion.getField3()));
    assertTrue(expectedEastConversion.getField4().equals(actualEastConversion.getField4()), String.format("Field4: expected [%s] actual [%s]", expectedEastConversion.getField4(), actualEastConversion.getField4()));
    assertTrue(expectedEastConversion.getField5().equals(actualEastConversion.getField5()), String.format("Field5: expected [%s] actual [%s]", expectedEastConversion.getField5(), actualEastConversion.getField5()));
    
    assertTrue(expectedWestConversion.getInternalId().equals(actualWestConversion.getInternalId()), String.format("InternalId: expected [%s] actual [%s]", expectedWestConversion.getInternalId(), actualWestConversion.getInternalId()));
    assertTrue(expectedWestConversion.getTs().equals(actualWestConversion.getTs()), String.format("Ts: expected [%s] actual [%s]", expectedWestConversion.getTs(), actualWestConversion.getTs()));
    assertTrue(expectedWestConversion.getField2().equals(actualWestConversion.getField2()), String.format("Field2: expected [%s] actual [%s]", expectedWestConversion.getField2(), actualWestConversion.getField2()));
    assertTrue(expectedWestConversion.getField3().equals(actualWestConversion.getField3()), String.format("Field3: expected [%s] actual [%s]", expectedWestConversion.getField3(), actualWestConversion.getField3()));
    assertTrue(expectedWestConversion.getField4().equals(actualWestConversion.getField4()), String.format("Field4: expected [%s] actual [%s]", expectedWestConversion.getField4(), actualWestConversion.getField4()));
    assertTrue(expectedWestConversion.getField5().equals(actualWestConversion.getField5()), String.format("Field5: expected [%s] actual [%s]", expectedWestConversion.getField5(), actualWestConversion.getField5()));
  }

}
