package com.pujjr.pujjr_util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pujjr.excel.impl.ExcelUtilsImpl;

public class ExcelTest {

	/**
	 * 测试方法：写入excel
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 */
	public void excelWrite(String filePath,String fileName){
		String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
		FileOutputStream fos = null;
		int fontSize = 20;
		try {
			fos = new FileOutputStream(new File(filePath + File.separator + fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if("xls".equals(suffix)){
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("催收记录");
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);
			HSSFCellStyle style = workBook.createCellStyle();
			HSSFFont font = workBook.createFont();
			font.setFontName("微软雅黑");
			font.setFontHeight((short)(fontSize * 20));//实际字体大小=高度/20
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("写入xls文件");
			
			CellRangeAddress cra = new CellRangeAddress(1, 3, 1, 3);
			sheet.addMergedRegion(cra);
			try {
				workBook.write(fos);
				fos.close();
				workBook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet();
			XSSFRow row = sheet.createRow(0);
			XSSFCellStyle cellStyle = workBook.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//			cellStyle.setFillBackgroundColor(new XSSFColor(new Color(208,206,206)));
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(208,206,206)));//前景色
			cellStyle.setFillBackgroundColor(new XSSFColor(new Color(159,50,50)));
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workBook.createFont();
			font.setFontName("微软雅黑");
			font.setBold(true);
//			font.setColor((short)6);
			font.setColor(new XSSFColor(new Color(113,64,80)));
			font.setFontHeight(fontSize);//实际字体大小=fontSize
			cellStyle.setFont(font);
			XSSFCell cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("写入xlsx文件");
			sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 3));
			try {
				workBook.write(fos);
				fos.close();
				workBook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("写入完成");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelUtilsImpl eui = new ExcelUtilsImpl();
//		eui.excelWrite("d:\\", "test.xlsx");
		XSSFWorkbook workBook = null;
		File targetFile = new File("d:\\催收记录.xlsx");
		try {
			workBook = new XSSFWorkbook(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workBook.getSheetAt(0);
		System.out.println(sheet.getRow(1).getCell(0).getStringCellValue());
	}

}
