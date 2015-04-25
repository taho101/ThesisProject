package com.java.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.java.mappings.JavaScriptMapper;

public class JavaScriptParser {
	
	private List<String> data;
	private JavaScriptMapper mappings;
	
	public JavaScriptParser(){
		this.data = new ArrayList<String>();
		this.mappings = new JavaScriptMapper();
	}
	
	public void applyMappings(File file) throws IOException{
		this.data = Files.readAllLines(file.toPath());
		
		Set<String> keys = this.mappings.getKeys();

		for(String str : this.data){
		    String tmp = str;
		    
		    for(String key : keys){
		        String replacement = str.replace(key, this.mappings.getMapping(key));
		        if(!str.equals(replacement)) 
		            tmp = replacement;
		    }
		    
		    System.out.println(tmp);
		}
	}
}
