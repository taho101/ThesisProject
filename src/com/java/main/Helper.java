package com.java.main;

public class Helper {
	
	public static String toUpper(String st){
		st = st.toLowerCase();
		return Character.toString(st.charAt(0)).toUpperCase() + st.substring(1);
	}
}
