package com.java.mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaScriptMapper extends Mapper {
	
	private List<String> variables;
	private List<String> functions;
	private List<String> eventListeners;
	private String code;
	
	private String newline = System.getProperty("line.separator");
	
	public JavaScriptMapper() {
		this.mappings = new HashMap<String, String>();

		this.mappings.put("OS_ANDROID", "Device.OS == TargetPlatform.Android");
		this.mappings.put("OS_IOS", "Device.OS == TargetPlatform.iOS");
	}
	
	public void mapFunction(String function){
		String[] info = function.split("\n");
		
		StringBuilder functionBuilder = new StringBuilder();
		
		functionBuilder.append(this.openFunction(info[0]));
		
		System.out.println(functionBuilder.toString());
	}
	
	private String openFunction(String begin) {
		String[] items = begin.split(" ");
		String fName = ""; 
		List<String> variables = new ArrayList<String>();
		
		if(items[0].equals("var") || items[0].equals("function")){
			fName = (items[1].indexOf("(") > -1 ) ? items[1] : items[1].substring(0, items[1].indexOf("(") - 1);
		}else{
			fName = items[0].substring(items[0].indexOf(".") + 1, items[0].length());
		}
		
		//form the variables
		if(items[0].equals("function")){
			
		}else{
			for(int i = 1; i < items.length; i++){
				
			}
		}
		
		//combine variables
		StringBuilder parameters = new StringBuilder();
		for(int i = 0; i < variables.size(); i++){
			if(i > 0) parameters.append(", ");
			parameters.append("Object " + variables.get(i));
		}
		
		return "public void "+ fName +"("+ parameters.toString() +"){ " + this.newline;
	}

	public void mapVariable(String var){
		
	}
	
	
	/*
	 * Get functions for the completed mapped elements
	 */
	
	public List<String> getVariables() {
		return variables;
	}

	public List<String> getFunctions() {
		return functions;
	}

	public List<String> getEventListeners() {
		return eventListeners;
	}

	public String getCode() {
		return code;
	}
}
