package com.java.interpreter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.java.builders.XAMLBuilder;
import com.java.builders.XMLBuilder;
import com.java.main.Helper;
import com.java.parsers.JavaScriptParser;
import com.java.parsers.JsonParser;
import com.java.parsers.XMLParser;
import com.java.structure.AlloyStructure;

public class AlloyInterpreter implements Interpreter {

	private String path;

	public AlloyInterpreter(String path) {
		this.path = path;
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
					this.parseJS(file);
					break;

				case "json":
					String data = this.parseJson(file);
					this.createFile(data, "Manifest", "xml");
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

	public void createFile(String content, String filename, String extension) {
		//System.out.println(content);
	}
	
	private void parseXML(File file) throws Exception {
		XMLParser parser = new XMLParser(file);
		XAMLBuilder xaml = new XAMLBuilder();
		
		xaml.startContent(Helper.toUpper(file.getName().replace(".xml", "")));	
		
		Map<String, String> tables = parser.parseTableView();
		if(!tables.isEmpty())
			xaml.buildView(tables);
			
		xaml.endContent();
		
		System.out.println(xaml.getXML());
	}

	private void parseJS(File file) throws IOException {
		JavaScriptParser parser = new JavaScriptParser();

		parser.applyMappings(file);
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
