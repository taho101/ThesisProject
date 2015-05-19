package com.java.builders;

import java.util.Map;

public class XAMLBuilder extends XMLBuilder{
	
	public void startContent(String className){
		this.xml.append("<ContentPage xmlns='http://xamarin.com/schemas/2014/forms' " +
             			"xmlns:x='http://schemas.microsoft.com/winfx/2009/xaml' " +
             			"x:Class='"+ className +"'>" + this.newline);
	}
	
	public void endContent(){
		this.xml.append("</ContentPage>" + this.newline);
	}
	
	public void buildView(Map<String, String> elements){
		this.xml.append("<"+ elements.get("TagName") +">");
		this.xml.append("</"+ elements.get("TagName") +">");
	}
}
