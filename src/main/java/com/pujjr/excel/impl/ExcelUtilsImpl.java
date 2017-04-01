package com.pujjr.excel.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
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
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.pujjr.ali.oss.IOssService;
import com.pujjr.excel.IExcelUtil;
import com.pujjr.xml.XmlUtil;
import com.pujjr.xml.bean.ExcelCellPubAttrCfg;
import com.pujjr.xml.bean.ExcelCfg;
import com.pujjr.xml.bean.ExcelColumnCfg;
import com.pujjr.xml.bean.ExcelColumnsCfg;
import com.pujjr.xml.bean.ExcelConditionCfg;
import com.pujjr.xml.bean.ExcelConditionsCfg;
import com.pujjr.xml.bean.ExcelContentCfg;
import com.pujjr.xml.bean.ExcelTitleCfg;
@Service
public class ExcelUtilsImpl implements IExcelUtil {
	
	private static final Logger logger = Logger.getLogger(ExcelUtilsImpl.class);
	
	@Value("${excel.temp.directory}")
	private String excelTempDirectory;
	
	@Override
	public XSSFCellStyle getCellStyle(XSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode) {
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		switch (pubAttrCfg.getHorizentalAlign()==null ? "" : pubAttrCfg.getHorizentalAlign()) {
		case "left":
			cellStyle.setAlignment(HorizontalAlignment.LEFT);
			break;
		case "center":
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			break;
		case "right":
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			break;
		default:
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			break;
		}
		
		switch (pubAttrCfg.getVerticalAlign() == null ? "" : pubAttrCfg.getVerticalAlign()) {
		case "top":
			cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
			break;
		case "center":
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			break;
		case "bottom":
			cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
			break;
		default:
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				break;
		}
		XSSFFont font = workBook.createFont();
		font.setFontName(defaultFontName);
		font.setFontHeight(defaultFontSize);
		//字体
		if(pubAttrCfg.getFontFamily() != null && !"".equals(pubAttrCfg.getFontFamily()))
			font.setFontName(pubAttrCfg.getFontFamily());
		if(pubAttrCfg.getIsBold() !=null && !"".equals(pubAttrCfg.getIsBold()))
			font.setBold(pubAttrCfg.getIsBold().equals("true") ? true:false);
//		font.setColor(new XSSFColor(new Color(113,64,80)));//字体颜色，后续增加
		if(pubAttrCfg.getFontSize() != null && !"".equals(pubAttrCfg.getFontSize()))
			font.setFontHeight(Integer.parseInt(pubAttrCfg.getFontSize()));//实际字体大小=fontSize
		cellStyle.setFont(font);
		String foregroundColor = pubAttrCfg.getForegroundColor();
		if(foregroundColor != null && !"".equals(foregroundColor)){
			String[] rgb = foregroundColor.split("\\#");
			if(rgb.length == 3){
				try {
					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					cellStyle.setFillForegroundColor(new XSSFColor(new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()), Integer.parseInt(rgb[2].trim()))));
				} catch (Exception e) {
					logger.error(e);
					logger.error("交易【"+tranCode+"】,获取forgroundColor配置项错误");
				}
			}
		}
		/*cellStyle.setBorderColor(BorderSide.TOP,new XSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.RIGHT,new XSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.BOTTOM,new XSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.LEFT,new XSSFColor(new Color(113, 64, 60)));*/
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFillBackgroundColor(new XSSFColor(new Color(100, 100, 100)));
		return cellStyle;
	}

	@Override
	public File generalExcel(Map<String,Object> pool) {
		List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) pool.get("dataList");
		String tranCode = (String) pool.get("tranCode");
		int defaultColWidth = 4000;
		
		ExcelCfg excelCfg = XmlUtil.getExcelCfg(tranCode);
		ExcelTitleCfg titleCfg = excelCfg.getExcelTitle();
		ExcelContentCfg contentCfg = excelCfg.getExcelContent();
		ExcelColumnsCfg colsCfg = excelCfg.getExcelColumns();
		List<ExcelColumnCfg> colCfgList = colsCfg.getExcelColList();
		ExcelConditionsCfg conditionsCfg = excelCfg.getConditionsCfg();
		List<ExcelConditionCfg> conditionList = conditionsCfg.getConditionList();
		
		
		
		if(excelCfg.getColSize() == null){
			excelCfg.setColSize(colCfgList.size() + "");
		}
		if(titleCfg.getColSpan() == null){
			titleCfg.setColSpan(colCfgList.size() + "");
		}
