package com.java.structure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlloyStructure {
	private Map<String, List<File>> files;
	private String path;

	public AlloyStructure(String path) {
		files = new HashMap<String, List<File>>();
		this.path = path;
	}

	public void GetRelevantFiles(String path) {
		File project = new File(path);
		File[] children = project.listFiles();

		if (children != null) {
			for (File child : children) {
				// set files to the specific directory
				if (child.isFile()) {
					String key = this.getContainer(child);
					this.addElement(key, child);
				}

				// recursively extract all other files
				if (child.isDirectory())
					GetRelevantFiles(child.getAbsolutePath());

			}
		}
	}

	public Map<String, List<File>> GetFiles() {
		return files;
	}

	private String getContainer(File file) {
		return file.getAbsolutePath().replace(this.path + "\\", "")
				.replace("\\" + file.getName(), "");
	}
	
	private void addElement(String key, File file){
		if(this.files.containsKey(key)){
			List<File> files = this.files.get(key);
			files.add(file);
			
			this.files.replace(key, files);
		}else{
			List<File> files = new ArrayList<File>();
			files.add(file);
			
			this.files.put(key, files);
		}
	}

	public String getExtension(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1,
				filename.length());
	}
}
