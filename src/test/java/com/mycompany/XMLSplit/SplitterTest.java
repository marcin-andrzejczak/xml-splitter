package com.mycompany.XMLSplit;

import com.mycompany.XMLSplit.parsers.ElementCountingParser;
import com.mycompany.XMLSplit.parsers.PurposedParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SplitterTest {

    private String tag = "employee";
    private Splitter splitter;
    private List<String> filesToClear;

    @Before
    public void setUp(){
        splitter = new Splitter(tag);
        filesToClear = new ArrayList<>();
    }

    @Test
    public void testSplitFilesNumber(){
        String dataFile = "src/test/resources/data/data.xml";

        try {
            splitter.split(dataFile, 2, tag);
        } catch (Exception ex){
            fail("Exception occurred!");
            ex.printStackTrace();
        }

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("^data.*\\.xml");
            }
        };
        File[] files = new File("src/test/resources/data/").listFiles(filter);
        assertEquals(3, files != null ? files.length : 0);
    }

    @Test
    public void testSplitFilesContentTagsCount(){
        String dataFile = "src/test/resources/data/data.xml";
        String[] outputFiles = { "src/test/resources/data/data_output_0.xml",
                                 "src/test/resources/data/data_output_1.xml" };
        filesToClear.addAll(Arrays.asList(outputFiles));

        try {
            splitter.split(dataFile, 2, tag);
            PurposedParser parser = new ElementCountingParser(tag);

            parser.parse(outputFiles[0]);
            if ((int) parser.getResult() != 3) {
                fail("Counter returned wrong number of elements! Returned " + parser.getResult() + " instead of 3!");
            }

            parser.parse(outputFiles[1]);
            if ((int) parser.getResult() != 2) {
                fail("Counter returned wrong number of elements! Returned " + parser.getResult() + " instead of 2!");
            }

        } catch (Exception ex){
            fail("Exception occurred!");
            ex.printStackTrace();
        }
    }

    @After
    public void clearData(){
        try {
            for (String file : filesToClear) {
                Files.deleteIfExists(Paths.get(file));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}