/*
		String fileName = excelCfg.getTranName()+".xlsx";
		String ossKey = "upload"+File.separator+fileName;
		*/
		String fileName = (String) pool.get("fileName");
//		String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
		String suffix = (String) pool.get("suffix");
		FileOutputStream fos = null;
		File targetFile = null;//生成最终文件
		try {
			/*File director = new File(excelTempDirectory);
			if(!director.exists())
				director.mkdirs();
			targetFile = new File(excelTempDirectory + File.separator + fileName);*/
			targetFile = File.createTempFile(fileName, suffix);
			fos = new FileOutputStream(targetFile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int rowNum = dataList.size();//行数
		int colNum = colCfgList.size();//列数
		if(excelCfg.getColSize()  == null)
			colNum = Integer.parseInt(excelCfg.getColSize());
		
		
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet();
		
		//默认字体
		String defaultFontName = excelCfg.getFontFamily();
		int defaultFontSize = Integer.parseInt(excelCfg.getFontSize());
		
		//标题行
		XSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight(Short.parseShort(titleCfg.getRowHeight()));
		XSSFCellStyle titleCellStyle = this.getCellStyle(workBook, titleCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i < colNum; i++) {
			XSSFCell cellTitle = titleRow.createCell(i);
			cellTitle.setCellStyle(titleCellStyle);
			if(i == 0)
				cellTitle.setCellValue(titleCfg.getTitleName());
		}
		//标题行合并
		CellRangeAddress titleMergeRegion = new CellRangeAddress(0, 0, 0, colNum - 1);
		sheet.addMergedRegion(titleMergeRegion);
		
		//查询条件行
		XSSFRow conditionRow = sheet.createRow(1);
		conditionRow.setHeight(Short.parseShort(conditionsCfg.getRowHeight()));
		XSSFCellStyle contitionsStyle = this.getCellStyle(workBook, conditionsCfg, defaultFontName, defaultFontSize,tranCode);
		for (int i = 0; i < conditionList.size(); i++) {
			ExcelConditionCfg conditionCfg = conditionList.get(i);
			XSSFCellStyle conditionStyle = this.getCellStyle(workBook,conditionCfg,defaultFontName,defaultFontSize,tranCode);
			XSSFCell conditionNameCell = conditionRow.createCell(i * 2);
			XSSFCell conditionValueCell = conditionRow.createCell(i * 2+1);
			conditionNameCell.setCellStyle(contitionsStyle);
			conditionValueCell.setCellStyle(conditionStyle);
			conditionNameCell.setCellValue(conditionCfg.getName());
			conditionValueCell.setCellValue(pool.get(conditionCfg.getId()) + "");
		}
		//表格列名行
		XSSFRow colNameRow = sheet.createRow(2);
//		colNameRow.setHeight((short) 1000);
		colNameRow.setHeight(Short.parseShort(colsCfg.getRowHeight()));
		XSSFCellStyle colCellStyle = this.getCellStyle(workBook, colsCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i < colCfgList.size() ; i++) {
			ExcelColumnCfg colCfg = colCfgList.get(i);
			XSSFCell colNameCell = colNameRow.createCell(i);
			colNameCell.setCellStyle(colCellStyle);
			colNameCell.setCellValue(colCfg.getName());
			//设置列宽
			sheet.setColumnWidth(i, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
		}
		
		//表格正文行
		XSSFCellStyle contentCellStyle = this.getCellStyle(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i <rowNum; i++) {
			HashMap<String, Object> rowMap = dataList.get(i);
			XSSFRow contentRow = sheet.createRow(i+3);
			contentRow.setHeight(Short.parseShort(contentCfg.getRowHeight()));
			for (int j = 0; j < colCfgList.size() ; j++) {
				ExcelColumnCfg colCfg = colCfgList.get(j);
				String colId = colCfg.getId();
				Iterator<String> keyIt = rowMap.keySet().iterator();
				while(keyIt.hasNext()){
					String key = keyIt.next();
					if(key.equals(colId)){
						XSSFCell contentTempCell = contentRow.createCell(j);
						contentTempCell.setCellStyle(contentCellStyle);
//						contentTempCell.setCellStyle(colCellStyle);
						String cellValue = rowMap.get(key)+"";
						contentTempCell.setCellValue(cellValue);
					}
				}
			}
		}
		
		try {
			workBook.write(fos);
			fos.close();
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetFile;
	}
	
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


}
