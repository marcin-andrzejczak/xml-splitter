package com.mycompany.XMLSplit.parsers;

import java.io.File;

public interface SimpleParser {

    void parse(String fs) throws Exception;

    void parse(File f) throws Exception;

}
