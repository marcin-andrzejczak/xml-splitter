package com.mycompany.XMLSplit.parsers.sax;

import com.mycompany.XMLSplit.handlers.ElementCountingHandler;
import com.mycompany.XMLSplit.parsers.PurposedParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;

public class ElementCountingParser implements PurposedParser {

    private String tag;
    private Integer count;

    public ElementCountingParser(){}

    public ElementCountingParser(String tag){
        this.tag = tag;
    }

    @Override
    public void parse(String fs) throws Exception {
        File file = new File(fs);
        if(!file.exists()){
            throw new FileNotFoundException("File \'" + fs + "\' was not found!");
        }
        parse(file);
    }

    @Override
    public void parse(File f) throws Exception {
        if( tag == null || tag.isEmpty() ){
            throw new Exception("Tag cannot be empty or null!");
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        ElementCountingHandler handler = new ElementCountingHandler(tag);
        saxParser.parse(f, handler);

        count = (Integer) handler.getValue();
    }

    @Override
    public Integer getResult() {
        return count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
