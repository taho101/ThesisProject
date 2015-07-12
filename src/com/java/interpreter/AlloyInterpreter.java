package com.java.interpreter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.java.builders.CSharpBuilder;
import com.java.builders.XAMLBuilder;
import com.java.builders.XMLBuilder;
import com.java.main.Helper;
import com.java.mappings.JavaScriptMapper;
import com.java.parsers.JavaScriptParser;
import com.java.parsers.JsonParser;
import com.java.parsers.XMLParser;
import com.java.structure.AlloyStructure;

public class AlloyInterpreter implements Interpreter {

	private String path;
	private String componentPath;

	public AlloyInterpreter(String path) {
		this.path = path;
		
		this.prepareStructure();
	}

	public void buildComponent() throws Exception {
		AlloyStructure alloy = new AlloyStructure(this.path);
		alloy.GetRelevantFiles(path);

		Map<String, List<File>> retrieved = alloy.GetFiles();
		Set<String> directories = retrieved.keySet();
		
		for (String key : directories) {
			for (File file : retrieved.get(key)) {
				switch (alloy.getExtension(file.getName())) {
				case "js":
					String csharp = this.parseJS(file);
					this.createFile(csharp, "Widget", "cs");
				break;

				case "json":
					String xml = this.parseJson(file);
					this.createFile(xml, "Manifest", "xml");
				break;

				case "tss":
					break;

				case "xml":
					String json = this.parseXML(file);
					this.createFile(json, "Widget", "xaml");
				break;
				}
			}
		}
		
		System.out.println("Complete");
	}
	
	public void prepareStructure(){
		File file = new File(System.getProperty("user.home") + "\\Desktop" + "\\Component");
		
		if(!file.exists()) 
			file.mkdir();
		
		this.componentPath = file.getAbsolutePath();
	}

	public void createFile(String content, String filename, String extension) {
		File file = new File(this.componentPath + "\\" + filename + "." + extension);
		
		try {
			if(!file.exists())
				file.createNewFile();
		
			PrintWriter writer = new PrintWriter(this.componentPath + "\\" + filename + "." + extension, "UTF-8");
			writer.println(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String parseXML(File file) throws Exception {
		XMLParser parser = new XMLParser(file);
		XAMLBuilder xaml = new XAMLBuilder();
		
		xaml.startContent(Helper.toUpper(file.getName().replace(".xml", "")));	
		
		Map<String, String> tables = parser.parseTableView();
		if(!tables.isEmpty())
			xaml.buildView(tables);
			
		xaml.endContent();
		
		return xaml.getXML();
	}

	private String parseJS(File file) throws IOException {
		JavaScriptParser jsParser = new JavaScriptParser();
		
		//separate the code parts
		jsParser.readCode(Files.readAllLines(file.toPath()));
		
		//apply map equivalents and get the interpreted code
		jsParser.applyMappings();
		JavaScriptMapper code = jsParser.getMappedData();
		
		CSharpBuilder csharp = new CSharpBuilder(Helper.toUpper(file.getName().replace(".js", "")), "Component");
		
		//add variables
		for(String var : code.getVariables()){
			csharp.addCode(var);
		}
		
		//add class constructor
		csharp.addConstructor(Helper.toUpper(file.getName().replace(".js", "")), code.getCode());
		
		//add functions
		for(String function : code.getFunctions()){
			csharp.addCode(function);
		}
		
		//add event listeners
		for(String eventListener : code.getEventListeners()){
			csharp.addCode(eventListener);
		}
		
		csharp.finishFile();

		
		return csharp.getCode();
	}

	private String parseJson(File file) {
		XMLBuilder xml = new XMLBuilder();
		JsonParser parser = new JsonParser(file);

		xml.startComponent(parser.getId());
		xml.addElement(parser.getAttribute("name"), "name");
		xml.addElement(parser.getAttribute("description"), "summary");
		xml.addElement(parser.getAttribute("author"), "publisher");
		xml.addElement(parser.getAttribute("version"), "version");
		xml.endComponent();

		return xml.getXML();
	}
}
