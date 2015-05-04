package com.java.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.*;

public class JsonParser {

	private JSONObject parsed;

	public JsonParser(File file) {
		try {
			String json = this.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
			this.parsed = new JSONObject(json);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public String getId() {
		String id = this.parsed.getString("id");
		
		return id.replace(".", "");
	}
	
	public String getAttribute(String name){
		return (this.parsed.has(name)) ? this.parsed.getString(name) : null;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
