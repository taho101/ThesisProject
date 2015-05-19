package com.java.parsers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser{
	public Document doc;
	
	public XMLParser(File file) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		this.doc = builder.parse(file);
		this.doc.getDocumentElement().normalize();
	}
	
	public Map<String, String> parseTableView(){
		NodeList nList = doc.getElementsByTagName("TableView");
		
		Map<String, String> code = new HashMap<String, String>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			//use XML mappings to create the equivalent code for Xamarin
			code.put("TagName", nNode.getNodeName());
			if(nNode.hasAttributes()){
				
			}
			
		}
		
		return code;
	}
}
