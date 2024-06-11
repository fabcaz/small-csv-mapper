package com.test_proj.csv_mapper.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.values.Directions;
import com.test_proj.csv_mapper.values.Labels;

public class SourceFormatEntry implements CsvEntry{

  private static final Logger log = LoggerFactory.getLogger(SourceFormatEntry.class);
  public static final String HEADERS = String.join(",", "field0", "field1", "field2", "field3", "field4", "field5", "field6");

  public static final Comparator<SourceFormatEntry> comparator = 
    (a,b) -> {
      int res = a.getTs().compareTo(b.getTs());
      if (res == 0) {
        res = a.getField4().direction().compareTo(b.getField4().direction());
      }
      return res;
    };

  public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern(
      "yyy-MM-dd HH:mm:ss");
  private static final DecimalFormat df = 
    new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

  private LocalDateTime ts;
  private String internalId;
  private Labels field2;
  private String field3;
  private Directions field4;
  private String field5;
  private Double field6;

  static{
    df.setMaximumFractionDigits(20);
  }

  /** Constructor to be used to read csv line tokens. 
 * @param ts must follow DT_FORMATTER format
 * @param internalId
 * @param field2
 * @param field3
 * @param field4
 * @param field5
 * @param field6
 */
public SourceFormatEntry(
      String ts,
      String internalId,
      String field2,
      String field3,
      String field4,
      String field5,
      String field6){

  this(readTimestampFromString(ts),
      internalId,
      Labels.fromString(field2),
      field3,
      Directions.fromString(field4),
      field5,
      Double.valueOf(field6));
      }

  public SourceFormatEntry(
      LocalDateTime ts,
      String internalId,
      Labels field2,
      String field3,
      Directions field4,
      String field5,
      Double field6){

    this.ts = ts;
    this.internalId = internalId;
    this.field2 = field2;
    this.field3 = field3;
    this.field4 = field4;
    this.field5 = field5;
    this.field6 = field6;
      }

  public LocalDateTime getTs() {
    return ts;
  }
  public void setTs(LocalDateTime ts) {
    this.ts = ts;
  }
  public String getInternalId() {
    return internalId;
  }
  public void setInternalId(String internalId) {
    this.internalId = internalId;
  }
  public Labels getField2() {
    return field2;
  }
  public void setField2(Labels field2) {
    this.field2 = field2;
  }
  public String getField3() {
    return field3;
  }
  public void setField3(String field3) {
    this.field3 = field3;
  }
  public Directions getField4() {
    return field4;
  }
  public void setField4(Directions field4) {
    this.field4 = field4;
  }
  public String getField5() {
    return field5;
  }
  public void setField5(String field5) {
    this.field5 = field5;
  }
  public Double getField6() {
    return field6;
  }
  public void setField6(Double field6) {
    this.field6 = field6;
  }
  private String csvTimestampFormat() {
    return ts.format(DT_FORMATTER);
  }

  public static LocalDateTime readTimestampFromString(String ts){
    return LocalDateTime.parse(ts, DT_FORMATTER);
  }

  public boolean isValid(){
    if (ts == null) {
      return false;
    } else if(internalId == null || internalId.isEmpty()){
      return false;
    } else if(field4 == null || field4.equals(Directions.NONE)){
      return false;
    } else if(field5 == null || field5.isEmpty()){
      return false;
    } else if(field6 == null || field6.equals(Double.valueOf(0))){
      return false;
    }
     return true;
  }


  public static Optional<String[]> validateCsvString(String csvLine){
    boolean isValid = true;
    if (csvLine == null || csvLine.isEmpty()) {
      //duplicating since cant split null or empty string into array of len=6
      log.info("invalid csv String: [{}]", csvLine);
      return Optional.empty();
    }
    String[] tokens = csvLine.split(",", -1);
    if (tokens.length < 7) {
      isValid = false;
    }
    if (Labels.fromString(tokens[2]) == null 
        || Directions.fromString(tokens[4]) == null
        || tokens[5].isEmpty()) {
      isValid = false;
    }

    try {
      readTimestampFromString(tokens[0]);
      Double.valueOf(tokens[6]);
    } catch (Exception e) {
      isValid = false;
    }
    if (!isValid) {
      log.info("invalid csv String: [{}]", csvLine);
      return Optional.empty();
    }
    return Optional.of(tokens);
  }

  /** Constructs SourceFormatEntry from valid cvs line with all 7 columns
 * @param csvLine
 * @return
 */
  public static SourceFormatEntry fromCsvString(String csvLine){
    String[] tokens = csvLine.split(",", -1);

    LocalDateTime ts = readTimestampFromString(tokens[0]);
    String internalId = tokens[1];
    Labels field2 = Labels.fromString(tokens[2]);
    String field3 = tokens[3];
    Directions field4 = Directions.fromString(tokens[4]);
    String field5 = tokens[5];
    Double field6 = tokens[6].isEmpty() ? Double.valueOf("0") : Double.valueOf(tokens[6]);
    
    if (field4.equals(Directions.NONE)) {
      throw new RuntimeException("SourceFormatEntry field4 should not be empty.");
    }

    return new SourceFormatEntry(ts, internalId, field2, field3, field4, field5, field6);
  }

  private String[] stringifyFields(){

    String field2Str = field2 == null ? "" : field2.label();
    String field4Str = field4 == null ? "" : field4.direction();
    String field6Str = field6 == null || field6.equals(Double.valueOf(0)) ? 
      "" : df.format(field6.doubleValue());

    String internalIdStr   = internalId   == null ? "" : internalId;
    String field3Str = field3 == null ? "" : field3;

    String field5Str = field5 == null ? "" : field5;
    return new String[]{
      csvTimestampFormat(),
        internalIdStr,
        field2Str,
        field3Str,
        field4Str,
        field5Str,
        field6Str};
  }
  @Override
  public String toCsvString() {

    String[] fields = stringifyFields();
    return String.format("%s,%s,%s,%s,%s,%s,%s",
        fields[0],
        fields[1],
        fields[2],
        fields[3],
        fields[4],
        fields[5],
        fields[6]
        );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SourceFormatEntry that = (SourceFormatEntry) o;
    return Objects.equals(ts, that.ts) && Objects.equals(internalId, that.internalId) && field2 == that.field2 && Objects.equals(field3, that.field3) && field4 == that.field4 && Objects.equals(field5, that.field5) && Objects.equals(field6, that.field6);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ts, internalId, field2, field3, field4, field5, field6);
  }

  @Override
  public String toString() {
    String[] fields = stringifyFields();
     String jsonLikeFmt = "SourceFormatEntry{"
      +" \"ts\": \"%s\","
      +" \"internalId\": \"%s\","
      +" \"field2\": \"%s\","
      +" \"field3\": \"%s\","
      +" \"field4\": \"%s\","
      +" \"field5\": \"%s\","
      +" \"field6\": \"%s\""
      +'}';
     // swap jsonLikeFmt with justIds to make some validation/filtering tests easier to debug
     String justIds = "SourceFormatEntry{" +
      "\"internalId\"= %s," +
      '}';
     // return String.format(justIds, fields[1]);
    return String.format(jsonLikeFmt,
        fields[0],
        fields[1],
        fields[2],
        fields[3],
        fields[4],
        fields[5],
        fields[6]
        );
  }
}

