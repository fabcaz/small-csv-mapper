package com.test_proj.csv_mapper.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.values.Labels;

public class TargetFormatEntryTest {
  
  @Test
  @DisplayName("verify DateFormater accepts expected format")
  void readTimestampFromStringGoodFmt(){
    String ts = "2022-10-22 05:17:14 UTC";

    LocalDateTime ldt = TargetFormatEntry.readTimestampFromString(ts);

    int y   = ldt.getYear();
    Month m = ldt.getMonth();
    int d   = ldt.getDayOfMonth();
    int h   = ldt.getHour();
    int min = ldt.getMinute();
    int s   = ldt.getSecond();

    assertEquals(y, 2022, String.format("mergeOutput2 YEAR = [%s] but expected [%s] ", y, 2022 ) );
    assertEquals(m, Month.OCTOBER, String.format("mergeOutput2 Month = [%s] but expected [%s] ", m, "OCT" ));
    assertEquals(d, 22, String.format("mergeOutput2 DAY = [%s] but expected [%s] ", d, 22 ));
    assertEquals(h, 5, String.format("mergeOutput2 HOUR = [%s] but expected [%s] ", h, 5 ));
    assertEquals(min, 17, String.format("mergeOutput2 MIN = [%s] but expected [%s] ", min, 17 ));
    assertEquals(s, 14, String.format("mergeOutput2 SEC = [%s] but expected [%s] ", s, 14 ));
  }

  @Test
  @DisplayName("Verify can print date with correct format")
  void printTimestampToStringCorrectly(){

    //example date from javadoc
    LocalDateTime ts = LocalDateTime.parse("2007-12-03T10:15:30");
    String expectedOutput = "2007-12-03 10:15:30 UTC";

    
    TargetFormatEntry targ = new TargetFormatEntry("id", ts, "DEF", "ABC", Double.valueOf(20), Double.valueOf(21), Labels.BLUE);

    String actualOutput = targ.toCsvString().split(",")[0];

    assertTrue(expectedOutput.equals(actualOutput), String.format("Ts String fmt mismatch: [%s] but expected [%s] ", expectedOutput, actualOutput ));
  }

  @Test
  @DisplayName("Verify can construct from valid csv line string")
  void constructFromCsvString(){

    String csvString = TestingUtils.SAMPLE_INTERM_MERGE_OUTPUT_2;
    TargetFormatEntry targFmt = TargetFormatEntry.fromCsvString(csvString);
    String[] tokens = csvString.split(",", -1);

    String expectedInternalId = "";
    Double expectedField1 = Double.valueOf(tokens[1]);
    String expectedField2 = tokens[2];
    Double expectedField3 = Double.valueOf(tokens[3]);
    String expectedField4 = tokens[4];
    Labels expectedField9 = Labels.fromString(tokens[9]);

    LocalDateTime ldt = targFmt.getTs();
    assertEquals(ldt.getYear(), 2022, String.format("mergeOutput2 YEAR = [%s] but expected [%s] ", ldt.getYear(), 2022 ) );
    assertEquals(ldt.getMonth(), Month.OCTOBER, String.format("mergeOutput2 Month = [%s] but expected [%s] ", ldt.getMonth(), "OCT" ));
    assertEquals(ldt.getDayOfMonth(), 22, String.format("mergeOutput2 DAY = [%s] but expected [%s] ", ldt.getDayOfMonth(), 22 ));
    assertEquals(ldt.getHour(), 5, String.format("mergeOutput2 HOUR = [%s] but expected [%s] ", ldt.getHour(), 5 ));
    assertEquals(ldt.getMinute(), 17, String.format("mergeOutput2 MIN = [%s] but expected [%s] ", ldt.getMinute(), 17 ));
    assertEquals(ldt.getSecond(), 14, String.format("mergeOutput2 SEC = [%s] but expected [%s] ", ldt.getSecond(), 14 ));


    assertTrue(expectedInternalId.equals(targFmt.getInternalId()),    
         String.format("InternalId: expected [%s] actual [%s]",  
         expectedInternalId, targFmt.getInternalId()));

    assertTrue(expectedField2.equals(targFmt.getField2()),
        String.format("Field2: expected [%s] actual [%s]",
        expectedField2, targFmt.getField2()));
    
    assertTrue(expectedField4.equals(targFmt.getField4()),
        String.format("Field4: expected [%s] actual [%s]",
        expectedField4, targFmt.getField4()));
    
    assertTrue(expectedField9.equals(targFmt.getField9()),
        String.format("Field9: expected [%s] actual [%s]",
        expectedField9, targFmt.getField9()));

    assertEquals(expectedField1, targFmt.getField1(),
        String.format("Field1: expected [%s] actual [%s]",
        expectedField1, targFmt.getField1()));

    assertEquals(expectedField3, targFmt.getField3(),
        String.format("Field3: expected [%s] actual [%s]",
        expectedField3, targFmt.getField3()));
  }

  @Test
  @DisplayName("Verify can write valid csv line string")
  void canWriteCsvString(){
    
    String csvStringIn = TestingUtils.SAMPLE_SOURCE_GREEN_CONVERTED2TARGET;
    TargetFormatEntry toTargFmt = TargetFormatEntry.fromCsvString(csvStringIn);
    String  csvStringOut = toTargFmt.toCsvString();

    assertEquals(csvStringIn, csvStringOut,
        String.format("ToCsvString: \nexpected [%s] \nactual [%s]",
        csvStringIn, csvStringOut));
  }
}
