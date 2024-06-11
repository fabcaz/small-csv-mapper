package com.test_proj.csv_mapper.mappers.converters;

import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.mappers.EntryMapper;

public interface EntryConverter<S extends CsvEntry,D extends CsvEntry> extends EntryMapper<S,D>{
  D convert(S src);

  //change this to a lower LCA
}
/*
 * 1) could do what HandlerFunctionAdater does which is to have convert(..) cast
 * Object args to desired Classes i.e. assume that acceptsPair() has been called
 * before to verify that calling convert() won't throw because the casting is
 * invalid
 *
 * 2)could also have method convertIfSupports(Object src, Object dest)
 * first calls acceptsPair then casts the Objects to S and D
 *
 * but might need to throw if it does not support -> how could that be handled
 * by filter()?
 *
 */
