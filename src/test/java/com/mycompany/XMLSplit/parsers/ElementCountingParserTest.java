package com.mycompany.XMLSplit.parsers;

import com.mycompany.XMLSplit.parsers.sax.ElementCountingParser;
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

    @Test
    public void testCountWithTagGetterSetter(){
        try {

            ElementCountingParser parser = new ElementCountingParser();
            parser.setTag(tagTest);
            parser.parse(xmlFile);
            if( parser.getResult() != 5 ){
                fail("Counter returned wrong number of elements! Returned "+parser.getResult()+" instead of 5!");
            }

            if( !parser.getTag().equals(tagTest) ){
                fail("Getter returned '"+parser.getTag()+"' instead of '"+tagTest+"'!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShouldNotFindFile(){
        try {

            PurposedParser parser = new ElementCountingParser(tagTest);
            parser.parse("Not a chance it will be found :)");
            fail("Parser did not throw an exception!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShouldNotAcceptEmptyTag(){
        try {

            PurposedParser parser = new ElementCountingParser("");
            parser.parse(xmlFile);
            fail("Parser did not throw an exception!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}