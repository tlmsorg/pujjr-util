package com.pujjr.xml.bean;

import java.util.List;

public class ExcelCfg extends ExcelCellPubAttrCfg{
	private String tranCode;
	private String tranName;
	private String colSize; 
	private String serviceName;
	private ExcelTitleCfg excelTitle;
	private ExcelColumnsCfg excelColumns;
	private ExcelContentCfg excelContent;
	
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getTranName() {
		return tranName;
	}
	public void setTranName(String tranName) {
		this.tranName = tranName;
	}
	public String getColSize() {
		return colSize;
	}
	public void setColSize(String colSize) {
		this.colSize = colSize;
	}
	public ExcelTitleCfg getExcelTitle() {
		return excelTitle;
	}
	public void setExcelTitle(ExcelTitleCfg excelTitle) {
		this.excelTitle = excelTitle;
	}
	public ExcelColumnsCfg getExcelColumns() {
		return excelColumns;
	}
	public void setExcelColumns(ExcelColumnsCfg excelColumns) {
		this.excelColumns = excelColumns;
	}
	public ExcelContentCfg getExcelContent() {
		return excelContent;
	}
	public void setExcelContent(ExcelContentCfg excelContent) {
		this.excelContent = excelContent;
	}
	
}
