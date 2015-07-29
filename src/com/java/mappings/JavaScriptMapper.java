package com.java.mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class JavaScriptMapper extends Mapper {
	
	private List<String> variables;
	private List<String> functions;
	private List<String> eventListeners;
	private String code;

	private String newline = System.getProperty("line.separator");
	
	private StringBuilder snippet;
	private boolean inSnippet = false;

	public JavaScriptMapper() {
		this.mappings = new HashMap<String, String>();
		this.snippets = new HashMap<String, String>();
		
		this.snippet = new StringBuilder();

		this.functions = new ArrayList<String>();
		this.eventListeners = new ArrayList<String>();
		this.variables = new ArrayList<String>();
		
		this.fillMappings();
		this.fillSnippets();
	}
	
	
	//map function definition
	public void mapFunction(String function) {
		String[] info = function.split("\n");
		StringBuilder functionBuilder = new StringBuilder();
		List<String> snippets = new ArrayList<String>();

		functionBuilder.append(this.openFunction(info[0]) + this.newline);
		
		int j = 0;
		for (int i = 1; i < info.length; i++) {
			String mapped = this.applyMappings(info[i]);
			
			if(this.snippets.containsValue(mapped)){
				this.inSnippet = true;
				
				this.prepareSnippet(info[i], snippets);
				
				//assign position in the code
				functionBuilder.append( "<!-- " + j + " -->" + this.newline);
			}else if(this.inSnippet == true){
				this.prepareSnippet(info[i], snippets);
			}else{
				functionBuilder.append(this.applyMappings(info[i]) + this.newline);
			}
		}
		
		//finish the construction
		String done = functionBuilder.toString();
		for(int i = 0; i < snippets.size(); i++){
			String mapped = JavaScriptSnippets.ApplySnippet(snippets.get(i));
			
			done = done.replace("<!-- "+ i +" -->", mapped);
		}

		this.functions.add(done);
	}
	
	
	//map event listener
	public void mapListener(String listener) {
		String[] info = listener.split("\n");
		StringBuilder listenerBuilder = new StringBuilder();
		List<String> snippets = new ArrayList<String>();

		listenerBuilder.append(this.openListener(info[0]) + this.newline);
		
		int j = 0;
		for (int i = 1; i < info.length; i++) {
			String mapped = this.applyMappings(info[i]);
			
			if(this.snippets.containsValue(mapped)){
				this.inSnippet = true;
				
				this.prepareSnippet(info[i], snippets);
				
				//assign position in the code
				listenerBuilder.append( "<!-- " + j + " -->" + this.newline);
				j++;
			}else if(this.inSnippet == true){
				this.prepareSnippet(info[i], snippets);
			}else{
				listenerBuilder.append( mapped + this.newline);
			}
		}
		
		//finish the construction
		String done = listenerBuilder.toString();
		for(int i = 0; i < snippets.size(); i++){
			String mapped = JavaScriptSnippets.ApplySnippet(snippets.get(i));
			
			done = done.replace("<!-- "+ i +" -->", mapped);
		}

		this.eventListeners.add(done);
	}
	

	//map variables
	public void mapVariable(String var) {
		String[] elems = var.split(" ");

		String object = "object ";
		if (elems[0].equals("var")) {
			object += elems[1] + " = ";

			String type = elems[elems.length - 1].replace(";", "");

			switch (type) {
				case "[]":
					object += "new object[0]";
					break;
				
				case "{}":
					object += "new object()";
					break;
				
				default:
					object += type;
					break;
			}
		}
		
		//apply snippet for variable init
		object = JavaScriptSnippets.ApplySnippet(object);
		
		if(!this.variables.contains("private " + object + ";") && !object.equals("object "))
			this.variables.add("private " + object + ";");
	}
	
	
	//map main code
	public void mapMain(String code){
		String[] parts = code.split(this.newline);
		StringBuilder function = new StringBuilder();
		List<String> snippets = new ArrayList<String>();
		
		int j = 0;
		for(String line : parts){
			if(!line.equals(null)){
				String mapped = this.applyMappings(line);
				
				if(this.snippets.containsValue(mapped)){
					this.inSnippet = true;
					
					this.prepareSnippet(line, snippets);
					
					//assign position in the code
					function.append( "<!-- " + j + " -->" + this.newline);
					j++;
				}else if(this.inSnippet == true){
					this.prepareSnippet(line, snippets);
				}else{
					function.append( mapped + this.newline);
				}
			}
				
		}
		
		//finish the construction
		String done = function.toString();
		for(int i = 0; i < snippets.size(); i++){
			String mapped = JavaScriptSnippets.ApplySnippet(snippets.get(i).replace("nullvar", "").replaceFirst("var", ""));
			
			done = done.replace("<!-- "+ i +" -->", mapped);
		}
		
		this.code = done;
	}
	
	

	// returns delegate setup for the event listener
	private String openListener(String begin) {
		String[] items = begin.split(" ");
		String object = "";
		String event = "";

		for (String item : items) {
			if (item.contains("(") && !item.contains("function")) {
				event = item.substring(item.indexOf("(") + 1, item.length())
						.replaceAll("\'", "").replaceAll("\"", "")
						.replace(",", "");
				item = item.substring(0, item.indexOf("("));
			}

			if (item.contains(".")) {
				object = item.replace("$.", "")
						.replace(".addEventListener", "").replaceAll("\\s+","");
				
				if(!this.variables.contains("private object " + object + ";"))
					this.variables.add("private object " + object + ";");
			}
		}

		return object + applyMappings("." + event) + " += delegate {";
	}
	
	
	//Create the initialization of a function
	private String openFunction(String begin) {
		String[] items = begin.split(" ");
		String fName = "";

		if (items[0].equals("var") || items[0].equals("function")) {
			fName = (items[1].indexOf("(") > -1) ? items[1] : items[1]
					.substring(0, items[1].indexOf("(") - 1);
		} else {
			fName = items[0].substring(items[0].indexOf(".") + 1,
					items[0].length());
		}

		List<String> variables = this.addVariables(items);

		// combine variables
		StringBuilder parameters = new StringBuilder();
		for (int i = 0; i < variables.size(); i++) {
			if (i > 0)
				parameters.append(", ");
			parameters.append("object " + variables.get(i));
		}

		return "public void " + fName + "(" + parameters.toString() + "){ "
				+ this.newline;
	}
	

	// Extract variables from string array
	private List<String> addVariables(String[] items) {
		List<String> variables = new ArrayList<String>();

		// form the variables
		boolean varStart = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].contains("(")) {
				varStart = true;

				String possibleVar = items[i].substring(
						items[i].indexOf("(") + 1, items[i].length())
						.replaceAll("/\\*.*\\*/", "");
				if (possibleVar.length() > 0) {
					if (possibleVar.contains(",")) {
						String[] vars = possibleVar.split(",");

						for (int j = 0; j < vars.length; j++)
							variables.add(vars[i]);

					} else {
						variables.add(possibleVar);
					}
				}
			} else if (varStart == true) {
				if (items[i].contains(")")) {
					if (items[i].indexOf(")") > 0) {
						String possibleVar = items[i].substring(0,
								items[i].indexOf(")")).replaceAll("/\\*.*\\*/",
								"");

						if (possibleVar.length() > 0) {
							if (possibleVar.contains(",")) {
								String[] vars = possibleVar.split(",");

								for (int j = 0; j < vars.length; j++)
									variables.add(vars[i]);

							} else {
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
	
	//apply general mappings for the code
	//function is also used to find the beginning of a code snippet
	private String applyMappings(String text) {
		Iterator<Entry<String, String>> it = this.mappings.entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<String, String> pair = it.next();
			
			if(text.indexOf(pair.getKey()) > -1 && this.snippets.containsKey(pair.getKey())){
				text = this.snippets.get(pair.getKey());
			}else{
				text = text.replace(pair.getKey(), pair.getValue());
			}
		}

		return text;
	}
	
	
	//identify snippet content
	private void prepareSnippet(String part, List<String> snippets){
		if(part.indexOf(");") > -1 || part.indexOf("];") > -1){
			this.inSnippet = false;
			
			this.snippet.append(part);
			
			snippets.add(this.snippet.toString());
			this.snippet = new StringBuilder();
		}else{
			this.snippet.append(part);
		}
	}
	

	private void fillMappings() {
		this.mappings.put("OS_ANDROID", "Device.OS == TargetPlatform.Android");
		this.mappings.put("OS_IOS", "Device.OS == TargetPlatform.iOS");
		this.mappings.put("};", "}");
		this.mappings.put(".click", ".Clicked");
		this.mappings.put("'", "\"");
		this.mappings.put("null", "");
		this.mappings.put("$.", "");
		
		//empty lines for snippets
		this.mappings.put("Titanium.API.info", "");
		this.mappings.put("Titanium.UI.createButton", "");
		this.mappings.put("Ti.UI.createListView", "");
		this.mappings.put("Ti.UI.createWindow", "");
		this.mappings.put("{properties:", "");
	}
	
	
	private void fillSnippets(){
		this.snippets.put("Titanium.API.info", "\\AlertView");
		this.snippets.put("Titanium.UI.createButton", "\\Button");
		this.snippets.put("Ti.UI.createListView", "\\ListView");
		this.snippets.put("Ti.UI.createWindow", "\\Window");
		this.snippets.put("{properties:", "\\ListItemCollection");
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
