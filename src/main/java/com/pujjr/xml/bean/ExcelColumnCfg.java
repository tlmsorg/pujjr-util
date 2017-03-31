package com.pujjr.xml.bean;

public class ExcelColumnCfg extends ExcelCellPubAttrCfg{
	private String id;//列编码
	private String colWidth;
	private String name;//列名称
	private String isSum;//是否列求总和
	private String isAvg;//是否列求平均值
	private String isCount;//是否求当前列总记录行数
	
	public String getColWidth() {
		return colWidth;
	}
	public void setColWidth(String colWidth) {
		this.colWidth = colWidth;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsSum() {
		return isSum;
	}
	public void setIsSum(String isSum) {
		this.isSum = isSum;
	}
	public String getIsAvg() {
		return isAvg;
	}
	public void setIsAvg(String isAvg) {
		this.isAvg = isAvg;
	}
	public String getIsCount() {
		return isCount;
	}
	public void setIsCount(String isCount) {
		this.isCount = isCount;
	}
	
	
	
}
