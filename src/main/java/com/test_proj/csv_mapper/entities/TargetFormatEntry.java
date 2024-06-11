package com.test_proj.csv_mapper.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.values.Labels;

public class TargetFormatEntry implements CsvEntry{
  
  
  private static final Logger log = LoggerFactory.getLogger(TargetFormatEntry.class);
  public static final String HEADERS = String.join(",", "field0", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "field10", "field11");
  public static final Comparator<TargetFormatEntry> comparator = 
    (a,b) -> a.getTs().compareTo(b.getTs());
  private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss 'UTC'");
  private static final DecimalFormat df =
    new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

  private String internalId; 
  private LocalDateTime ts; 
  private String field2; 
  private String field4; 
  private Double field1; 
  private Double field3; 
  private Labels field9; 

  static{
    df.setMaximumFractionDigits(20);
  }

  public TargetFormatEntry(
      String internalId,
      LocalDateTime ts,
      String field2,
      String field4,
      Double field1,
      Double field3,
      Labels field9){

    if(field1 == null){field1 = Double.valueOf(0);}
    if(field3 == null){field3 = Double.valueOf(0);}
    if(field2 == null){field2 = "";}
    if(field4 == null){field4 = "";}
    this.internalId = internalId;
    this.ts = ts;
    this.field2 = field2;
    this.field4 = field4;
    this.field1 = Math.abs(field1);
    this.field3 = Math.abs(field3);
    this.field9 = field9;
      }

  public LocalDateTime getTs() {
    return ts;
  }
  public void setTs(LocalDateTime ts) {
    this.ts = ts;
  }
  public String getField2() {
    return field2;
  }
  public void setField2(String field2) {
    this.field2 = field2;
  }
  public String getField4() {
    return field4;
  }
  public void setField4(String recievedCurrency) {
    this.field4 = recievedCurrency;
  }
  public Double getField1() {
    return field1;
  }
  public void setField1(Double field1) {
    this.field1 = field1;
  }
  public Double getField3() {
    return field3;
  }
  public void setField3(Double recievedAmount) {
    this.field3 = recievedAmount;
  }
  public String getInternalId() {
    return internalId;
  }
  public void setInternalId(String internalId) {
    this.internalId = internalId;
  }
  public Labels getField9() {
    return field9;
  }
  public void setField9(Labels field9) {
    this.field9 = field9;
  }


  public boolean hasSentInfo(){
    return field2 != null && !field2.isEmpty() 
      && field1 != null && field1 > 0;
  }
  public boolean hasRecieveInfo(){
    return field4 != null && !field4.isEmpty() 
      && field3 != null && field3 > 0;
  }

  public boolean hasSentAndRecieveInfo(){
    return this.hasSentInfo() && this.hasRecieveInfo();
  }
  public static LocalDateTime readTimestampFromString(String ts){
    return LocalDateTime.parse(ts, DT_FORMATTER);
  }
  private String csvTimestampFormat() {
    return ts.format(DT_FORMATTER);

  }

  public boolean isValid(){
    if(ts == null){
      return false;
    }
    if((field1 == null || field1.equals(Double.valueOf(0))) 
        && (field3 == null || field3.equals(Double.valueOf(0)))){
      return false;
    }
    if((field4 == null || field4.isEmpty())
        && (field2 == null || field2.isEmpty())){
      return false;
    }
    return true;
  }

  /** Constructs TargetEntryFormat from valid cvs line with all 12 columns
 * @param csvLine
 * @return
 */
  public static TargetFormatEntry fromCsvString(String csvLine){
    String[] tokens = csvLine.split(",", -1);

    LocalDateTime ts = readTimestampFromString(tokens[0]);
    Double field1 = tokens[1].isEmpty() ? Double.valueOf("0") : Double.valueOf(tokens[1]);
    String field2 = tokens[2];
    Double field3 = tokens[3].isEmpty() ? Double.valueOf("0") : Double.valueOf(tokens[3]);
    String field4 = tokens[4];
    Labels field9 = Labels.fromString(tokens[9]);

    return new TargetFormatEntry("", ts, field2, field4, field1, field3, field9);
  }

  private String[] stringifyFields(){

    String field2Str = field2 == null ? "" : field2;
    String field4Str = field4 == null ? "" : field4;

    String field1Str = field1 == null || field1.equals(Double.valueOf(0)) ? 
      "" : df.format(field1.doubleValue());
    String field3Str = field3 == null || field3.equals(Double.valueOf(0)) ?
      "" : df.format(field3.doubleValue());
    
    String labelStr = field9 == null ? "" :  field9.label();
    return new String[] {
      csvTimestampFormat(), // field0
      field1Str,        // field1
      field2Str,      // field2
      field3Str,    // field3
      field4Str,  // field4
      "",                   // field5
      "",                   // field6
      "",                   // field7
      "",                   // field8
      labelStr,             // field9
      "",                   // field10
      ""                    // field11
    };
  }

  @Override
  public String toCsvString() {

    String[] fields = stringifyFields();
    return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
        fields[0],  // field0
        fields[1],  // field1
        fields[2],  // field2
        fields[3],  // field3
        fields[4],  // field4
        fields[5],  // field5
        fields[6],  // field6
        fields[7],  // field7
        fields[8],  // field8
        fields[9],  // field9
        fields[10], // field10
        fields[11]  // field11
        );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TargetFormatEntry that = (TargetFormatEntry) o;
    return Objects.equals(internalId, that.internalId) && Objects.equals(ts, that.ts) && Objects.equals(field2, that.field2) && Objects.equals(field4, that.field4) && Objects.equals(field1, that.field1) && Objects.equals(field3, that.field3) && field9 == that.field9;
  }

  @Override
  public int hashCode() {
    return Objects.hash(internalId, ts, field2, field4, field1, field3, field9);
  }

  @Override
  public String toString() {

    String jsonLikeFmt = "TargetEntryFormat{" 
  +" \"date\": \"%s\","
  +" \"field1\": \"%s\","
  +" \"field2\": \"%s\","
  +" \"field3\": \"%s\","
  +" \"field4\": \"%s\","
  +" \"field5\": \"%s\","
  +" \"field6\": \"%s\","
  +" \"field7\": \"%s\","
  +" \"field8\": \"%s\","
  +" \"field9\": \"%s\","
  +" \"field10\": \"%s\","
  +" \"field11\": \"%s\""
  +"}";
    String[] fields = stringifyFields();
    return String.format(jsonLikeFmt,
        fields[0],  // field0
        fields[1],  // field1
        fields[2],  // field2
        fields[3],  // field3
        fields[4],  // field4
        fields[5],  // field5
        fields[6],  // field6
        fields[7],  // field7
        fields[8],  // field8
        fields[9],  // field9
        fields[10], // field10
        fields[11]  // field11
        );
  }
}

