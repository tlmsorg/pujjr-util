package com.pujjr.excel.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemLoopException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import com.pujjr.ali.oss.IOssService;
import com.pujjr.excel.IExcelUtil;
import com.pujjr.utils.Utils;
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
	private int rowIndex = 0;
	private InputStream fis = null;
	private final int defaultColWidth = 5000;//excel列默认宽度5000
	@Override
	public CellStyle getCellStyle03(Workbook workBook, ExcelCellPubAttrCfg pubAttrCfg, String defaultFontName,
			int defaultFontSize, String tranCode) {
		CellStyle cellStyle = workBook.createCellStyle();
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
		Font font = workBook.createFont();
		font.setFontName(defaultFontName);
//		font.setFontHeight(defaultFontSize);
		font.setFontHeight((short) (defaultFontSize * 20));
		//字体
		if(pubAttrCfg.getFontFamily() != null && !"".equals(pubAttrCfg.getFontFamily()))
			font.setFontName(pubAttrCfg.getFontFamily());
		if(pubAttrCfg.getIsBold() !=null && !"".equals(pubAttrCfg.getIsBold()))
			font.setBold(pubAttrCfg.getIsBold().equals("true") ? true:false);
//		font.setColor(new HSSFColor(new Color(113,64,80)));//字体颜色，后续增加
		if(pubAttrCfg.getFontSize() != null && !"".equals(pubAttrCfg.getFontSize()))
			font.setFontHeight(Short.parseShort(Integer.parseInt(pubAttrCfg.getFontSize())  * 20 + ""));//(pujjr-excel.xml中配置的字号 * 20)/20=实际生成的03版excel文件中对应cell的字号
		cellStyle.setFont(font);
		String foregroundColor = pubAttrCfg.getForegroundColor();
		
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		//excel配置文件03格式文件前景色
		String[] foreGroundName = new String[]{"ROYAL_BLUE","TEAL","LIME","PALE_BLUE","AQUA"
				,"GREEN","TURQUOISE","DARK_BLUE","CORNFLOWER_BLUE","OLIVE_GREEN"
				,"WHITE","LIGHT_TURQUOISE","LEMON_CHIFFON","LIGHT_GREEN","BLUE"
				,"DARK_RED","CORAL","RED","LIGHT_YELLOW","SKY_BLUE"
				,"BROWN","SEA_GREEN","INDIGO","MAROON","GREY_80_PERCENT"
				,"GREY_25_PERCENT","DARK_GREEN","YELLOW","GOLD","GREY_40_PERCENT"
				,"DARK_TEAL","PINK","ORCHID","LIGHT_BLUE","LIGHT_CORNFLOWER_BLUE"
				,"BLACK","DARK_YELLOW","VIOLET","LAVENDER","ROSE"
				,"BLUE_GREY","LIGHT_ORANGE","ORANGE","GREY_50_PERCENT"};
		//03格式文件前景色名对应值
		Short[] foreGroundValue = new Short[]{HSSFColor.ROYAL_BLUE.index,HSSFColor.TEAL.index,HSSFColor.LIME.index,HSSFColor.PALE_BLUE.index,HSSFColor.AQUA.index
				,HSSFColor.GREEN.index,HSSFColor.TURQUOISE.index,HSSFColor.DARK_BLUE.index,HSSFColor.CORNFLOWER_BLUE.index,HSSFColor.OLIVE_GREEN.index
				,HSSFColor.WHITE.index,HSSFColor.LIGHT_TURQUOISE.index,HSSFColor.LEMON_CHIFFON.index,HSSFColor.LIGHT_GREEN.index,HSSFColor.BLUE.index
				,HSSFColor.DARK_RED.index,HSSFColor.CORAL.index,HSSFColor.RED.index,HSSFColor.LIGHT_YELLOW.index,HSSFColor.SKY_BLUE.index
				,HSSFColor.BROWN.index,HSSFColor.SEA_GREEN.index,HSSFColor.INDIGO.index,HSSFColor.MAROON.index,HSSFColor.GREY_80_PERCENT.index
				,HSSFColor.GREY_25_PERCENT.index,HSSFColor.DARK_GREEN.index,HSSFColor.YELLOW.index,HSSFColor.GOLD.index,HSSFColor.GREY_40_PERCENT.index
				,HSSFColor.DARK_TEAL.index,HSSFColor.PINK.index,HSSFColor.ORCHID.index,HSSFColor.LIGHT_BLUE.index,HSSFColor.LIGHT_CORNFLOWER_BLUE.index
				,HSSFColor.BLACK.index,HSSFColor.DARK_YELLOW.index,HSSFColor.VIOLET.index,HSSFColor.LAVENDER.index,HSSFColor.ROSE.index
				,HSSFColor.BLUE_GREY.index,HSSFColor.LIGHT_ORANGE.index,HSSFColor.ORANGE.index,HSSFColor.GREY_50_PERCENT.index};
//		cellStyle.setFillBackgroundColor(new HSSFColor(new Color(100, 100, 100)));
		
		//设置前景色
		String foreGroundColor = pubAttrCfg.getForegroundColor() == null ? "" : pubAttrCfg.getForegroundColor();
		for (int i = 0; i < foreGroundName.length; i++) {
			if(foreGroundColor.equals(foreGroundName[i])){
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setFillForegroundColor(foreGroundValue[i]);
				break;
			}
		}
				
		if(pubAttrCfg.getShowPattern() != null	){
			cellStyle.setDataFormat(workBook.createDataFormat().getFormat(pubAttrCfg.getShowPattern()));//数据格式化
		}else{
			
		}
		return cellStyle;
	}
	
	@Override
	public XSSFCellStyle getCellStyle07(XSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode) {
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

		if(pubAttrCfg.getShowPattern() != null	)
			cellStyle.setDataFormat(workBook.createDataFormat().getFormat(pubAttrCfg.getShowPattern()));//数据格式化
		return cellStyle;
	}
	
	@Override
	public void writeLargeFile(String fileFullName,int pageNow,int pageSize,int pageTotal,String defaultFontName,
			int defaultFontSize,String tranCode,List<HashMap<String, Object>> dataList,ExcelCfg excelCfg){
		ExcelTitleCfg titleCfg = excelCfg.getExcelTitle();
		ExcelContentCfg contentCfg = excelCfg.getExcelContent();
		ExcelColumnsCfg colsCfg = excelCfg.getExcelColumns();
		List<ExcelColumnCfg> colCfgList = colsCfg.getExcelColList();
		ExcelConditionsCfg conditionsCfg = excelCfg.getConditionsCfg();
		List<ExcelConditionCfg> conditionList = new ArrayList<ExcelConditionCfg>();
		if(conditionsCfg != null){
			conditionList = conditionsCfg.getConditionList();
		}
		//海量数据批量插入
		SXSSFWorkbook workBook = null;
		File targetFile = new File(fileFullName);
		try {
//			workBook = new SXSSFWorkbook(new FileInputStream(fileFullName));
//			XSSFWorkbook workBook1 = new XSSFWorkbook(new FileInputStream(fileFullName));
//			workBook = new SXSSFWorkbook(100);
			workBook = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(fileFullName)), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//变量定义区
		SXSSFRow contentRow = null;
		HashMap<String, Object> rowMap = null;
		ExcelColumnCfg colCfg = null;
		Iterator<String> keyIt = null;
		String key = null;
		String colId = null;
		SXSSFCell contentTempCell = null;
		String cellValue = null;
		int rowNum = dataList.size();//行数
		/**
		 * cellStyle 不能在创建cell时动态生成，excel对cellStyle上限存在限制，上限：65000左右
		 */
		//数据格式化器
		DataFormat dataFormate = workBook.createDataFormat();
		SXSSFSheet sheet = workBook.getSheetAt(0);
		CellStyle[] cellStyles = new CellStyle[colCfgList.size()];//列cellstyle数组
		if(pageNow < pageTotal){//非最后页
			for (int i = (pageNow - 1) * pageSize; i <(pageNow -1 + 1) * pageSize ; i++) {
				rowMap = dataList.get(i);
				//设置列cellstyle
				int index = 0;
				if(i == (pageNow-1) * pageSize){
					for (int j = 0; j < colCfgList.size() ; j++) {
						colCfg = colCfgList.get(j);
						//当前字段所用cellStyle(写入大量数据，正文采用03样式，设置前景色)
						ExcelCellPubAttrCfg pubAttrCfg = new ExcelCellPubAttrCfg();//以colCfg列样式为主，当列样式无相关样式时，取正文中相关样式。
						Utils.copyPropertiesDeep(contentCfg, pubAttrCfg);//1-------采用正文样式
						Utils.copyPropertiesDeep(colCfg, pubAttrCfg);//2------采用列独立样式
//						CellStyle currCellStyle = this.getCellStyleOfLargeFile(workBook,colCfg,contentCfg,defaultFontName,defaultFontSize,tranCode,"");
						CellStyle currCellStyle = this.getCellStyle03(workBook,pubAttrCfg,defaultFontName,defaultFontSize,tranCode);
						//设置列宽
						sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
						if(colCfg.getShowPattern() != null)
							currCellStyle.setDataFormat(dataFormate.getFormat(colCfg.getShowPattern()));
						cellStyles[index++] = currCellStyle;
					}
				}
				
				//写入数据
				contentRow = sheet.createRow(i+rowIndex);
				contentRow.setHeight(Short.parseShort(contentCfg.getRowHeight()));
				for (int j = 0; j < colCfgList.size() ; j++) {
					colCfg = colCfgList.get(j);
					//设置列宽
					sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
					colId = colCfg.getId();
					keyIt = rowMap.keySet().iterator();
					while(keyIt.hasNext()){
						key = keyIt.next();
						if(key.equals(colId)){
							contentTempCell = contentRow.createCell(j);
							cellValue = rowMap.get(key)+"";
							this.writeCell(cellValue, colCfg, contentTempCell, cellStyles[j], workBook);
						}
					}
				}
			}
		}else if (pageNow == pageTotal){//最后一页
			for (int i = (pageNow-1) * pageSize; i < rowNum; i++) {
				rowMap = dataList.get(i);
				//设置列cellstyle
				int index = 0;
				if(i == (pageNow-1) * pageSize){
					for (int j = 0; j < colCfgList.size() ; j++) {
						colCfg = colCfgList.get(j);
						//当前字段所用cellStyle(写入大量数据，正文采用03样式，设置前景色)
						ExcelCellPubAttrCfg pubAttrCfg = new ExcelCellPubAttrCfg();//以colCfg列样式为主，当列样式无相关样式时，取正文中相关样式。
						Utils.copyPropertiesDeep(contentCfg, pubAttrCfg);
						Utils.copyPropertiesDeep(colCfg, pubAttrCfg);
//						CellStyle currCellStyle = this.getCellStyleOfLargeFile(workBook,colCfg,contentCfg,defaultFontName,defaultFontSize,tranCode,"");
						CellStyle currCellStyle = this.getCellStyle03(workBook,pubAttrCfg,defaultFontName,defaultFontSize,tranCode);
						//设置列宽
						sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
						if(colCfg.getShowPattern() != null)
							currCellStyle.setDataFormat(dataFormate.getFormat(colCfg.getShowPattern()));
						cellStyles[index++] = currCellStyle;
					}
				}
				//写入数据
				contentRow = sheet.createRow(i+rowIndex);
				contentRow.setHeight(Short.parseShort(contentCfg.getRowHeight()));
				for (int j = 0; j < colCfgList.size() ; j++) {
					colCfg = colCfgList.get(j);
					//设置列宽
					sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
					colId = colCfg.getId();
					keyIt = rowMap.keySet().iterator();
					while(keyIt.hasNext()){
						key = keyIt.next();
						if(key.equals(colId)){
							contentTempCell = contentRow.createCell(j);
							cellValue = rowMap.get(key)+"";
							this.writeCell(cellValue, colCfg, contentTempCell, cellStyles[j], workBook);
						}
					}
				}
			}
		}
		
		//文件写出
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileFullName);
			workBook.write(fos);
			fos.close();
			workBook.close();
			System.gc();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/*
		//递归查询下一页
		if(pageNow < pageTotal){
			this.writeFile(fileFullName,pageNow+1, pageSize,pageTotal, defaultFontName, defaultFontSize, tranCode, dataList);
		}
		*/
