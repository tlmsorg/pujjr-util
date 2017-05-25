package com.pujjr.excel;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.pujjr.xml.bean.ExcelCellPubAttrCfg;
import com.pujjr.xml.bean.ExcelCfg;
import com.pujjr.xml.bean.ExcelColumnCfg;

@Service
public interface IExcelUtil {
	
	/**
	 * 03版excel cell 样式
	 * @param workBook
	 * @param pubAttrCfg
	 * @param defaultFontName
	 * @param defaultFontSize
	 * @param tranCode
	 * @return
	 */
	public HSSFCellStyle getCellStyle03(HSSFWorkbook workBook,ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode);
	
	/**
	 * 07版excel cell 样式
	 * @param workBook
	 * @param pubAttrCfg
	 * @param defaultFontName
	 * @param defaultFontSize
	 * @param tranCode
	 * @return
	 */
	public XSSFCellStyle getCellStyle07(XSSFWorkbook workBook,ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode);
	
	/**
	 * 海量数据excel cell样式
	 * @param workBook
	 * @param pubAttrCfg
	 * @param defaultFontName
	 * @param defaultFontSize
	 * @param tranCode
	 * @return
	 */
	public CellStyle getCellStyleOfLargeFile(SXSSFWorkbook workBook, ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode,String dataType,String dateFormat);
	
	/**
	 * 海量数据写入
	 * @param fileFullName 临时文件名
	 * @param pageNow 当前页
	 * @param pageSize 页码大小
	 * @param pageTotal 总页数
	 * @param defaultFontName 默认字体
	 * @param defaultFontSize 默认字号
	 * @param tranCode 交易码
	 * @param dataList 数据源
	 */
	public void writeLargeFile(String fileFullName,int pageNow,int pageSize,int pageTotal,String defaultFontName,
			int defaultFontSize,String tranCode,List<HashMap<String, Object>> dataList,ExcelCfg excelCfg);
	/**
	 * 写入excel
	 * @param cellValue
	 * @param colCfg
	 * @param contentTempCell
	 * @param dateCellStyle
	 * @param workBook
	 */
	public void writeCell(String cellValue,ExcelColumnCfg colCfg,SXSSFCell contentTempCell,CellStyle dateCellStyle,SXSSFWorkbook workBook);
	/**
	 * 生成07版excel
	 * @param pool
	 * @return
	 */
	public File generalExcel(Map<String,Object> pool,InputStream fis);
	/**
	 * 生成03版excel
	 * @param pool
	 * @return
	 */
	public File generalExcel2003(Map<String,Object> pool,InputStream fis);
	
//	public void excelWrite(String filePath,String fileName);
}
