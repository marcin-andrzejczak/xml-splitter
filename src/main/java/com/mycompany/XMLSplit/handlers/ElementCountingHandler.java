package com.mycompany.XMLSplit.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ElementCountingHandler extends DefaultHandler implements ValuedHandler {

    private Integer count = 0;
    private String tag;


    public ElementCountingHandler(String tag){
        assert tag != null : "Tag cannot be null!";
        this.tag = tag;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase(tag)){
            count++;
        }
    }

    @Override
    public Object getValue() {
        return count;
    }

}
