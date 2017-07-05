package com.pujjr.xml.bean;

import java.util.List;

public class ExcelColumnsCfg extends ExcelCellPubAttrCfg{
	private List<ExcelColumnCfg> excelColList;
	private String isShowColName;

	public String getIsShowColName() {
		return isShowColName;
	}

	public void setIsShowColName(String isShowColName) {
		this.isShowColName = isShowColName;
	}

	public void setShowColName(String isShowColName) {
		this.isShowColName = isShowColName;
	}

	public List<ExcelColumnCfg> getExcelColList() {
		return excelColList;
	}

	public void setExcelColList(List<ExcelColumnCfg> excelColList) {
		this.excelColList = excelColList;
	}
	
	
}
