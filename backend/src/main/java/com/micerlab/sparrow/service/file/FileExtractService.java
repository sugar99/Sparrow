package com.micerlab.sparrow.service.file;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.util.List;

public interface FileExtractService {

    List<String> findKeyword(String text, int k) throws IOException, OpenXML4JException, XmlException;
}
