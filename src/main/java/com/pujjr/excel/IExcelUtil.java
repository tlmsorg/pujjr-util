package com.pujjr.excel;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
	public CellStyle getCellStyle03(Workbook workBook,ExcelCellPubAttrCfg pubAttrCfg,String defaultFontName,int defaultFontSize,String tranCode);
	
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
	 * 大文件写入(大量数据)
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
	 * 写入03excel(HSSFWorkbook)
	 * @param cellValue
	 * @param colCfg
	 * @param contentTempCell
	 * @param cellStyle
	 * @param workBook
	 */
//	public void writeCell(String cellValue,ExcelCellPubAttrCfg colCfg,HSSFCell contentTempCell,CellStyle cellStyle,HSSFWorkbook workBook);
	
	/**
	 * 写入07excel，普通数据写入(XSSFWorkbook)
	 * @param cellValue 待写入单元格值
	 * @param colCfg 配置对象
	 * @param contentTempCell 待写入值单元格
	 * @param cellStyle cell样式
	 * @param workBook 工作薄对象
	 */
//	public void writeCell(String cellValue,ExcelCellPubAttrCfg colCfg,XSSFCell contentTempCell,CellStyle cellStyle,XSSFWorkbook workBook);
	
	/**
	 * 写入07excel,海量数据写入(SXSSFWorkbook)
	 * @param cellValue 待写入单元格值
	 * @param colCfg 配置对象
	 * @param contentTempCell 待写入值单元格
	 * @param cellStyle cell样式
	 * @param workBook 工作薄对象
	 */
//	public void writeCell(String cellValue,ExcelCellPubAttrCfg colCfg,SXSSFCell cell,CellStyle cellStyle,SXSSFWorkbook workBook);
	/**
	 * 写入excel
	 * @param cellValue 待写入单元格值
	 * @param colCfg 配置对象
	 * @param cell 待写入值单元格
	 * @param cellStyle cell样式
	 * @param workBook 工作薄对象
	 */
	public void writeCell(String cellValue,ExcelCellPubAttrCfg colCfg,Cell cell,CellStyle cellStyle,Workbook workBook);
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
	
	
}
