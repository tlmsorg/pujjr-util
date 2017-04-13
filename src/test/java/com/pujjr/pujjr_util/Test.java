package com.pujjr.pujjr_util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pujjr.utils.TransactionMapData;
import com.pujjr.utils.Utils;
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
	/**
	 * 获取指定日期所在月份第一天
	 * @param currMonth 格式：2012-01或2012-01-05
	 * @return 月第一天，格式：2012-01-01
	 */
	public static String getFirstDayOfMonth(String currMonth){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = "";
		try {
			firstDayOfMonth = sdf2.format(sdf.parse(currMonth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("日期："+currMonth+"，所在月第一天："+firstDayOfMonth);
		return firstDayOfMonth;
	}
	
	/**
	 * 获取指定日期所在月份最后一天
	 * @param currMonth 格式：2012-01或2012-01-05
	 * @return 月最后一天，格式：2012-01-01
	 */
	public static String getLastDayOfMonth(String currMonth){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String lastDayOfMonth = "";
		try {
			calendar.setTime(sdf.parse(currMonth));
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			lastDayOfMonth = sdf2.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("日期："+currMonth+"，所在月最后一天："+lastDayOfMonth);
		return lastDayOfMonth;
	}
	
	public static void main(String[] args) throws ParseException {
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
		
		//月初
		String month = "2016-02";
		String currMonthFirstDay = "2016-02-20";
		String currMonth = "2016-02";
		
		String nextMonthFirstDay = "2016-03-10";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date currFirstDate = sdf.parse(currMonthFirstDay);
		Date nextMonthfirstDate = sdf.parse(nextMonthFirstDay);
//		Date date = Calendar.getInstance().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nextMonthfirstDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		System.out.println(sdf2.format(calendar.getTime()));
		Test test = new Test();
		test.getFirstDayOfMonth("2017-03");
		test.getLastDayOfMonth("2017-03");
	}
}
