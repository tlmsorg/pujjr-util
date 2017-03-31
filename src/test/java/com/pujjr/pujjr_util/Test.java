package com.pujjr.pujjr_util;

import java.io.File;

import com.pujjr.utils.TransactionMapData;
import com.pujjr.xml.XmlUtil;

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
		
		TransactionMapData tmd1 = TransactionMapData.getInstance();
		tmd1.put("name", "test");
		tmd1.setName("name1");
		TransactionMapData tmd2 = tmd1.clone();
		tmd2.put("name", "test2");
		tmd2.setName("name2");
		
		System.out.println(tmd1);
		System.out.println(tmd2);
		System.out.println(tmd1.get("name")+"|"+tmd1.getName());
		System.out.println(tmd2.get("name")+"|"+tmd2.getName());
		System.out.println(File.separatorChar);
	}
}
