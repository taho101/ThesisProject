package com.java.interpreter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.java.builders.XMLBuilder;
import com.java.parsers.JavaScriptParser;
import com.java.parsers.JsonParser;
import com.java.parsers.XMLParser;
import com.java.structure.AlloyStructure;

public class AlloyInterpreter implements Interpreter{
	
	private String path;
	
	public AlloyInterpreter(String path){
		this.path = path;
	}
	
	public void buildComponent() throws Exception{
		AlloyStructure alloy = new AlloyStructure(this.path);
		alloy.GetRelevantFiles(path);
		
		Map<String, List<File>> retrieved = alloy.GetFiles();
		Set<String> directories = retrieved.keySet();
		
		for(String key : directories){			
			for(File file : retrieved.get(key)){
				switch(alloy.getExtension(file.getName())){
					case "js":
						this.parseJS(file);
					break;
					
					case "json":
						this.parseJson(file);
					break;
					
					case "tss":
					break;
					
					case "xml":
						this.parseXML(file);
					break;
				}
			}
		}
	}
	
	private void parseXML(File file) throws Exception{
		XMLParser parser = new XMLParser(file);
		
		//System.out.println(parser.parseTableView());
	}
	
	private void parseJS(File file) throws IOException{
		JavaScriptParser parser = new JavaScriptParser();
		
		parser.applyMappings(file);
	}
	
	private void parseJson(File file){
		XMLBuilder xml = new XMLBuilder();
		JsonParser parser = new JsonParser(file);
		
		xml.startComponent(parser.getId());
		xml.addName(parser.getName());
		xml.endComponent();
		
		System.out.println(xml.getXML());
	}
}
