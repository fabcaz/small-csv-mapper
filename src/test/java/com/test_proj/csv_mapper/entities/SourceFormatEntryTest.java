package com.test_proj.csv_mapper.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.test_proj.csv_mapper.values.Labels;

import com.test_proj.csv_mapper.TestingUtils;
import com.test_proj.csv_mapper.values.Directions;

public class SourceFormatEntryTest {
  
  private static final String SAMPLE_ENTRY = "2022-01-01 02:03:04,1784267816,blue,,East,DEF,-10";
  
  @Test
  @DisplayName("verify DateFormater accepts expected format")
  void readTimestampFromStringGoodFmt(){
    String ts = "2022-01-01 02:03:04";

    LocalDateTime ldt = SourceFormatEntry.readTimestampFromString(ts);

    assertEquals(ldt.getYear(), 2022, "year from timestamp does not match year from string");
    assertEquals(ldt.getMonth(), Month.JANUARY, "month from timestamp does not match month from string");
    assertEquals(ldt.getDayOfMonth(), 1, "day from timestamp does not match day from string");
    assertEquals(ldt.getHour(), 2, "hour from timestamp does not match hour from string");
    assertEquals(ldt.getMinute(), 3, "minute from timestamp does not match minute from string");
    assertEquals(ldt.getSecond(), 4, "seconds from timestamp does not match seconds from string");
  }

  @Test
  @DisplayName("Verify DateFormater rejects unexpected format")
  void readTimestampFromInvalidString(){
    String ts = "2022-01-01T02:03:04";

    Exception e = assertThrows(
        DateTimeParseException.class, 
        () -> SourceFormatEntry.readTimestampFromString(ts)
        );
  }

  @Test
  @DisplayName("Verify string args constructor builds correct object")
  void stringConstructor(){
    String[] args = SAMPLE_ENTRY.split(",", -1);
    SourceFormatEntry srcFmtEnt = new SourceFormatEntry(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);

    //not checking timestamp because it is tested in tests above
    assertEquals(srcFmtEnt.getInternalId(), args[1]);
    assertEquals(srcFmtEnt.getField2(), Labels.BLUE);
    assertEquals(srcFmtEnt.getField3(), args[3]);
    assertEquals(srcFmtEnt.getField4(), Directions.EAST);
    assertEquals(srcFmtEnt.getField5(), args[5]);
    assertEquals(srcFmtEnt.getField6(), Double.valueOf(args[6]));
  }

  @Test
  @DisplayName("verify can construct from valid csv line string")
  void canConstructFromCsvString(){
    String csvString = TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2;
    
    String[] tokens = csvString.split(",", -1);

    String expectedInternalId = tokens[1];
    Labels expectedField2     = Labels.fromString(tokens[2]);
    String expectedField3     = tokens[3];
    Directions expectedField4 = Directions.fromString(tokens[4]);
    String expectedField5     = tokens[5];
    Double expectedField6     = Double.valueOf(tokens[6]);

    SourceFormatEntry srcFmtEnt = SourceFormatEntry.fromCsvString(csvString);

    LocalDateTime ldt = srcFmtEnt.getTs();
    assertEquals(ldt.getYear(), 2022,
         String.format("Year: expected [%s] actual [%s]",
           2022, ldt.getYear()));  

    assertEquals(ldt.getMonth(), Month.JANUARY,
         String.format("Month: expected [%s] actual [%s]",
           "JANUARY", ldt.getMonth()));  
    
    assertEquals(ldt.getDayOfMonth(), 1,
         String.format("Day: expected [%s] actual [%s]",
           1, ldt.getDayOfMonth()));  
    
    assertEquals(ldt.getHour(), 2,
         String.format("Hour: expected [%s] actual [%s]",
           2, ldt.getHour()));  
    
    assertEquals(ldt.getMinute(), 3,
         String.format("Minute: expected [%s] actual [%s]",
           3, ldt.getMinute()));  
    
    assertEquals(ldt.getSecond(), 4,
         String.format("Second: expected [%s] actual [%s]",
           4, ldt.getSecond()));  

    assertTrue(expectedInternalId.equals(srcFmtEnt.getInternalId()),    
         String.format("InternalId: expected [%s] actual [%s]",  
         expectedInternalId, srcFmtEnt.getInternalId()));

    assertTrue(expectedField2.equals(srcFmtEnt.getField2()),
        String.format("Field2: expected [%s] actual [%s]",
        expectedField2, srcFmtEnt.getField2()));
    
    assertTrue(expectedField3.equals(srcFmtEnt.getField3()),
        String.format("Field3: expected [%s] actual [%s]",
        expectedField3, srcFmtEnt.getField3()));
    
    assertTrue(expectedField4.equals(srcFmtEnt.getField4()),
        String.format("Field4 expected [%s] actual [%s]",
        expectedField4, srcFmtEnt.getField4()));
    
    assertTrue(expectedField5.equals(srcFmtEnt.getField5()),
        String.format("Field5: expected [%s] actual [%s]",
        expectedField5, srcFmtEnt.getField5()));
    
    assertEquals(expectedField6, srcFmtEnt.getField6(),
        String.format("Field6: expected [%s] actual [%s]",
        expectedField6, srcFmtEnt.getField6()));
  }

  @Test
  @DisplayName("Verify can write valid csv line string")
  void canWriteCsvString(){
    
    String csvStringIn = TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2;
    SourceFormatEntry toSrcFmtEnt = SourceFormatEntry.fromCsvString(csvStringIn);
    String  csvStringOut = toSrcFmtEnt.toCsvString();

    assertEquals(csvStringIn, csvStringOut,
        String.format("ToCsvString: \nexpected [%s] \nactual [%s]",
        csvStringIn, csvStringOut));
  }

  @Test
  @DisplayName("Verify csv string validation")
  void csvStringValidation(){

    //testing all test strings to avoid headaches
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_EAST_BLUE_2).isPresent());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_WEST_BLUE_2).isPresent());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_BLUE_ENTRY).isPresent());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_GREEN_ENTRY).isPresent()); 

    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_LABEL_INVALID).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_DIRECTION_INVALID).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_FIELD5_INVALID).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_DATE_INVALID).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_FIELD6_INVALID1).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_FIELD6_INVALID2).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString(TestingUtils.SAMPLE_SOURCE_FIELDNUM_INVALID).isEmpty());
    assertTrue(SourceFormatEntry.validateCsvString("").isEmpty());

  }
}
