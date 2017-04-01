package com.pujjr.xml.bean;

import java.util.List;

public class ExcelConditionsCfg extends ExcelCellPubAttrCfg{
	List<ExcelConditionCfg> conditionList;

	public List<ExcelConditionCfg> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<ExcelConditionCfg> conditionList) {
		this.conditionList = conditionList;
	}

}
