package com.java.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.java.mappings.JavaScriptMapper;

public class JavaScriptParser{
	
	private List<String> variables;
	private Map<String, String> functions;
	private Map<String,String> eventListeners;
	private String code;
	
	private JavaScriptMapper mappings;
	
	private boolean inFunction = false;
	private boolean inListener = false;
	private int brackets = 0;
	
	private String event = "";
	private String function = "";
	
	public JavaScriptParser(){
		this.variables = new ArrayList<String>();
		this.functions = new HashMap<String, String>();
		this.eventListeners = new HashMap<String, String>();
		
		this.mappings = new JavaScriptMapper();
	}
	
	//Rewrite this function
	public void applyMappings(File file) throws IOException{
		/*this.data = Files.readAllLines(file.toPath());
		
		Set<String> keys = this.mappings.getKeys();

		for(String str : this.data){
		    String tmp = str;
		    
		    for(String key : keys){
		        String replacement = str.replace(key, this.mappings.getMapping(key));
		        if(!str.equals(replacement)) 
		            tmp = replacement;
		    }
		}*/
	}
	
	public void readCode(List<String> content){
		int i =  0; int j = 0;
		
		for(String str : content){
			if(str.indexOf("//") == 0) continue;
			
			if(str.indexOf("addEventListener") > -1 && str.indexOf("function") > -1){
				this.inListener = true;
				this.event = "Event"+i;
				i++;
			}else if(str.indexOf("addEventListener") == -1 && str.indexOf("function") > -1){
				this.inFunction = true;
				this.function = "Function"+j;
				j++;
			}
			
			//register event listeners
			this.addEventListener(str);
			
			//register functions
			this.addFunction(str);
			
			if(str.indexOf("var ") > -1 && this.inFunction == false && this.inListener == false)
				this.variables.add(str);
		}
		
		/*for (String key : this.functions.keySet()) {
			System.out.println(key + " " + this.functions.get(key));
		}*/
	}
	
	/**
	 * Detects and stores event listeners 
	 * 
	 * @param str
	 */
	private void addEventListener(String str){
		if(str.indexOf("}") == -1 && this.inListener == true){
			if(this.eventListeners.containsKey(this.event)){
				String body = this.eventListeners.get(this.event);
				body += str + "\n";
				this.eventListeners.replace(this.event, body);
			}else{
				this.eventListeners.put(this.event, str + "\n");
			}
			
			if(str.indexOf("{") > -1){
				this.brackets++;
			}
			
		}else if(str.indexOf("}") > -1 && this.inListener == true){
			this.brackets--;
			
			String body = this.eventListeners.get(this.event);
			body += str + "\n";
			this.eventListeners.replace(this.event, body);
			
			if(this.brackets == 0) 
				this.inListener = false;
		}
	}
	
	/**
	 * Detects and stores functions
	 * 
	 * @param str
	 */
	private void addFunction(String str){
		System.out.println(this.inListener + " " + this.inFunction + " "+ str);
		
		if(str.indexOf("}") == -1 && this.inFunction == true){
			if(this.functions.containsKey(this.function)){
				String body = this.functions.get(this.function);
				body += str + "\n";
				this.functions.replace(this.function, body);
			}else{
				this.functions.put(this.function, str + "\n");
			}
			
			if(str.indexOf("{") > -1){
				this.brackets++;
			}
			
		}else if(str.indexOf("}") > -1 && this.inFunction == true){
			this.brackets--;
			
			String body = this.functions.get(this.function);
			body += str + "\n";
			this.functions.replace(this.function, body);
			
			if(this.brackets == 0) 
				this.inFunction = false;
		}
	}
	
}
