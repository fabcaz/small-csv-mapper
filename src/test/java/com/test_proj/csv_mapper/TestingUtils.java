package com.test_proj.csv_mapper;

import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;

public class TestingUtils {

  //change 03:43:42 to 02:03:04
  public static final String SAMPLE_SOURCE_EAST_BLUE_2 = "2022-01-01 02:03:04,000000001,blue,,East,DEF,-10";
  public static final String SAMPLE_SOURCE_WEST_BLUE_2 = "2022-01-01 02:03:04,000000001,blue,,West,ABC,10";
  public static final String SAMPLE_SOURCE_BLUE_ENTRY  = "2022-01-01 02:03:04,000000002,blue,,East,DEF,-10";
  public static final String SAMPLE_SOURCE_GREEN_ENTRY = "2022-01-01 07:54:19,000000003,green,,West,GHI,0.00059762";

  public static final String SAMPLE_SOURCE_EAST_BLUE_2_CONVERTED2INTERM = "2022-01-01T02:03:04,000000001,East,DEF,10,blue";
  public static final String SAMPLE_SOURCE_WEST_BLUE_2_CONVERTED2INTERM = "2022-01-01T02:03:04,000000001,West,ABC,10,blue";

  public static final String SAMPLE_SOURCE_GREEN_CONVERTED2TARGET = "2022-01-01 07:54:19 UTC,,,0.00059762,GHI,,,,,green,,";

  public static final String SAMPLE_INTERM_WEST_1         = "2022-11-22T04:17:14,000000004,West,ABC,10,blue";
  public static final String SAMPLE_INTERM_EAST_1         = "2022-11-22T04:17:14,000000004,East,DEF,10,blue";
  public static final String SAMPLE_INTERM_MERGE_OUTPUT_1 = "2022-11-22 04:17:14 UTC,10,DEF,10,ABC,,,,,blue,,";
  public static final String SAMPLE_INTERM_WEST_2         = "2022-10-22T05:17:14,000000004,West,ABC,15,blue";
  public static final String SAMPLE_INTERM_EAST_2         = "2022-10-22T05:17:14,000000004,East,DEF,15,blue";
  public static final String SAMPLE_INTERM_MERGE_OUTPUT_2 = "2022-10-22 05:17:14 UTC,15,DEF,15,ABC,,,,,blue,,";

  public static final String SAMPLE_K_GREEN = "2022-10-22 05:17:14 UTC,,,15,ABC,,,,,green,,";

  public static final String SAMPLE_SOURCE_LABEL_INVALID     = "2022-01-01 02:03:04,111111111,FAKELABEL,,East,DEF,-10";
  public static final String SAMPLE_SOURCE_DIRECTION_INVALID = "2022-01-01 02:03:04,222222222,blue,,FAKEDIRECTION,DEF,-10";
  public static final String SAMPLE_SOURCE_FIELD5_INVALID    = "2022-01-01 02:03:04,333333333,blue,,East,,-10";
  public static final String SAMPLE_SOURCE_DATE_INVALID      = "yyyy-mm-dd hh:mm:ss,444444444,blue,,East,DEF,-10";
  public static final String SAMPLE_SOURCE_FIELD6_INVALID1   = "2022-01-01 02:03:04,555555555,blue,,East,DEF,NOAMOUT";
  public static final String SAMPLE_SOURCE_FIELD6_INVALID2   = "2022-01-01 02:03:04,666666666,blue,,East,DEF,";
  public static final String SAMPLE_SOURCE_FIELDNUM_INVALID  = "2022-01-01 02:03:04,777777777,blue,,East,";

  public static final String SAMPLE_INTERM_INVALID           = "2022-11-22T04:17:14,888888888,,ABC,10,blue";

  public static final String SAMPLE_TARGET_GREEN_NOFIELD3_INVALID = "2022-10-22 05:17:14 UTC,,,,ABC,,,,,green,,";

  

  public static SourceFormatEntry srcFmtEntFromCsvString(String str){
    return SourceFormatEntry.fromCsvString(str);
  }

  public static IntermediateEntry intermFmtEntFromString(String str){
    String[] args = str.split(",");
    return new IntermediateEntry(args[0], args[1], args[2], args[3], args[4]);
  }

  public static TargetFormatEntry targFmtEntFromCsvLine(String str){
    return TargetFormatEntry.fromCsvString(str);
  }
}
