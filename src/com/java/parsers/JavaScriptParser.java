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
	
	private JavaScriptMapper mappings;
	
	private boolean inFunction = false;
	private boolean inListener = false;
	
	private String event = "";
	
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
		int i = 0;
		for(String str : content){
			if(str.indexOf("//") == 0) continue;
			
			//register event listeners
			this.addEventListener(str);
			
			if(str.indexOf("addEventListener") > -1 && str.indexOf("function") > -1){
				this.inListener = true;
				this.event = "Event"+i;
				i++;
			}
		}
	}
	
	private void addEventListener(String str){
		//add brackets count
		if(str.indexOf("}") == -1 && this.inListener == true){
			if(this.eventListeners.containsKey(this.event)){
				String body = this.eventListeners.get(this.event);
				body += str + "\n";
				this.eventListeners.replace(this.event, body);
			}else{
				this.eventListeners.put(this.event, str + "\n");
			}
		}else if(str.indexOf("}") > -1 && this.inListener == true){
			this.inListener = false;
		}
	}
	
}
