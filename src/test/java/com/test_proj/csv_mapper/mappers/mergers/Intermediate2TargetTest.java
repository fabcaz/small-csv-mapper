package com.test_proj.csv_mapper.mappers.mergers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;

public class Intermediate2TargetTest {

  @Test
  @DisplayName("Verify accepts valid pair of IntermediateEntry")
  void acceptsPairOfIntermediateEntrys(){

    Intermediate2Target i2t = new Intermediate2Target();

    List<IntermediateEntry> validList = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1));

    assertTrue(i2t.accepts(validList));
  }

  @Test
  @DisplayName("Verify rejects invalid pair of IntermediateEntry")
  void rejectsPairOfIntermediateEntrys(){

    Intermediate2Target i2t = new Intermediate2Target();

    List<IntermediateEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1));

    assertFalse(i2t.accepts(invalidList1));

    List<IntermediateEntry> invalidList2 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_2));

    assertFalse(i2t.accepts(invalidList2));
  }

  @Test
  @DisplayName("Verify rejects pair with a malformed IntermediateEntry")
  void rejectsPairWithOneMalformedIntermediateEntrys(){

    Intermediate2Target i2t = new Intermediate2Target();

    List<IntermediateEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_INVALID));

    assertFalse(i2t.accepts(invalidList1));

    List<IntermediateEntry> invalidList2 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_2));

    assertFalse(i2t.accepts(invalidList2));
  }

  @Test
  @DisplayName("Verify rejects invalid pair of non-IntermediateEntry CsvEntry")
  void rejectsPairOfCsvEntrys(){

    Intermediate2Target i2t = new Intermediate2Target();

    List<? extends CsvEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2));

    assertFalse(i2t.accepts(invalidList1));

    List<SourceFormatEntry> invalidList2 = List.of(
    TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2),
    TestingUtils.srcFmtEntFromCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2));

    assertFalse(i2t.accepts(invalidList2));
  }

  @Test
  @DisplayName("Verify rejects incorrectly sized Lists of IntermediateEntry")
  void rejectsincorrectlySizedListOfIntermediateEntrys(){

    Intermediate2Target i2t = new Intermediate2Target();

    List<IntermediateEntry> invalidList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1));

    assertFalse(i2t.accepts(invalidList1));

    List<IntermediateEntry> invalidList2 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1));

    assertFalse(i2t.accepts(invalidList2));
  }


  @Test
  @DisplayName("Verify merges valid pairs")
  void successfullyMergesValidPairs(){

    TargetFormatEntry expectedOutput1 = TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_1);
    Intermediate2Target i2t = new Intermediate2Target();

    List<IntermediateEntry> validList1 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_1),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_1));
    assertNotNull(validList1.get(0).getTs());
    assertNotNull(validList1.get(1).getTs());

    TargetFormatEntry mergeOutput1 = i2t.merge(validList1);
    assertNotNull(mergeOutput1);
    assertNotNull(expectedOutput1);
    assertNotNull(mergeOutput1.getTs());
    assertNotNull(mergeOutput1.getField2());
    assertNotNull(mergeOutput1.getField1());
    assertNotNull(mergeOutput1.getField4());
    assertNotNull(mergeOutput1.getField3());
    assertNotNull(mergeOutput1.getField9());

    assertTrue(expectedOutput1.getTs().equals(mergeOutput1.getTs()));
    assertTrue(expectedOutput1.getField2().equals(mergeOutput1.getField2()));
    assertTrue(expectedOutput1.getField1().equals(mergeOutput1.getField1()));
    assertTrue(expectedOutput1.getField4().equals(mergeOutput1.getField4()));
    assertTrue(expectedOutput1.getField3().equals(mergeOutput1.getField3()));
    assertTrue(expectedOutput1.getField9().equals(mergeOutput1.getField9()));
    
    TargetFormatEntry expectedOutput2 = TestingUtils.targFmtEntFromCsvLine(TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_2);
    List<IntermediateEntry> validList2 = List.of(
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_WEST_2),
    TestingUtils.intermFmtEntFromString(TestingUtils.SAMPLE_INTERM_EAST_2));

    TargetFormatEntry mergeOutput2 = i2t.merge(validList2);
    assertNotNull(mergeOutput2);
    assertNotNull(expectedOutput2);
    assertNotNull(mergeOutput2.getTs());
    assertNotNull(mergeOutput2.getField2());
    assertNotNull(mergeOutput2.getField1());
    assertNotNull(mergeOutput2.getField4());
    assertNotNull(mergeOutput2.getField3());
    assertNotNull(mergeOutput2.getField9());

    assertTrue(expectedOutput2.getTs().toString().equals(mergeOutput2.getTs().toString()), String.format("Timestamp: expected [%s] but got [%s]", expectedOutput2.getTs().toString(), mergeOutput2.getTs().toString()));
    assertTrue(expectedOutput2.getField2().equals(mergeOutput2.getField2()), "Field2: expected [%s] but got [%s]");
    assertTrue(expectedOutput2.getField1().equals(mergeOutput2.getField1()), "Field1: expected [%s] but got [%s]");
    assertTrue(expectedOutput2.getField4().equals(mergeOutput2.getField4()), "Field4: expected [%s] but got [%s]");
    assertTrue(expectedOutput2.getField3().equals(mergeOutput2.getField3()), "Field3: expected [%s] but got [%s]");
    assertTrue(expectedOutput2.getField9().equals(mergeOutput2.getField9()), "Field9: expected [%s] but got [%s]");
  }
}
