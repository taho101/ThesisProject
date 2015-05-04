package com.java.builders;

public class XMLBuilder {
	
	private StringBuilder xml;
	private String newline = "\n";
	
	public XMLBuilder(){
		this.xml = new StringBuilder();
		this.xml.append("<?xml version='1.0' encoding='utf-8'?>" + newline);
	}
	
	public void startComponent(String id){
		this.xml.append("<component format='1' id='"+ id +"'>" + newline);
	}
	
	public void addElement(String data, String name){
		this.xml.append("<"+ name +">"+ data +"</"+ name +">" + newline);
	}
	
	public void endComponent(){
		this.xml.append("</component>");
	}
	
	public String getXML(){
		return this.xml.toString();
	}
}
