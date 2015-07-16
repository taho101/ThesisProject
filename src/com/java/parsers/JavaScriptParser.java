package com.java.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.java.mappings.JavaScriptMapper;

public class JavaScriptParser {

	private List<String> variables;
	private Map<String, String> functions;
	private Map<String, String> eventListeners;
	private String code;

	private JavaScriptMapper mappings;

	private boolean inFunction = false;
	private boolean inListener = false;
	private int brackets = 0;

	private String event = "";
	private String function = "";
	
	private String newline = System.getProperty("line.separator");

	public JavaScriptParser() {
		this.variables = new ArrayList<String>();
		this.functions = new HashMap<String, String>();
		this.eventListeners = new HashMap<String, String>();

		this.mappings = new JavaScriptMapper();
	}

	public void applyMappings(){
		for(String function : this.functions.values()){
			mappings.mapFunction(function);
		}
		
		for(String var : this.variables){
			mappings.mapVariable(var);
		}
		
		for(String listener : this.eventListeners.values()){
			mappings.mapListener(listener);
		}
		
		mappings.mapMain(code);
	}
	
	public JavaScriptMapper getMappedData(){
		return this.mappings;
	}

	public void readCode(List<String> content) {
		int i = 0;
		int j = 0;

		for (String str : content) {
			if (str.indexOf("//") == 0)
				continue;

			if (str.indexOf("addEventListener") > -1 && str.indexOf("function") > -1 ) {
				if(this.inListener == false){
					this.inListener = true;
					this.event = "Event" + i;
					i++;
				}
			} else if (str.indexOf("addEventListener") == -1 && str.indexOf("function") > -1) {
				if(this.inFunction == false){
					this.inFunction = true;
					this.function = "Function" + j;
					j++;
				}
			} else if (str.indexOf("var ") > -1 && this.inFunction == false
					&& this.inListener == false && str.indexOf("{") == -1){
				this.variables.add(str);
			}else{
				if(!this.inListener && !this.inFunction)
					this.code += str + this.newline;
			}

			// register event listeners
			this.addEventListener(str);

			// register functions
			this.addFunction(str);

		}
	}

	/**
	 * Detects and stores event listeners
	 * 
	 * @param str
	 */
	private void addEventListener(String str) {
		if (str.indexOf("}") == -1 && this.inListener == true) {
			if (this.eventListeners.containsKey(this.event)) {
				String body = this.eventListeners.get(this.event);
				body += str + this.newline;
				this.eventListeners.replace(this.event, body);
			} else {
				this.eventListeners.put(this.event, str + this.newline);
			}

			if (str.indexOf("{") > -1) {
				this.brackets++;
			}

		}else if(str.indexOf("{") > -1 && this.inListener == true){
			this.brackets++;
		}
		
		//account for closing brackets in the same row
		if (str.indexOf("}") > -1 && this.inListener == true) {
			this.brackets--;

			String body = this.eventListeners.get(this.event);
			body += str + this.newline;
			this.eventListeners.replace(this.event, body);

			if (this.brackets == 0)
				this.inListener = false;
		}
	}

	/**
	 * Detects and stores functions
	 * 
	 * @param str
	 */
	private void addFunction(String str) {
		if (str.indexOf("}") == -1 && this.inFunction == true) {
			if (this.functions.containsKey(this.function)) {
				String body = this.functions.get(this.function);
				body += str + this.newline;
				this.functions.replace(this.function, body);
			} else {
				this.functions.put(this.function, str + this.newline);
			}

			if (str.indexOf("{") > -1) {
				this.brackets++;
			}

		}else if(str.indexOf("{") > -1 && this.inFunction == true){
			this.brackets++;
		}
		
		//check for closing brackets on the same line
		if (str.indexOf("}") > -1 && this.inFunction == true) {
			this.brackets--;

			String body = this.functions.get(this.function);
			body += str + this.newline;
			this.functions.replace(this.function, body);

			if (this.brackets == 0)
				this.inFunction = false;
		}
	}

}
