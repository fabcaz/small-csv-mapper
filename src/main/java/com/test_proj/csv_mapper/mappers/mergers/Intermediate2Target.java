package com.test_proj.csv_mapper.mappers.mergers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.entities.CsvEntry;
import com.test_proj.csv_mapper.entities.IntermediateEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.mappers.ManyToOneEntryMapper;
import com.test_proj.csv_mapper.values.Directions;

public class Intermediate2Target implements ManyToOneEntryMapper<TargetFormatEntry>{


  private static final Logger log = LoggerFactory.getLogger(Intermediate2Target.class);

	/* Should verify that the pair is valid before merging.
	 * @see com.test_proj.csv_mapper.mappers.ManyToOneEntryMapper#merge(java.util.List)
	 */
	@Override
	public TargetFormatEntry merge(List<? extends CsvEntry> src) {

    if(src == null){
      throw new IllegalArgumentException("arg connot be null");
    }
    if(src.size() != 2 ){
      log.debug("Lsit should be size 2: [{}]", src);
      throw new IllegalArgumentException("arg length must be of size two but is " + src.size());
    }
    
    // should extract ordering of elements
    boolean firstIsWest = ((IntermediateEntry)src.get(0))
      .getField2().equals(Directions.WEST);

    int credIdx, debIdx;
    
    if (firstIsWest) {
      credIdx = 0; debIdx = 1;
    }else{
      credIdx = 1; debIdx = 0;
    }
    
    IntermediateEntry cred = 
      ((IntermediateEntry)src.get(credIdx));

    IntermediateEntry deb = 
      ((IntermediateEntry)src.get(debIdx));

    TargetFormatEntry targ = new TargetFormatEntry(cred.getInternalId(),
        cred.getTs(),
        deb.getField3(),
        cred.getField3(),
        deb.getField4(),
        cred.getField4(),
        cred.getField5());

    log.debug("merged [{}] into [{}]", src, targ);
    return targ;
	}

  @Override
  public boolean accepts(List<? extends CsvEntry> srcList){
    return srcList.size() == 2
      && srcList.get(0) instanceof IntermediateEntry 
      && srcList.get(1) instanceof IntermediateEntry
      && srcList.get(0).isValid()
      && srcList.get(1).isValid()
      && srcList.stream()
          .filter(ent -> ((IntermediateEntry)ent).getField2()
          .equals(Directions.EAST)).count() == 1
      && srcList.stream()
          .filter(ent -> ((IntermediateEntry)ent).getField2()
          .equals(Directions.WEST)).count() == 1;
  }

	@Override
	public List<Class<? extends CsvEntry>> getSourceTypes() {
		return List.of(IntermediateEntry.class);
	}

	@Override
	public Class<TargetFormatEntry> getTargetType() {
		return TargetFormatEntry.class;
	}

}
