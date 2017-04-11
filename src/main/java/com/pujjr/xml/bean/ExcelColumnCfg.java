package com.pujjr.xml.bean;

public class ExcelColumnCfg extends ExcelCellPubAttrCfg{
	private String id;//列编码
	private String colWidth;
	private String name;//列名称
	private String dataType;//excel列数据类型。string：字符串;double：数字;date：日期
	private String dateFormat;//日期格式
	private String scale;//double精度
	private String isSum;//是否列求总和
	private String isAvg;//是否列求平均值
	private String isCount;//是否求当前列总记录行数
	
	
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
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
