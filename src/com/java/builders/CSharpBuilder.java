package com.java.builders;

public class CSharpBuilder {
	
	protected StringBuilder file;
	protected String newline = System.getProperty("line.separator");
	
	public CSharpBuilder(String className, String namespace){
		this.file = new StringBuilder();
		this.file.append("using System;"+ this.newline +
                         "using Xamarin.Forms;"+ this.newline + this.newline +
                         "namespace " + namespace + this.newline +
                         "{" + this.newline + 
                         "public class "+ className +" : ContentPage" + this.newline +
                         "{");
	}
	
	public void finishFile(){
		this.file.append(this.newline + "}" + this.newline + "}");
	}
	
	public String getCode(){
		return this.file.toString();
	}
}
