package com.java.mappings;

import java.util.HashMap;

public class JavaScriptMapper extends Mapper {
	
	public JavaScriptMapper() {
		this.mappings = new HashMap<String, String>();

		this.mappings.put("OS_ANDROID", "Device.OS == TargetPlatform.Android");
		this.mappings.put("OS_IOS", "Device.OS == TargetPlatform.iOS");
	}
	
}
