package com.test_proj.csv_mapper.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import com.test_proj.csv_mapper.values.Directions;
import com.test_proj.csv_mapper.values.Labels;

public class IntermediateEntry implements CsvEntry{

  public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern(
      "yyy-MM-dd HH:mm:ss");

  public static final Comparator<SourceFormatEntry> comparator = 
    (a,b) -> {
      int res = a.getTs().compareTo(b.getTs());
      if (res == 0) {
        res = a.getField4().direction().compareTo(b.getField4().direction());
      }
      return res;
    };
  private static final DecimalFormat df = 
    new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

  private LocalDateTime ts;
  private String internalId;
  private Directions field2; // field2 field2
  private String field3; // field3 field3
  private Double field4; // field4 field4
  private Labels field5; // field5 field5

  public IntermediateEntry(
      String ts,
      String internalId,
      String field2,
      String field3,
      String field4
      ) {
    this(
        LocalDateTime.parse(ts),
        internalId,
        Directions.fromString(field2),
        field3,
        Double.valueOf(field4)
        );
      }
  public IntermediateEntry(
      LocalDateTime ts,
      String internalId,
      Directions field2,
      String field3,
      Double field4
      ) {
    this.ts = ts;
    this.internalId = internalId;
    this.field2 = field2;
    this.field3 = field3;
    this.field4 = Math.abs(field4);
    this.field5 = Labels.BLUE; // should make this.field5 a private static final = Labels.X ; or not have a field at all just a getter returning X
      }

  public LocalDateTime getTs() {
    return ts;
  }

  public void setTs(LocalDateTime ts) {
    this.ts = ts;
  }

  public Directions getField2() {
    return field2;
  }

  public void setField2(Directions field2) {
    this.field2 = field2;
  }

  public String getField3() {
    return field3;
  }

  public void setField3(String field3) {
    this.field3 = field3;
  }

  public Double getField4() {
    return field4;
  }

  public void setField4(Double field4) {
    this.field4 = field4;
  }

  public Labels getField5() {
    return field5;
  }

  public void setField5(Labels field5) {
    this.field5 = field5;
  }

  public String getInternalId() {
    return internalId;
  }

  public void setInternalId(String internalId) {
    this.internalId = internalId;
  }

  /**
   * Checks if given entry has all fields set and has the expected field5 else
   * throws RTE
   * 
   * @return true or throws if invalid
   */
  public boolean isValid() {

    if(this.internalId == null){
      return false;
    }
    if(this.field2 == null){
      return false;
    }else if(!this.field2.equals(Directions.EAST) &&
        !this.field2.equals(Directions.WEST)){
      return false;
        }
    if(this.field3 == null){
      return false;
    }
    if(this.field4 == null){
      return false;
    }else if(this.field4 < 0 ){
      return false;
    }
    if(this.field5 == null){
      return false;
    }else if(!this.field5.equals(Labels.BLUE)){
      return false;
    }

    return true;
  }

  private String csvTimestampFormat() {
    return ts.format(DT_FORMATTER);
  }

  private String[] stringifyFields(){

    String field2Str = field2 == null ? "" : field2.direction();
    String field3Str = field3 == null ? "" : field3;
    String field4Str = field4 == null || field4.equals(Double.valueOf(0)) ? 
      "" : df.format(field4.doubleValue());
    String field5Str = field5 == null ? "" : field5.label();

    String internalIdStr   = internalId   == null ? "" : internalId;

    return new String[]{
        csvTimestampFormat(),
        internalIdStr,
        field5Str,
        field3Str,
        field2Str,
        field4Str
    };
  }

  @Override
  public String toCsvString() {

    String[] fields = stringifyFields();
    return String.format("%s,%s,%s,%s,%s,%s",
        fields[0],
        fields[1],
        fields[2],
        fields[3],
        fields[4],
        fields[5]
        );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IntermediateEntry that = (IntermediateEntry) o;
    return Objects.equals(ts, that.ts) && Objects.equals(internalId, that.internalId) && field2 == that.field2 && Objects.equals(field3, that.field3) && Objects.equals(field4, that.field4) && field5 == that.field5;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ts, internalId, field2, field3, field4, field5);
  }

  @Override
  public String toString() {
    String jsonLikeFmt = "IntermediateEntry{" 
      + " \"ts\": \"%s\","
      + " \"internalId\": \"%s\","
      + " \"field2\": \"%s\","
      + " \"field3\": \"%s\","
      + " \"field4\": \"%s\","
      + " \"field5\": \"%s\""
      + "}";
    String[] fields = stringifyFields();
    return String.format(jsonLikeFmt,
        fields[0],
        fields[1],
        fields[2],
        fields[3],
        fields[4],
        fields[5]
        );
  }
}