//		return targetFile;
//		System.gc();
	}
	
	@Override
	public File generalExcel(Map<String,Object> pool,InputStream fis) {
		this.fis = fis;
		rowIndex = 0;
		List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) pool.get("dataList");
		String tranCode = (String) pool.get("tranCode");
		
		ExcelCfg excelCfg = XmlUtil.getExcelCfg(tranCode,fis);
		ExcelTitleCfg titleCfg = excelCfg.getExcelTitle();
		ExcelContentCfg contentCfg = excelCfg.getExcelContent();
		ExcelColumnsCfg colsCfg = excelCfg.getExcelColumns();
		List<ExcelColumnCfg> colCfgList = colsCfg.getExcelColList();
		ExcelConditionsCfg conditionsCfg = excelCfg.getConditionsCfg();
		List<ExcelConditionCfg> conditionList = new ArrayList<ExcelConditionCfg>();
		if(conditionsCfg != null){
			conditionList = conditionsCfg.getConditionList();
		}
		if(excelCfg.getColSize() == null){
			excelCfg.setColSize(colCfgList.size() + "");
		}
		
		if(titleCfg != null){
			if(titleCfg.getColSpan() == null){
				titleCfg.setColSpan(colCfgList.size() + "");
			}
		}
		//指定目录生成文件
		String fileName = (String) pool.get("fileName");
		String suffix = (String) pool.get("suffix");
		FileOutputStream fos = null;
		File targetFile = null;//生成最终文件
		
		String fileFullName = fileName+""+Utils.get16UUID()+suffix;
		try {
			fos = new FileOutputStream(fileFullName);
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
		if(titleCfg != null){
			XSSFRow titleRow = sheet.createRow(rowIndex++);
			titleRow.setHeight(Short.parseShort(titleCfg.getRowHeight()));
			XSSFCellStyle titleCellStyle = this.getCellStyle07(workBook, titleCfg,defaultFontName,defaultFontSize,tranCode);
//			CellStyle titleCellStyle = this.getCellStyleOfLargeFile(workBook, titleCfg, defaultFontName, defaultFontSize, tranCode, "", "");
			for (int i = 0; i < colNum; i++) {
				XSSFCell cellTitle = titleRow.createCell(i);
				cellTitle.setCellStyle(titleCellStyle);
				if(i == 0)
					cellTitle.setCellValue(titleCfg.getTitleName());
			}
			//标题行合并
			CellRangeAddress titleMergeRegion = new CellRangeAddress(0, 0, 0, colNum - 1);
			sheet.addMergedRegion(titleMergeRegion);
		}
		
		
		//格式化器
		DataFormat dataFormat = workBook.createDataFormat();
		if(conditionsCfg != null){
			XSSFRow conditionRow = sheet.createRow(rowIndex++);
			conditionRow.setHeight(Short.parseShort(conditionsCfg.getRowHeight()));
			XSSFCellStyle contitionsStyle = this.getCellStyle07(workBook, conditionsCfg, defaultFontName, defaultFontSize,tranCode);
			for (int i = 0; i < conditionList.size(); i++) {
				ExcelConditionCfg conditionCfg = conditionList.get(i);
				XSSFCell conditionNameCell = conditionRow.createCell(i * 2);
				XSSFCell conditionValueCell = conditionRow.createCell(i * 2+1);
				//当前cell样式
				CellStyle currCellStyle = this.getCellStyle07(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode);
				if(conditionCfg.getShowPattern() != null)
					currCellStyle.setDataFormat(dataFormat.getFormat(conditionCfg.getShowPattern()));
				conditionNameCell.setCellStyle(contitionsStyle);
				conditionNameCell.setCellValue(conditionCfg.getName());
				this.writeCell(pool.get(conditionCfg.getId()) + "", conditionCfg, conditionValueCell, currCellStyle, workBook);
			}
		}
		
		//表格列名行
		if(!"false".equals(colsCfg.getIsShowColName())){
			XSSFRow colNameRow = sheet.createRow(rowIndex++);
//			colNameRow.setHeight((short) 1000);
			colNameRow.setHeight(Short.parseShort(colsCfg.getRowHeight()));
			for (int i = 0; i < colCfgList.size() ; i++) {
				ExcelColumnCfg colCfg = colCfgList.get(i);
				ExcelCellPubAttrCfg pubAttrCfg = new ExcelCellPubAttrCfg();//以colsCfg样式为主，当colsCfg无相关样式属性时，取colCfg对应样式属性。
				Utils.copyPropertiesDeep(colCfg, pubAttrCfg);//1-----取得列独立列样式
				Utils.copyPropertiesDeep(colsCfg, pubAttrCfg);//2-----取得列公共样式
				XSSFCellStyle colCellStyle = this.getCellStyle07(workBook, pubAttrCfg,defaultFontName,defaultFontSize,tranCode);
				XSSFCell colNameCell = colNameRow.createCell(i);
				colNameCell.setCellStyle(colCellStyle);
				colNameCell.setCellValue(colCfg.getName());
				//设置列宽
				sheet.setColumnWidth(i, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
			}
		}
		
		//表格正文行
		int pageSize = rowNum;//页大小，每页记录条数
		int pageTotal = 0;//总页数
		if(pageSize != 0){
			if(rowNum % pageSize == 0)
				pageTotal = rowNum/pageSize;
			else
				pageTotal = rowNum/pageSize +1;
		}
		try {
			workBook.write(fos);
			fos.close();
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int j = 1; j <= pageTotal; j++) {
			logger.info("************j="+j+"***********pageTotal="+pageTotal+"********pageSize:"+pageSize+"****rowNum:"+rowNum+"******************************");
			this.writeLargeFile(fileFullName,j, pageSize,pageTotal, defaultFontName, defaultFontSize, tranCode, dataList,excelCfg);
			//回收Old Gen
			System.gc();
		}
//		System.gc();
		return new File(fileFullName);
	}
	
	@Override
	public File generalExcel2003(Map<String, Object> pool,InputStream fis) {
		this.fis = fis;
		rowIndex = 0;
		List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) pool.get("dataList");
		String tranCode = (String) pool.get("tranCode");
		int defaultColWidth = 4000;
		
		ExcelCfg excelCfg = XmlUtil.getExcelCfg(tranCode,fis);
		ExcelTitleCfg titleCfg = excelCfg.getExcelTitle();
		ExcelContentCfg contentCfg = excelCfg.getExcelContent();
		ExcelColumnsCfg colsCfg = excelCfg.getExcelColumns();
		List<ExcelColumnCfg> colCfgList = colsCfg.getExcelColList();
		ExcelConditionsCfg conditionsCfg = excelCfg.getConditionsCfg();
		List<ExcelConditionCfg> conditionList = new ArrayList<ExcelConditionCfg>();
		if(conditionsCfg != null){
			conditionList = conditionsCfg.getConditionList();
		}
		if(excelCfg.getColSize() == null){
			excelCfg.setColSize(colCfgList.size() + "");
		}
		if(titleCfg != null){
			if(titleCfg.getColSpan() == null){
				titleCfg.setColSpan(colCfgList.size() + "");
			}
		}

		String fileName = (String) pool.get("fileName");
		String suffix = (String) pool.get("suffix");
		FileOutputStream fos = null;
		File targetFile = null;//生成最终文件
		try {
			targetFile = File.createTempFile(fileName, suffix);
			fos = new FileOutputStream(targetFile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int rowNum = dataList.size();//行数
		int colNum = colCfgList.size();//列数
		if(excelCfg.getColSize()  == null)
			colNum = Integer.parseInt(excelCfg.getColSize());
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		//格式化器
		DataFormat dataFormat = workBook.createDataFormat();
		//默认字体
		String defaultFontName = excelCfg.getFontFamily();
		int defaultFontSize = Integer.parseInt(excelCfg.getFontSize());
		
		//标题行
		if(titleCfg != null){
			HSSFRow titleRow = sheet.createRow(rowIndex++);
			titleRow.setHeight(Short.parseShort(titleCfg.getRowHeight()));
			CellStyle titleCellStyle = this.getCellStyle03(workBook, titleCfg,defaultFontName,defaultFontSize,tranCode);
			for (int i = 0; i < colNum; i++) {
				HSSFCell cellTitle = titleRow.createCell(i);
				cellTitle.setCellStyle(titleCellStyle);
				if(i == 0)
					cellTitle.setCellValue(titleCfg.getTitleName());
			}
			//标题行合并
			CellRangeAddress titleMergeRegion = new CellRangeAddress(0, 0, 0, colNum - 1);
			sheet.addMergedRegion(titleMergeRegion);
		}
		
		if(conditionsCfg != null){
			//查询条件行
			HSSFRow conditionRow = sheet.createRow(rowIndex++);
			conditionRow.setHeight(Short.parseShort(conditionsCfg.getRowHeight()));
			CellStyle contitionsStyle = this.getCellStyle03(workBook, conditionsCfg, defaultFontName, defaultFontSize,tranCode);
			for (int i = 0; i < conditionList.size(); i++) {
				ExcelConditionCfg conditionCfg = conditionList.get(i);
				CellStyle conditionStyle = this.getCellStyle03(workBook,conditionCfg,defaultFontName,defaultFontSize,tranCode);
				HSSFCell conditionNameCell = conditionRow.createCell(i * 2);
				HSSFCell conditionValueCell = conditionRow.createCell(i * 2+1);
				this.writeCell(conditionCfg.getName(), conditionsCfg, conditionNameCell, contitionsStyle, workBook);
				this.writeCell(pool.get(conditionCfg.getId()) + "", conditionCfg, conditionValueCell, conditionStyle, workBook);
			}
		}
		
		//表格列名行
		if(!"false".equals(colsCfg.getIsShowColName())){
			HSSFRow colNameRow = sheet.createRow(rowIndex++);
//			colNameRow.setHeight((short) 1000);
			colNameRow.setHeight(Short.parseShort(colsCfg.getRowHeight()));
			
			for (int i = 0; i < colCfgList.size() ; i++) {
				ExcelColumnCfg colCfg = colCfgList.get(i);
				ExcelCellPubAttrCfg pubAttrCfg = new ExcelCellPubAttrCfg();
				Utils.copyPropertiesDeep(colCfg, pubAttrCfg);
				Utils.copyPropertiesDeep(colsCfg, pubAttrCfg);
				CellStyle colCellStyle = this.getCellStyle03(workBook, pubAttrCfg,defaultFontName,defaultFontSize,tranCode);
				
				HSSFCell colNameCell = colNameRow.createCell(i);
				colNameCell.setCellStyle(colCellStyle);
				colNameCell.setCellValue(colCfg.getName());
//				sheet.autoSizeColumn(i);
				//设置列宽
				sheet.setColumnWidth(i, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
			}
		}
		
		//表格正文行
		CellStyle contentCellStyle = this.getCellStyle03(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode);
		//列样式数组
		CellStyle[] cellStyles = new CellStyle[colCfgList.size()];
		for (int i = 0; i <rowNum; i++) {
			HashMap<String, Object> rowMap = dataList.get(i);
			HSSFRow contentRow = sheet.createRow(i+rowIndex);
			contentRow.setHeight(Short.parseShort(contentCfg.getRowHeight()));
			//初始化各列样式
			if(i == 0){
				for (int j = 0; j < colCfgList.size() ; j++) {
					ExcelColumnCfg colCfg = colCfgList.get(j);
					ExcelCellPubAttrCfg pubAttrCfg = new ExcelCellPubAttrCfg();//以colCfg列样式为主，当列样式无相关样式时，取正文中相关样式。
					Utils.copyPropertiesDeep(contentCfg, pubAttrCfg);//1-------采用正文样式
					Utils.copyPropertiesDeep(colCfg, pubAttrCfg);//2------采用列独立样式
					CellStyle colStyle = this.getCellStyle03(workBook, contentCfg, defaultFontName, defaultFontSize, tranCode);
					if(colCfg.getShowPattern() != null){
						//设置显示格式
						colStyle.setDataFormat(dataFormat.getFormat(colCfg.getShowPattern()));
					}
					cellStyles[j] = colStyle;
				}
			}
			for (int j = 0; j < colCfgList.size() ; j++) {
				ExcelColumnCfg colCfg = colCfgList.get(j);
				//设置列宽
				sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
				String colId = colCfg.getId();
				Iterator<String> keyIt = rowMap.keySet().iterator();
				while(keyIt.hasNext()){
					String key = keyIt.next();
					if(key.equals(colId)){
						HSSFCell contentTempCell = contentRow.createCell(j);
						String cellValue = rowMap.get(key)+"";
						contentTempCell.setCellValue(cellValue);
						this.writeCell(cellValue, colCfg, contentTempCell, cellStyles[j], workBook);
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

	/*@Override
	public void writeCell(String cellValue, ExcelCellPubAttrCfg colCfg, XSSFCell contentTempCell,
			CellStyle dateCellStyle, XSSFWorkbook workBook) {
		contentTempCell.setCellStyle(dateCellStyle);
		if(colCfg.getDataType() != null){//指定数据类型
			switch(colCfg.getDataType()){
			case "string":
				contentTempCell.setCellValue(cellValue);
				break;
			case "double":
				if(!"null".equals(cellValue.trim()) && !"".equals(cellValue.trim()) ){
					if(null != colCfg.getScale() && !"null".equals(colCfg.getScale()) && !"".equals(colCfg.getScale())){
						contentTempCell.setCellValue(Utils.formateDouble2Double(Double.parseDouble(cellValue), Integer.parseInt(colCfg.getScale())));
					}else{
						contentTempCell.setCellValue(Double.parseDouble(cellValue));
					}
				}
				break;
			case "date":
				String dateFomate = colCfg.getShowPattern() == null ? "yyyy-MM-dd" : colCfg.getShowPattern();//默认日期格式
				if(!("".equals(cellValue) || null == cellValue || "null".equals(cellValue))){
					contentTempCell.setCellValue(Utils.formateString2Date(cellValue, dateFomate));
				}
				dateCellStyle.setDataFormat(workBook.createDataFormat().getFormat(dateFomate));
				
				break;
			default:
				contentTempCell.setCellValue(cellValue);
				break;
			}
		}else{//未指定数据类型，按文本存储excel cell数据
			contentTempCell.setCellValue(cellValue);
		}
	}*/

	@Override
	public void writeCell(String cellValue, ExcelCellPubAttrCfg colCfg, Cell cell, CellStyle cellStyle,
			Workbook workBook) {
		cell.setCellStyle(cellStyle);
		if(colCfg.getDataType() != null){//指定数据类型
			switch(colCfg.getDataType()){
			case "string":
				cell.setCellValue(cellValue);
				break;
			case "double":
				if(!"null".equals(cellValue.trim()) && !"".equals(cellValue.trim()) ){
					if(null != colCfg.getScale() && !"null".equals(colCfg.getScale()) && !"".equals(colCfg.getScale())){
						cell.setCellValue(Utils.formateDouble2Double(Double.parseDouble(cellValue), Integer.parseInt(colCfg.getScale())));
					}else{
						cell.setCellValue(Double.parseDouble(cellValue));
					}
				}
				break;
			case "date":
				String dateFomate = colCfg.getShowPattern() == null ? "yyyy-MM-dd" : colCfg.getShowPattern();//默认日期格式
				if(!("".equals(cellValue) || null == cellValue || "null".equals(cellValue))){
					cell.setCellValue(Utils.formateString2Date(cellValue, dateFomate));
				}
				cellStyle.setDataFormat(workBook.createDataFormat().getFormat(dateFomate));
				break;
			default:
				cell.setCellValue(cellValue);
				break;
			}
		}else{//未指定数据类型，按文本存储excel cell数据
			cell.setCellValue(cellValue);
		}
	}

}
