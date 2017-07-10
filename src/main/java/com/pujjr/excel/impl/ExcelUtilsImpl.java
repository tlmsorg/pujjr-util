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
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
	public HSSFCellStyle getCellStyle03(HSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg, String defaultFontName,
			int defaultFontSize, String tranCode) {
		HSSFCellStyle cellStyle = workBook.createCellStyle();
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
		HSSFFont font = workBook.createFont();
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
		if(foregroundColor != null && !"".equals(foregroundColor)){
			String[] rgb = foregroundColor.split("\\#");
			if(rgb.length == 3){
				try {
//					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//					cellStyle.setFillForegroundColor(new HSSFColor(new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()), Integer.parseInt(rgb[2].trim()))));
//					cellStyle.setFillPattern(fillp);
				} catch (Exception e) {
					logger.error(e);
					logger.error("交易【"+tranCode+"】,获取forgroundColor配置项错误");
				}
			}
		}
		/*cellStyle.setBorderColor(BorderSide.TOP,new HSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.RIGHT,new HSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.BOTTOM,new HSSFColor(new Color(113, 64, 60)));
		cellStyle.setBorderColor(BorderSide.LEFT,new HSSFColor(new Color(113, 64, 60)));*/
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
//		cellStyle.setFillBackgroundColor(new HSSFColor(new Color(100, 100, 100)));
		return cellStyle;
	}
	
	@Override
	public XSSFCellStyle getCellStyle07(XSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode,String dataType,String dateFormat) {
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
		
		//日期数字格式
		if("date".equals(dataType) || "double".equals(dataType)){
			cellStyle.setDataFormat(workBook.createDataFormat().getFormat(dateFormat));//日期\数字格式
		}
		return cellStyle;
	}
	@Override
	public CellStyle getCellStyleOfLargeFile(SXSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode,String dataType,String dateFormat) {
		CellStyle cellStyle = workBook.createCellStyle();
		/**
		 * 说明：CellStyle，不支持setFillForegroundColor中指定红绿蓝三原色，导致无法灵活设置cell前景色 20170705
		 */
//		SXSSFCellStyle cellStyle = workBook.createCellStyle();
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
//		XSSFFont font = workBook.createFont();
		font.setFontName(defaultFontName);
		font.setFontHeight((short) (defaultFontSize * 20));
		//字体
		if(pubAttrCfg.getFontFamily() != null && !"".equals(pubAttrCfg.getFontFamily()))
			font.setFontName(pubAttrCfg.getFontFamily());
		if(pubAttrCfg.getIsBold() !=null && !"".equals(pubAttrCfg.getIsBold()))
			font.setBold(pubAttrCfg.getIsBold().equals("true") ? true:false);
//		font.setColor(new XSSFColor(new Color(113,64,80)));//字体颜色，后续增加
		if(pubAttrCfg.getFontSize() != null && !"".equals(pubAttrCfg.getFontSize()))
			font.setFontHeight((short) (Integer.parseInt(pubAttrCfg.getFontSize()) * 20));//实际字体大小=fontSize
		cellStyle.setFont(font);
		String foregroundColor = pubAttrCfg.getForegroundColor();
		/*if(foregroundColor != null && !"".equals(foregroundColor)){
			String[] rgb = foregroundColor.split("\\#");
			if(rgb.length == 3){
				try {
					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//					cellStyle.setFillForegroundColor(new XSSFColor(new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()), Integer.parseInt(rgb[2].trim()))));
					cellStyle.setFillBackgroundColor((short) 100);
				} catch (Exception e) {
					logger.error(e);
					logger.error("交易【"+tranCode+"】,获取forgroundColor配置项错误");
				}
			}
		}*/
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
//		cellStyle.setFillBackgroundColor(new XSSFColor(new Color(100, 100, 100)));
		cellStyle.setFillBackgroundColor((short) 100);
		
		//日期\数字格式
		if("date".equals(dataType) || "double".equals(dataType)){
			cellStyle.setDataFormat(workBook.createDataFormat().getFormat(dateFormat));//日期数字格式
		}
		return cellStyle;
	}
	
	/**
	 * 写入cell
	 * @param cellValue
	 * @param colCfg
	 * @param contentTempCell
	 * @param dateCellStyle
	 * @param workBook
	 */
	public void writeCell(String cellValue,ExcelCellPubAttrCfg colCfg,SXSSFCell contentTempCell,CellStyle cellStyle,SXSSFWorkbook workBook){
		contentTempCell.setCellStyle(cellStyle);
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
				String dateFomate = "yyyy-MM-dd";//默认日期格式
				if(colCfg.getDateFormat() != null){
					dateFomate = colCfg.getDateFormat();
				}
				if(!("".equals(cellValue) || null == cellValue || "null".equals(cellValue))){
					contentTempCell.setCellValue(Utils.formateString2Date(cellValue, dateFomate));
				}
				cellStyle.setDataFormat(workBook.createDataFormat().getFormat(dateFomate));
				break;
			default:
				contentTempCell.setCellValue(cellValue);
				break;
			}
		}else{//未指定数据类型，按文本存储excel cell数据
			contentTempCell.setCellValue(cellValue);
		}
	}
	@Override
	public void writeLargeFile(String fileFullName,int pageNow,int pageSize,int pageTotal,String defaultFontName,
			int defaultFontSize,String tranCode,List<HashMap<String, Object>> dataList,ExcelCfg excelCfg){
//		ExcelCfg excelCfg = XmlUtil.getExcelCfg(tranCode,fis);
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
		
//		CellStyle contentCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,String dataType);
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
//		int colNum = colCfgList.size();//列数
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
						//普通字符cell类型
						CellStyle contentCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"","");
						//日期cell类型
						CellStyle dateCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"date","yyyy-MM-dd");
						//数字cell类型
						CellStyle numCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"double","###.00");
						//当前字段所用cellStyle
						CellStyle currCellStyle = contentCellStyle;
						colCfg = colCfgList.get(j);
						//设置列宽
						sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
						colId = colCfg.getId();
						keyIt = rowMap.keySet().iterator();
						String dataType = colCfg.getDataType() == null ? "string":colCfg.getDataType();//字段类型
						String dateFormat = colCfg.getDateFormat();
						String showPattern = colCfg.getShowPattern();
						switch(dataType){
						case "string":
							currCellStyle = contentCellStyle;
							break;
						case "double":
							if(showPattern != null)
								numCellStyle.setDataFormat(dataFormate.getFormat(showPattern));
							currCellStyle = numCellStyle;
							break;
						case "date":
							if(dateFormat != null)
								dateCellStyle.setDataFormat(dataFormate.getFormat(dateFormat));
							currCellStyle = dateCellStyle;
							break;
						default:
							currCellStyle = contentCellStyle;
							break;
						}
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
						//普通字符cell类型
						CellStyle contentCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"","");
						//日期cell类型
						CellStyle dateCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"date","yyyy-MM-dd");
						//数字cell类型
						CellStyle numCellStyle = this.getCellStyleOfLargeFile(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"double","###");
						//当前字段所用cellStyle
						CellStyle currCellStyle = contentCellStyle;
						colCfg = colCfgList.get(j);
						//设置列宽
						sheet.setColumnWidth(j, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
						colId = colCfg.getId();
						keyIt = rowMap.keySet().iterator();
						String dataType = colCfg.getDataType() == null ? "string":colCfg.getDataType();//字段类型
						String dateFormat = colCfg.getDateFormat();
						String showPattern = colCfg.getShowPattern();
						switch(dataType){
						case "string":
							currCellStyle = contentCellStyle;
							break;
						case "double":
							if(showPattern != null)
								numCellStyle.setDataFormat(dataFormate.getFormat(showPattern));
							currCellStyle = numCellStyle;
							break;
						case "date":
							if(dateFormat != null)
								dateCellStyle.setDataFormat(dataFormate.getFormat(dateFormat));
							currCellStyle = dateCellStyle;
							break;
						default:
							currCellStyle = contentCellStyle;
							break;
						}
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
			XSSFCellStyle titleCellStyle = this.getCellStyle07(workBook, titleCfg,defaultFontName,defaultFontSize,tranCode,"","");
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
			XSSFCellStyle contitionsStyle = this.getCellStyle07(workBook, conditionsCfg, defaultFontName, defaultFontSize,tranCode,"","");
//			CellStyle contitionsStyle = this.getCellStyleOfLargeFile(workBook, conditionsCfg, defaultFontName, defaultFontSize, tranCode, "", "");
			for (int i = 0; i < conditionList.size(); i++) {
				ExcelConditionCfg conditionCfg = conditionList.get(i);
//				CellStyle conditionStyle = this.getCellStyleOfLargeFile(workBook, conditionCfg, defaultFontName, defaultFontSize, tranCode, "", "");
				XSSFCell conditionNameCell = conditionRow.createCell(i * 2);
				XSSFCell conditionValueCell = conditionRow.createCell(i * 2+1);
				String showPattern = conditionCfg.getShowPattern();
				String dateFormat = conditionCfg.getDateFormat();
				String dataType = conditionCfg.getDataType() == null ? "" : conditionCfg.getDataType();

				//查询条件行
				CellStyle conditionStyle = this.getCellStyle07(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"","");
				//日期cell类型
				CellStyle dateCellStyle = this.getCellStyle07(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"date","yyyy-MM-dd");
				//数字cell类型
				CellStyle numCellStyle = this.getCellStyle07(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode,"double","###.00");
				//当前cell样式
				CellStyle currCellStyle = conditionStyle;
				
				switch(dataType){
				case "string":
					currCellStyle = conditionStyle;
					break;
				case "date":
					if(dateFormat != null)
						dateCellStyle.setDataFormat(dataFormat.getFormat(dateFormat));
					currCellStyle = dateCellStyle;
					break;
				case "double":
					if(showPattern != null)
						numCellStyle.setDataFormat(dataFormat.getFormat(showPattern));
					currCellStyle = numCellStyle;
					break;
				}
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
			XSSFCellStyle colCellStyle = this.getCellStyle07(workBook, colsCfg,defaultFontName,defaultFontSize,tranCode,"","");
//			CellStyle colCellStyle = this.getCellStyleOfLargeFile(workBook, colsCfg, defaultFontName, defaultFontSize, tranCode, "", "");
			for (int i = 0; i < colCfgList.size() ; i++) {
				ExcelColumnCfg colCfg = colCfgList.get(i);
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
		List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) pool.get("dataList");
		String tranCode = (String) pool.get("tranCode");
		int defaultColWidth = 4000;
		
		ExcelCfg excelCfg = XmlUtil.getExcelCfg(tranCode,fis);
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
		
		//默认字体
		String defaultFontName = excelCfg.getFontFamily();
		int defaultFontSize = Integer.parseInt(excelCfg.getFontSize());
		
		//标题行
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight(Short.parseShort(titleCfg.getRowHeight()));
		HSSFCellStyle titleCellStyle = this.getCellStyle03(workBook, titleCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i < colNum; i++) {
			HSSFCell cellTitle = titleRow.createCell(i);
			cellTitle.setCellStyle(titleCellStyle);
			if(i == 0)
				cellTitle.setCellValue(titleCfg.getTitleName());
		}
		//标题行合并
		CellRangeAddress titleMergeRegion = new CellRangeAddress(0, 0, 0, colNum - 1);
		sheet.addMergedRegion(titleMergeRegion);
		
		//查询条件行
		HSSFRow conditionRow = sheet.createRow(1);
		conditionRow.setHeight(Short.parseShort(conditionsCfg.getRowHeight()));
		HSSFCellStyle contitionsStyle = this.getCellStyle03(workBook, conditionsCfg, defaultFontName, defaultFontSize,tranCode);
		for (int i = 0; i < conditionList.size(); i++) {
			ExcelConditionCfg conditionCfg = conditionList.get(i);
			HSSFCellStyle conditionStyle = this.getCellStyle03(workBook,conditionCfg,defaultFontName,defaultFontSize,tranCode);
			HSSFCell conditionNameCell = conditionRow.createCell(i * 2);
			HSSFCell conditionValueCell = conditionRow.createCell(i * 2+1);
			conditionNameCell.setCellStyle(contitionsStyle);
			conditionValueCell.setCellStyle(conditionStyle);
			conditionNameCell.setCellValue(conditionCfg.getName());
			conditionValueCell.setCellValue(pool.get(conditionCfg.getId()) + "");
		}
		//表格列名行
		HSSFRow colNameRow = sheet.createRow(2);
//		colNameRow.setHeight((short) 1000);
		colNameRow.setHeight(Short.parseShort(colsCfg.getRowHeight()));
		HSSFCellStyle colCellStyle = this.getCellStyle03(workBook, colsCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i < colCfgList.size() ; i++) {
			ExcelColumnCfg colCfg = colCfgList.get(i);
			HSSFCell colNameCell = colNameRow.createCell(i);
			colNameCell.setCellStyle(colCellStyle);
			colNameCell.setCellValue(colCfg.getName());
//			sheet.autoSizeColumn(i);
			//设置列宽
			sheet.setColumnWidth(i, Integer.parseInt(colCfg.getColWidth() == null ? defaultColWidth+"":colCfg.getColWidth()));
		}
		
		//表格正文行
		HSSFCellStyle contentCellStyle = this.getCellStyle03(workBook,contentCfg,defaultFontName,defaultFontSize,tranCode);
		for (int i = 0; i <rowNum; i++) {
			HashMap<String, Object> rowMap = dataList.get(i);
			HSSFRow contentRow = sheet.createRow(i+3);
			contentRow.setHeight(Short.parseShort(contentCfg.getRowHeight()));
			for (int j = 0; j < colCfgList.size() ; j++) {
				ExcelColumnCfg colCfg = colCfgList.get(j);
				String colId = colCfg.getId();
				Iterator<String> keyIt = rowMap.keySet().iterator();
				while(keyIt.hasNext()){
					String key = keyIt.next();
					if(key.equals(colId)){
						HSSFCell contentTempCell = contentRow.createCell(j);
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

	@Override
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
				String dateFomate = "yyyy-MM-dd";//默认日期格式
				if(colCfg.getDateFormat() != null){
					dateFomate = colCfg.getDateFormat();
				}
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
	}


}
