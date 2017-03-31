package com.pujjr.vo;

import java.io.Serializable;

public class PageVo implements Serializable{
	//当前页码
	private Integer pageNum;
	//每页记录数
	private Integer pageSize;
	//总页数
	private Integer pages;
	//总记录数
	private Long totalItem;
	//当前页数据
	private Object data;
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public Long getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(Long totalItem) {
		this.totalItem = totalItem;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
