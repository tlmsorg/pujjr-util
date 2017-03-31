package com.pujjr.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.pujjr.xml.bean.ExcelCellPubAttrCfg;

@Service
public interface IExcelUtil {
	
	public XSSFCellStyle getCellStyle(XSSFWorkbook workBook,ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize);
	
	public File generalExcel(Map<String,Object> pool);
	
	public void excelWrite(String filePath,String fileName);
}
