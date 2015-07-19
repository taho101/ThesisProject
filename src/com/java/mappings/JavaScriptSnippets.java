package com.java.mappings;

import java.util.Random;

public class JavaScriptSnippets extends JavaScriptMapper{
	
	private static String newline = System.getProperty("line.separator");

	public static String ApplySnippet(String code) {
		String interpreted = "";

		if(code.indexOf("Titanium.API.info") > -1)
			interpreted = AlertWindow(code);

		return interpreted;
	}
	
	
	private static String AlertWindow(String code){
		String message = code.substring(code.indexOf("(") + 1, code.length()).replace(");", "");
		
		//generate random integer to distinguish between multiple definitions
		//of variables of the same type
		Random rand = new Random();
		int idx = rand.nextInt();
		
		String alert = "	UIAlertView message"+ idx +" = new UIAlertView (\"My Title Text\", "+ message +", null, \"Ok\", null);"+ newline +
					   "	message"+ idx +".Show();" + newline;
		
		return alert;
	}
}
