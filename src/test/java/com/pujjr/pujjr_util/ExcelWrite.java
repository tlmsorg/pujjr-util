package com.pujjr.pujjr_util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("发的发生的");
		
		try {
			FileOutputStream fos = new FileOutputStream(new File("d:\\excel_write.xls"));
			wb.write(fos);
			fos.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("文件写入完成");
	}

}
