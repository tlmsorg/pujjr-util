package com.pujjr.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.pujjr.utils.Utils;
import com.pujjr.xml.bean.ExcelCfg;
import com.pujjr.xml.bean.ExcelColumnCfg;
import com.pujjr.xml.bean.ExcelColumnsCfg;
import com.pujjr.xml.bean.ExcelConditionCfg;
import com.pujjr.xml.bean.ExcelConditionsCfg;
import com.pujjr.xml.bean.ExcelContentCfg;
import com.pujjr.xml.bean.ExcelTitleCfg;

public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class);
	
	/**
	 * 获取节点属性配置信息对象
	 * @param nodeCfg 节点属性配置对象(空对象)
	 * @param attrList 节点属性列表(解析xml配置文件)
	 * @return 节点属性对象(数据已填充)
	 */
	public static Object getNodeCfg(Object nodeCfg,List<Attribute> attrList){
		Class<?> titleCfgCls = nodeCfg.getClass();
//		logger.info("titleCfg.getClass():"+nodeCfg.getClass());
		List<Field> titleCfgFields = Utils.getFieldList(nodeCfg.getClass());
		for (Attribute attr : attrList) {
			String attrName = attr.getName();
			String attrValue = attr.getText();
			for (Field field : titleCfgFields) {
				if(attrName.equals(field.getName())){
					Method method = null;
					try {
						method = titleCfgCls.getMethod(Utils.getSetMethodName(attrName), String.class);
						method.invoke(nodeCfg, attrValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return nodeCfg;
	}
	/**
	 * 获取对应交易码excel配置信息对象
	 * @param tranCode 交易码
	 * @return excel配置信息对象
	 */
	public static ExcelCfg getExcelCfg(String tranCode,InputStream fis){
		ExcelCfg excelCfg = new ExcelCfg();
		Element currElement = null;//当前交易码对应excel节点对象
		String path = "";
		try {
			logger.info("rrr"+XmlUtil.class.getClassLoader().getResource("").toURI().getPath().toString());
			path = XmlUtil.class.getClassLoader().getResource("").toURI().getPath().toString();
			logger.info("pujjr-excel.xml文件目录："+path);
		} catch (URISyntaxException e) {
			logger.error("pujjr-excel.xml文件目录："+path);
			e.printStackTrace();
		}
//		List<Element> nodeList = XmlUtil.getNodeList(path, "pujjr-excel.xml", "pjrp", "http://www.pujjr.com/pujjrExcel", "pjrp:excel");
		List<Element> nodeList = XmlUtil.getNodeList(fis, "pjrp", "http://www.pujjr.com/pujjrExcel", "pjrp:excel");
		Iterator<Element> it = nodeList.iterator();
		while(it.hasNext()){
			Element element = it.next();
			if(tranCode.equals(element.attribute("tranCode").getText())){
				currElement = element;
				List<Attribute> attrs = element.attributes();
				excelCfg = (ExcelCfg) XmlUtil.getNodeCfg(excelCfg, attrs);
			}
		}
		//pjrp:excel所有孩子节点
		List<Element> elementList = currElement.elements();
		for (Element element : elementList) {
			String elmentName = element.getName();
			List<Attribute> attrList = element.attributes();
			switch(elmentName){
			case "title":
				ExcelTitleCfg titleCfg = new ExcelTitleCfg();
				titleCfg = (ExcelTitleCfg) XmlUtil.getNodeCfg(titleCfg, attrList);
				excelCfg.setExcelTitle(titleCfg);
				break;
			case "cols":
				ExcelColumnsCfg colsCfg = new ExcelColumnsCfg();
//				List<Field> colCfgFields = Utils.getFieldList(colsCfg.getClass());
				colsCfg = (ExcelColumnsCfg) XmlUtil.getNodeCfg(colsCfg, attrList);
				List<ExcelColumnCfg> excelColList = new ArrayList<ExcelColumnCfg>();
				//cols节点下所有col节点
				List<Element> colElementList = element.elements();
				for (Element colElement : colElementList) {
					ExcelColumnCfg colCfg = new ExcelColumnCfg();
					List<Attribute> colAttrList = colElement.attributes();
					colCfg = (ExcelColumnCfg) XmlUtil.getNodeCfg(colCfg, colAttrList);
					if("".equals(colElement.getStringValue()))
						colCfg.setName(colCfg.getId());
					else
						colCfg.setName(colElement.getStringValue());//列名
					excelColList.add(colCfg);
				}
				colsCfg.setExcelColList(excelColList);
				excelCfg.setExcelColumns(colsCfg);
				break;
			case "content":
				ExcelContentCfg contentCfg = new ExcelContentCfg();
				List<Field> contentCfgFields = Utils.getFieldList(contentCfg.getClass());
				contentCfg = (ExcelContentCfg) XmlUtil.getNodeCfg(contentCfg, attrList);
				excelCfg.setExcelContent(contentCfg);
				break;
			case "conditions":
				ExcelConditionsCfg conditionsCfg = new ExcelConditionsCfg();
				conditionsCfg = (ExcelConditionsCfg) XmlUtil.getNodeCfg(conditionsCfg, attrList);
				//conditions节点下所有condition节点
				List<Element> conditionElementList = element.elements();
				List<ExcelConditionCfg> excelConditionList = new ArrayList<ExcelConditionCfg>();
				for (Element conditionElement : conditionElementList) {
					ExcelConditionCfg conditionCfg = new ExcelConditionCfg();
					List<Attribute> conditionAttrList = conditionElement.attributes();
					conditionCfg = (ExcelConditionCfg) XmlUtil.getNodeCfg(conditionCfg, conditionAttrList);
					conditionCfg.setName(conditionElement.getStringValue());
					excelConditionList.add(conditionCfg);
				}
				conditionsCfg.setConditionList(excelConditionList);
				excelCfg.setConditionsCfg(conditionsCfg);
				break;
			}
			
			/*if("title".equals(elmentName)){
				ExcelTitleCfg titleCfg = new ExcelTitleCfg();
				titleCfg = (ExcelTitleCfg) XmlUtil.getNodeCfg(titleCfg, attrList);
				excelCfg.setExcelTitle(titleCfg);
			}else if("cols".equals(elmentName)){
				ExcelColumnsCfg colsCfg = new ExcelColumnsCfg();
//				List<Field> colCfgFields = Utils.getFieldList(colsCfg.getClass());
				colsCfg = (ExcelColumnsCfg) XmlUtil.getNodeCfg(colsCfg, attrList);
				List<ExcelColumnCfg> excelColList = new ArrayList<ExcelColumnCfg>();
				//cols节点下所有col节点
				List<Element> colElementList = element.elements();
				for (Element colElement : colElementList) {
					ExcelColumnCfg colCfg = new ExcelColumnCfg();
					colCfg.setName(colElement.getStringValue());//列名
					List<Attribute> colAttrList = colElement.attributes();
					colCfg = (ExcelColumnCfg) XmlUtil.getNodeCfg(colCfg, colAttrList);
					excelColList.add(colCfg);
				}
				colsCfg.setExcelColList(excelColList);
				excelCfg.setExcelColumns(colsCfg);
			}else if("content".equals(elmentName)){
				ExcelContentCfg contentCfg = new ExcelContentCfg();
				List<Field> contentCfgFields = Utils.getFieldList(contentCfg.getClass());
				contentCfg = (ExcelContentCfg) XmlUtil.getNodeCfg(contentCfg, attrList);
				excelCfg.setExcelContent(contentCfg);
			}else if("conditions".equals(elmentName)){
				ExcelConditionsCfg conditionsCfg = new ExcelConditionsCfg();
				conditionsCfg = (ExcelConditionsCfg) XmlUtil.getNodeCfg(conditionsCfg, attrList);
				//conditions节点下所有condition节点
				List<Element> conditionElementList = element.elements();
				List<ExcelConditionCfg> excelConditionList = new ArrayList();
				for (Element conditionElement : conditionElementList) {
					ExcelConditionCfg conditionCfg = new ExcelConditionCfg();
					List<Attribute> conditionAttrList = conditionElement.attributes();
					conditionCfg = (ExcelConditionCfg) XmlUtil.getNodeCfg(conditionCfg, conditionAttrList);
					excelConditionList.add(conditionCfg);
				}
				conditionsCfg.setConditionList(excelConditionList);
			}*/
//			logger.info(element.getName());
		}
//		logger.info((elementList.size()));
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return excelCfg;
	}
	
	/**
	 * 获取xml指定节点
	 * @param path xml文件所在目录
	 * @param fileName xml文件名
	 * @param prefix xml节点前缀
	 * @param nameSpace 命名空间
	 * @param nodeName 节点名称(带前缀)
	 * @return
	 */
//	public static List<Element> getNodeList(String path,String fileName,String prefix,String nameSpace,String nodeName){
	public static List<Element> getNodeList(InputStream fis,String prefix,String nameSpace,String nodeName){
		List<Element> nodeList = null;
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
//			document = reader.read(new File(path + File.separator + fileName));
			document = reader.read(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put(prefix, nameSpace);
		XPath xpath = document.createXPath("//"+nodeName);
		xpath.setNamespaceURIs(map);
		nodeList = xpath.selectNodes(document);
		return nodeList;
	}
}
