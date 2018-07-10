package com.mycompany.XMLSplit;

import com.mycompany.XMLSplit.parsers.sax.ElementCountingParser;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Splitter {

    private XMLInputFactory inFactory;
    private XMLOutputFactory outFactory;
    private XMLEventReader eventReader;
    private XMLEventFactory eventFactory;

    private List<XMLEventWriter> outputWriters;

    private String tag;

    public Splitter(String tag){
        this.tag = tag;
        init();
    }

    private void init(){
        inFactory = XMLInputFactory.newInstance();
        outFactory = XMLOutputFactory.newInstance();
        eventFactory = XMLEventFactory.newInstance();
    }

    public void split(String source, Integer outputFilesNumber, String byTag) throws Exception{
        split(new File(source), outputFilesNumber, byTag);
    }

    public void split(File source, Integer outputFilesNumber, String byTag) throws Exception{
        createWriters(outputFilesNumber, source);

        Iterator<XMLEventWriter> writerIterator = outputWriters.iterator();
        int maxTagsPerFile = countMaxTagElementsByFile(source, outputFilesNumber);
        int currentWriterTagCount = 0;
        if(!writerIterator.hasNext() || maxTagsPerFile == currentWriterTagCount){
            return;
        }

        XMLEventWriter currentWriter = writerIterator.next();
        eventReader = inFactory.createXMLEventReader(new FileReader(source));
        while (eventReader.hasNext()) {
            if(currentWriterTagCount >= maxTagsPerFile){
                currentWriterTagCount = 0;
                currentWriter = writerIterator.next();
            }

            XMLEvent event = eventReader.peek();
            if ( event.getEventType() == XMLStreamConstants.START_ELEMENT &&
                 event.asStartElement().getName().getLocalPart().equalsIgnoreCase(tag))
            {
                writeWholeElement(eventReader, currentWriter);
                currentWriterTagCount++;
            } else {
                writeToAll(eventReader.nextEvent());
                if(event.isStartDocument()){
                    writeToAll(eventFactory.createCharacters("\n"));
                }
            }

            flushWriters();
        }

        eventReader.close();
        closeWriters();
    }

    private void writeWholeElement(XMLEventReader reader, XMLEventWriter writer){
        try {
            while( reader.hasNext() ){
                XMLEvent event = reader.nextEvent();
                writer.add(event);
                if( event.isEndElement() && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(tag) ){
                    if( reader.peek().isCharacters() && reader.peek().asCharacters().isWhiteSpace() ){
                        writer.add(reader.nextEvent());
                    }
                    return;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private int countMaxTagElementsByFile(File source, int filesNum){
        int result = 0;
        try {
            ElementCountingParser counter = new ElementCountingParser(tag);
            counter.parse(source);
            result = (int) Math.ceil(counter.getResult().doubleValue() / filesNum);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    private void createWriters(Integer writersNumber, File source){
        for (int i = 0; i < writersNumber; i++) {
            addWriter(i, source);
        }
    }

    private void addWriter(int fileNumber, File source){
        if(outputWriters == null){
            outputWriters = new ArrayList<>();
        }
        String fileName = source.getParent() + "/" + source.getName().replaceFirst("[.][^.]+$", "") + String.format("_output_%d.xml", fileNumber);
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            outputWriters.add(outFactory.createXMLEventWriter(fileWriter));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void closeWriters(){
        try {
            for (XMLEventWriter writer : outputWriters) {
                writer.close();
                writer = null;
            }
            System.gc();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void flushWriters(){
        try {
            for (XMLEventWriter writer : outputWriters) {
                writer.flush();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void writeToAll(XMLEvent event){
        try {
            for (XMLEventWriter writer : outputWriters) {
                writer.add(event);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
