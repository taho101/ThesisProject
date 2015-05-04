package com.java.interpreter;

public interface Interpreter {
	public void buildComponent() throws Exception;
	public void createFile(String content, String filename, String extension);
}
