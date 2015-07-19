package com.java.mappings;

import java.util.Random;

public class JavaScriptSnippets extends JavaScriptMapper{
	
	private static String newline = System.getProperty("line.separator");

	public static String ApplySnippet(String code) {
		String interpreted = "";

		if(code.indexOf("Titanium.API.info") > -1)
			interpreted = AlertWindow(code);
		else if(code.indexOf("Titanium.UI.createButton") > -1)
			interpreted = Button(code);

		return interpreted;
	}
	
	private static String Button(String code){
		//extract parameters
		String parameters = code.substring(code.indexOf("(") + 1, code.indexOf(");"));
		String variable = code.substring(0, code.indexOf("="));
		
		code = code.replace(parameters, "this.Context").replace("Titanium.UI.createButton", "new Button") + newline +
			   "this.AddView("+ variable +");";
		
		return code;
	}
	
	
	private static String AlertWindow(String code){
		String message = code.substring(code.indexOf("(") + 1, code.length()).replace(");", "");
		
		//generate random integer to distinguish between multiple definitions
		//of variables of the same type
		Random rand = new Random();
		int idx = Math.abs(rand.nextInt());
		
		String alert = "	UIAlertView message"+ idx +" = new UIAlertView (\"Widget\", "+ message +", null, \"Ok\", null);"+ newline +
					   "	message"+ idx +".Show();" + newline;
		
		return alert;
	}
}
