package com.java.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.java.grammar.ECMAScriptBaseListener;
import com.java.grammar.ECMAScriptParser;
import com.java.mappings.JavaScriptMapper;

public class JavaScriptParser extends ECMAScriptBaseListener{
	
	private List<String> data;
	private JavaScriptMapper mappings;
	
	public JavaScriptParser(){
		this.data = new ArrayList<String>();
		this.mappings = new JavaScriptMapper();
	}
	
	//Rewrite this function
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
		}
	}
	
	
	public void enterVariableDeclaration(ECMAScriptParser.VariableDeclarationContext ctx) { 
		//System.out.println(ctx.getText());
	}
	
	public void enterFunctionExpression(ECMAScriptParser.FunctionExpressionContext ctx) { 
		System.out.println(ctx.getParent().getParent().getParent().getText());
	}
	
	public String readCode(List<String> content){
		StringBuilder builder = new StringBuilder();
		
		for(String st : content){
			builder.append(st + "\n");
		}
		
		return builder.toString();
	}
	
}
