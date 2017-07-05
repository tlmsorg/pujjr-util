package com.pujjr.xml.bean;
/**
 * 节点公共属性类
 * @author tom
 *
 */
public class ExcelCellPubAttrCfg {
	private String fontFamily;
	private String fontSize;
	private String isBold;
	private String horizentalAlign;
	private String verticalAlign;
	private String colSpan;
	private String rowHeight;
	private String foregroundColor;
	
	private String dataType;//excel列数据类型。string：字符串;double：数字;date：日期
	private String dateFormat;//日期格式
	private String scale;//double精度
	
	
	
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getForegroundColor() {
		return foregroundColor;
	}
	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	public String getRowHeight() {
		return rowHeight;
	}
	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public String getFontSize() {
		return fontSize;
	}
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	public String getIsBold() {
		return isBold;
	}
	public void setIsBold(String isBold) {
		this.isBold = isBold;
	}
	public String getHorizentalAlign() {
		return horizentalAlign;
	}
	public void setHorizentalAlign(String horizentalAlign) {
		this.horizentalAlign = horizentalAlign;
	}
	public String getVerticalAlign() {
		return verticalAlign;
	}
	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
	}
	public String getColSpan() {
		return colSpan;
	}
	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}
	
	
	
}
