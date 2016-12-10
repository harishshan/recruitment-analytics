package com.winterhack.altimetrik.util;

public class StringUtil {
	public static String removeBracket(String str){
		if(str.startsWith("[")){
			str=str.substring(1);			
		}
		if(str.endsWith("]")){
			str=str.substring(0,str.length()-1);			
		}
		return str;
	}
}
