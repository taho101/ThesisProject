package com.java.mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaScriptMapper extends Mapper {
	
	private List<String> variables;
	private List<String> functions;
	private List<String> eventListeners;
	private String code;
	
	private String newline = System.getProperty("line.separator");
	
	public JavaScriptMapper() {
		this.mappings = new HashMap<String, String>();
		
		this.functions = new ArrayList<String>();
		this.eventListeners = new ArrayList<String>();
		this.variables = new ArrayList<String>();

		this.fillMappings();
	}
	
	public void mapFunction(String function){
		String[] info = function.split("\n");
		StringBuilder functionBuilder = new StringBuilder();
		
		functionBuilder.append(this.openFunction(info[0]) + this.newline);
		
		for(int i = 1; i < info.length; i++){
			functionBuilder.append(this.applyMappings(info[i]) + this.newline);
		}
		
		this.functions.add(functionBuilder.toString());
	}
	
	public void mapListener(String listener){
		String[] info = listener.split("\n");
		StringBuilder listenerBuilder = new StringBuilder();
		
		listenerBuilder.append(this.openListener(info[0]) + this.newline);
		
		for(int i = 1; i < info.length; i++){
			listenerBuilder.append(this.applyMappings(info[i]) + this.newline);
		}
		
		this.eventListeners.add(listenerBuilder.toString());
	}
	
	
	public void mapVariable(String var){
		
	}
	
	//returns delegate setup for the event listener
	private String openListener(String begin){
		String[] items = begin.split(" ");
		String object = "";
		String event = "";
		
		for(String item : items){
			if(item.contains("(") && !item.contains("function")){
				event = item.substring(item.indexOf("(") + 1, item.length()).replaceAll("\'", "").replaceAll("\"", "").replace(",", "");
				item = item.substring(0, item.indexOf("("));
			}
			
			if(item.contains(".")){
				object = item.replace("$.", "").replace(".addEventListener", "");
				
				this.variables.add("private object " + object + ";");
			}
		}
		
		return object + "." + event + " += delegate {";
	} 
	
	private String openFunction(String begin) {
		String[] items = begin.split(" ");
		String fName = ""; 
		
		if(items[0].equals("var") || items[0].equals("function")){
			fName = (items[1].indexOf("(") > -1 ) ? items[1] : items[1].substring(0, items[1].indexOf("(") - 1);
		}else{
			fName = items[0].substring(items[0].indexOf(".") + 1, items[0].length());
		}
		
		List<String> variables = this.addVariables(items);
		
		//combine variables
		StringBuilder parameters = new StringBuilder();
		for(int i = 0; i < variables.size(); i++){
			if(i > 0) parameters.append(", ");
			parameters.append("Object " + variables.get(i));
		}
		
		return "public void "+ fName +"("+ parameters.toString() +"){ " + this.newline;
	}
	
	//Extract variables from string array
	private List<String> addVariables(String[] items){
		List<String> variables = new ArrayList<String>();
		
		//form the variables
		boolean varStart = false;
		for(int i = 0; i < items.length; i++){
			if(items[i].contains("(")){
				varStart = true;
				
				String possibleVar = items[i].substring(items[i].indexOf("(") + 1, items[i].length()).replaceAll("/\\*.*\\*/", "");
				if(possibleVar.length() > 0){
					if(possibleVar.contains(",")){
						String[] vars = possibleVar.split(",");
						
						for(int j = 0; j < vars.length; j++)
							variables.add(vars[i]);
							
					}else{
						variables.add(possibleVar);
					}
				}
			}else if(varStart == true){
				if(items[i].contains(")")){
					if(items[i].indexOf(")") > 0){
						String possibleVar = items[i].substring(0, items[i].indexOf(")")).replaceAll("/\\*.*\\*/", "");
						
						if(possibleVar.length() > 0){
							if(possibleVar.contains(",")){
								String[] vars = possibleVar.split(",");
								
								for(int j = 0; j < vars.length; j++)
									variables.add(vars[i]);
									
							}else{
								variables.add(possibleVar);
							}
						}
					}
					
					varStart = false;
				}
			}
		}
		
		return variables;
	}
	
	private String applyMappings(String text){
		Iterator<Entry<String, String>> it = this.mappings.entrySet().iterator();
		
		while (it.hasNext()) {
	        Entry<String, String> pair = it.next();
	        text = text.replace(pair.getKey(), pair.getValue());
	    }
		
		return text;
	}
	
	private void fillMappings(){
		this.mappings.put("OS_ANDROID", "Device.OS == TargetPlatform.Android");
		this.mappings.put("OS_IOS", "Device.OS == TargetPlatform.iOS");
		this.mappings.put("};", "}");
		this.mappings.put("click", "TouchDown");
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
