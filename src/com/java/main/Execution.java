package com.java.main;

import com.java.interpreter.Interpreter;


public class Execution {
	
	public static void main(String[] args){
		//load path to component
		String path = "tests\\listView";
		
		//instantiate the observer
		ComponentObserver observer = ComponentObserver.getInstance();
		
		//load the appropriate interpreter
		Interpreter interpreter = observer.loadInterpreter(path);
		
		try {
			interpreter.buildComponent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
