package com.test_proj.csv_mapper;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test_proj.csv_mapper.configurators.FileMetadata;
import com.test_proj.csv_mapper.mappers.converters.Source2Intermediate;
import com.test_proj.csv_mapper.mappers.converters.Source2Target;
import com.test_proj.csv_mapper.entities.SourceFormatEntry;
import com.test_proj.csv_mapper.entities.TargetFormatEntry;
import com.test_proj.csv_mapper.mappers.mergers.Intermediate2Target;
import com.test_proj.csv_mapper.services.SourceFormatEntryReaderService;
import com.test_proj.csv_mapper.services.GeneralWriterService;
import com.test_proj.csv_mapper.services.MappingService;
import com.test_proj.csv_mapper.services.MappingServiceImpl;
import com.test_proj.csv_mapper.services.ReaderService;
import com.test_proj.csv_mapper.services.WriterService;


public class App 
{

  private static final Logger log = LoggerFactory.getLogger(App.class);
  private static final DateTimeFormatter fileNameTsFmt = DateTimeFormatter.ofPattern("yyyy_MM_dd'T'HH_mm_ss");

  public static void main( String[] args ) throws FileNotFoundException
  {
    // check if source and dest file names have been passed as system props to 
    // set them as vars then create FileMetadata instances.
    if (System.getProperty("csvPath") == null) {
      throw new NullPointerException("need to set -DcsvPath=\"path/to.file.csv\"");
    }

    String srcFp = System.getProperty("csvPath");

    FileMetadata srcFm = new FileMetadata(srcFp, ",", SourceFormatEntry.class);
    if (!srcFm.fileExists()){ 
      throw new RuntimeException("csvPath points to non-existant file.");
    }
    log.info("created source FileMetadata for location: {}", srcFp);
    log.info("created source FileMetadata filename: {}", srcFm.getFile().getName());
    log.debug("source format = {}", srcFm.getCsvFormat().getName());

    // may not create new file in same dir as source
    String destFp = System.getProperty("destPath") != null ?
      System.getProperty("destPath")
      :
      srcFm.getFile().getName().split("\\.")[0]
      .concat("_output")
      .concat(LocalDateTime.now().format(fileNameTsFmt))
      .concat(".csv");
      
    FileMetadata destFm = new FileMetadata(destFp, ",", TargetFormatEntry.class);
    log.info("created dest FileMetadata for location: {}", destFp);
    log.debug("target format = {}", destFm.getCsvFormat().getName());

    //set up services
    ReaderService<SourceFormatEntry> readerService = new SourceFormatEntryReaderService();
    WriterService writerService = new GeneralWriterService();
    MappingService mappingService = new MappingServiceImpl(
        new Source2Target(),
        new Source2Intermediate(),
        new Intermediate2Target());

    List<SourceFormatEntry> sourceEntries = readerService.readFileIntoEntryList(srcFm);
    log.info("Created {} entries from source file", sourceEntries.size());

    List<TargetFormatEntry> targetEntries = mappingService.transformEntries(sourceEntries);

    writerService.writeLines(destFm, targetEntries);

  }

}
