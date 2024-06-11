package com.test_proj.csv_mapper.mappers.converters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;

public class Source2TargetTest {

  private static Source2Target s2t;


  @BeforeAll
  static void setup(){
    s2t = new Source2Target();
  }

  @Test
  @DisplayName("Verify rejects SourceFormatEntry and Labels.GREEN")
  void acceptsGreenLabels(){
    assertTrue(s2t.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY)));
  }

  @Test
  @DisplayName("Verify accepts SourceFormatEntry and Labels.BLUE")
  void rejectsGreenLabels(){
    assertFalse(s2t.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY)));
  }

  @Test
  @DisplayName("Verify rejects malformed SourceFormatEntry")
  void rejectsMalformedEntry(){
    assertFalse(s2t.accepts(TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_FIELD5_INVALID)));
  }

  @Test
  @DisplayName("Verify rejects non-SourceFormatEntry and Labels.BLUE")
  void rejectsNonSourceFormatEntries(){
    TargetFormatEntry targFmtEnt = TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_K_GREEN);
    assertFalse(s2t.accepts(targFmtEnt));
  }

  @Test
  @DisplayName("Verify successfully convert valid SourceFormatEntry")
  void successfullyConvertValidEntry(){
    SourceFormatEntry srcFmtEntGreenEntry = TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY);

    TargetFormatEntry expectedTargetFmtEntGreenConversion = TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_SOURCE_GREEN_CONVERTED2TARGET);


    TargetFormatEntry actualTargetFmtEntGreenConversion = s2t.convert(srcFmtEntGreenEntry);

    // checking InternalId against source entry since TargetFormatEntry csv string does not have an id
    assertTrue(srcFmtEntGreenEntry.getInternalId().equals(actualTargetFmtEntGreenConversion.getInternalId()), String.format("InternalId: expected [%s] actual [%s]", srcFmtEntGreenEntry.getInternalId(), actualTargetFmtEntGreenConversion.getInternalId()));
    assertTrue(expectedTargetFmtEntGreenConversion.getTs().equals(actualTargetFmtEntGreenConversion.getTs()), String.format("Ts: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getTs(), actualTargetFmtEntGreenConversion.getTs()));

    assertTrue(expectedTargetFmtEntGreenConversion.getField2().equals(actualTargetFmtEntGreenConversion.getField2()), String.format("Field2: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getField2(), actualTargetFmtEntGreenConversion.getField2()));
    assertTrue(expectedTargetFmtEntGreenConversion.getField1().equals(actualTargetFmtEntGreenConversion.getField1()), String.format("Field1: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getField1(), actualTargetFmtEntGreenConversion.getField1()));
    assertTrue(expectedTargetFmtEntGreenConversion.getField4().equals(actualTargetFmtEntGreenConversion.getField4()), String.format("Field4: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getField4(), actualTargetFmtEntGreenConversion.getField4()));
    assertTrue(expectedTargetFmtEntGreenConversion.getField3().equals(actualTargetFmtEntGreenConversion.getField3()), String.format("Field3: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getField3(), actualTargetFmtEntGreenConversion.getField3()));
    
    assertTrue(expectedTargetFmtEntGreenConversion.getField9().equals(actualTargetFmtEntGreenConversion.getField9()), String.format("Field9: expected [%s] actual [%s]", expectedTargetFmtEntGreenConversion.getField9(), actualTargetFmtEntGreenConversion.getField9()));
    
    
  }


}
