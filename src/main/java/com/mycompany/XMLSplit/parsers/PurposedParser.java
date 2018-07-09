package com.mycompany.XMLSplit.parsers;

import java.io.File;

public interface PurposedParser {

    void parse(String fs) throws Exception;

    void parse(File f) throws Exception;

    Object getResult();

}
