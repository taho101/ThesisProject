package com.java.mappings;

import java.util.HashMap;
import java.util.List;

public class JavaScriptMapper extends Mapper {
	
	private List<String> variables;
	private List<String> functions;
	private List<String> eventListeners;
	private String code;
	
	public JavaScriptMapper() {
		this.mappings = new HashMap<String, String>();

		this.mappings.put("OS_ANDROID", "Device.OS == TargetPlatform.Android");
		this.mappings.put("OS_IOS", "Device.OS == TargetPlatform.iOS");
	}
	
	public void mapFunction(String function){
		
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
