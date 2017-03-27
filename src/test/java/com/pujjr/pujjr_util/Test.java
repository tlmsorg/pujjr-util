package com.pujjr.pujjr_util;

public class Test {
	public static String getSetMethodName(String attrName){
		String methodName = "";
		if(attrName.length() > 0)
			methodName = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}

	public static String getGetMethodName(String attrName) {
		String methodName = "";
		if(attrName.length() > 0)
			methodName = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}
	public static void main(String[] args) {
		System.out.println(Test.getGetMethodName("name"));
		System.out.println(Test.getSetMethodName("userName"));
	}
}
