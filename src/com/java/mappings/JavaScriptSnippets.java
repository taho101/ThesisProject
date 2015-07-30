package com.java.mappings;

import java.util.Random;

public class JavaScriptSnippets extends JavaScriptMapper{
	
	private static String newline = System.getProperty("line.separator");

	public static String ApplySnippet(String code) {
		String interpreted = code;

		if(code.indexOf("Titanium.API.info") > -1)
			interpreted = AlertWindow(code);
		else if(code.indexOf("Titanium.UI.createButton") > -1)
			interpreted = Button(code);
		else if(code.indexOf("Ti.UI.createListView") > -1)
			interpreted = ListView(code);
		else if(code.indexOf("Ti.UI.createWindow") > -1)
			interpreted = Page(code);
		else if(code.indexOf("{properties:") > -1)
			interpreted = ListItemCollection(code);
		else if(code.indexOf("Ti.UI.createListSection") > -1)
			interpreted = Section(code);


		return interpreted;
	}
	
	private static String Button(String code){
		//extract parameters
		String parameters = code.substring(code.indexOf("(") + 1, code.indexOf(");"));
		String variable = code.substring(0, code.indexOf("="));
		
		//replace parameters with their equivalent
		String interpreted = parameters.replace("title:", "Text =").replace("top:", "VerticalOptions =").replaceAll("width: \\\\*+[0-9]+,", "")
									   .replaceAll("height: \\\\*+[0-9]+", "HorizontalOptions = LayoutOptions.Center")
									   .replaceAll("'", "\"");
		//add newlines
		interpreted = interpreted.replaceAll(",", "," + newline).replace("{", "{" + newline)
								 .replace("}", newline + "}");
		
		code = code.replace(parameters, "this.Context").replace("Titanium.UI.createButton", "new Button").replace(";", "") + newline +
			   interpreted + ";" + newline +
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
	
	private static String ListView(String code){
		return code.replace("object", "ListView").replace("Ti.UI.createListView", "new ListView");
	}
	
	private static String Page(String code){
		String main = code.substring(0, code.indexOf("{"));
		
		
		return "var" + main.replace("Ti.UI.createWindow", "new ContentPage") + ");";
	}
	
	private static String ListItemCollection(String code){
		String data = code.substring(code.indexOf("[") + 1, code.indexOf("]"));
		String[] items = data.split(",");
		
		StringBuilder listItems = new StringBuilder();
		for(String item : items){
			String temp = item.replaceAll("'", "\"");
			
			//extract title
			String title = temp.substring(temp.indexOf("title: \"") + 8, temp.lastIndexOf("\""));
			listItems.append("new ListItemValue (\""+ title +"\")," + newline);
		}
		
		return "var " + code.substring(0, code.indexOf("=")) + " = new ListItemCollection<ListItemValue>() {" + newline +
				listItems.toString() + "};" + newline;
	}
	
	private static String Section(String code){
		String sectionName = code.substring(code.indexOf("{") + 1, code.indexOf("}")).replaceAll("'", "\"");
		String variable = code.substring(0, code.indexOf("Ti.UI.createListSection"));
		
		return "var" + variable + sectionName.replace("headerTitle:", "") + ";" + newline;
	}
}
