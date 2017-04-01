package com.pujjr.pujjr_util;

import com.pujjr.excel.impl.ExcelUtilsImpl;

public class ExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelUtilsImpl eui = new ExcelUtilsImpl();
		eui.excelWrite("d:\\", "test.xlsx");
	}

}
