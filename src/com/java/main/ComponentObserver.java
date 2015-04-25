package com.java.main;

import com.java.interpreter.AlloyInterpreter;
import com.java.interpreter.Interpreter;

public class ComponentObserver {

	private static ComponentObserver instance = null;

	protected ComponentObserver() {
	}

	public static ComponentObserver getInstance() {
		if (instance == null) {
			instance = new ComponentObserver();
		}
		return instance;
	}
	
	public Interpreter loadInterpreter(String path){
		return new AlloyInterpreter(path);
	}

}
