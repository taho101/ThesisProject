package com.java.mappings;

import java.util.Map;
import java.util.Set;

public abstract class Mapper {
	
	protected Map<String, String> mappings;
	protected Map<String, String> snippets;
	
	public String getMapping(String key){
		return this.mappings.get(key);
	}
	
	public Set<String> getKeys(){
		return this.mappings.keySet();
	}
	
}
