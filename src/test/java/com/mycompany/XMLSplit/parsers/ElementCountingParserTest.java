package com.mycompany.XMLSplit.parsers;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ElementCountingParserTest {

    private final String filePath = "src/test/resources/data/data.xml";
    private final String tagTest = "employee";
    private File xmlFile;

    @Before
    public void setUp() {
        xmlFile = new File(filePath);
    }

    @Test
    public void testCountWithTagConstructor(){
        try {

            PurposedParser parser = new ElementCountingParser(tagTest);
            parser.parse(xmlFile);
            if( (int) parser.getResult() != 5){
                fail("Counter returned wrong number of elements! Returned "+parser.getResult()+" instead of 5!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}