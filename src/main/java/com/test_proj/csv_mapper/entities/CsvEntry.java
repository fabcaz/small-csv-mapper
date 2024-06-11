package com.test_proj.csv_mapper.entities;

import com.test_proj.csv_mapper.mappers.converters.EntryConverter;

// maybe turn this into an interface
/**
 * Only exists to create an upper bound for the {@link EntryConverter} and 
 * {@link ConverterOrganizer}
 */
public interface CsvEntry {
  boolean isValid();
  String toCsvString();
}
