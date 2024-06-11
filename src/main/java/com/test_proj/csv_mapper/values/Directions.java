package com.test_proj.csv_mapper.values;

public enum Directions {
  EAST("East"),
  WEST("West"),
  NONE("");

  private String direction;

  Directions(String direction){
    this.direction = direction;
  }

  public String direction(){
    return direction;
  }
  public static Directions fromString(String str){
    for (Directions d : Directions.values()) {
      if (d.direction.equalsIgnoreCase(str)) {
        return d;
      }
    }
    return null;
  }
}
