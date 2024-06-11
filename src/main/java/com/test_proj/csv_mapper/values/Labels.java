package com.test_proj.csv_mapper.values;

public enum Labels {
  GREEN("green"),
  BLUE("blue"),
  NONE("");

  private String label;

  Labels(String label){
    this.label = label;
  }

  public String label(){
    return label;
  }

  public static Labels fromString(String str){
    for (Labels l : Labels.values()) {
      if (l.label.equalsIgnoreCase(str)) {
        return l;
      }
    }
    return null;
  }
}
