package com.pujjr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import com.itextpdf.text.pdf.BaseFont;
import com.pujjr.enumeration.EIntervalMode;

public class Utils {
	private static  Logger logger = Logger.getLogger(Utils.class);
	
	
	public static int seq=0;
	
	/**
	 * 获取配置文件目录，配置所在目录为jar包所在目录下的conf目录中。
	 * @return jar包目录->conf目录绝对路径
	 */
	public static String getJarConfFilepath(){
		String filePath = Utils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File file = new File(filePath);
		filePath = file.getParent()+File.separator+"conf"+File.separator;
		return filePath;
	}
	
	/**
	 * 获取property value值
	 * @param key 键名
	 * @param fileName 文件名
	 * @return 键对应值
	 */
	public static String getProperty(String key,String fileName,String realPath){
		Properties pops = new Properties();
		String path = null;
		try {
			path = realPath+fileName;
			logger.info("获取属性:"+key+"|"+fileName+"|"+path);
			pops.load(new FileInputStream(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pops.getProperty(key);
	}
	
	/**
	 * 获取指定日期所在月份第一天
	 * @param currMonth 格式：2012-01或2012-01-05
	 * @return 月第一天，格式：2012-01-01
	 */
	public static String getFirstDayOfMonth(String currMonth){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = "";
		try {
			firstDayOfMonth = sdf2.format(sdf.parse(currMonth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("日期："+currMonth+"，所在月第一天："+firstDayOfMonth);
		return firstDayOfMonth;
	}
	
	/**
	 * 获取指定日期所在月份最后一天
	 * @param currMonth 格式：2012-01或2012-01-05
	 * @return 月最后一天，格式：2012-01-01
	 */
	public static String getLastDayOfMonth(String currMonth){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String lastDayOfMonth = "";
		try {
			calendar.setTime(sdf.parse(currMonth));
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			lastDayOfMonth = sdf2.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("日期："+currMonth+"，所在月最后一天："+lastDayOfMonth);
		return lastDayOfMonth;
	}
	
	/**
	 * 过滤List<HashMap<String, Object>>类型数据中的null为""
	 * @param dataList 待过滤数据
	 * @return 过滤后数据
	 */
	public static List<HashMap<String, Object>> filtDataList(List<HashMap<String, Object>> dataList) {
		for (HashMap<String, Object> rowMap : dataList) {
			Iterator keyIt = rowMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String key = (String) keyIt.next();
				rowMap.put(key, (rowMap.get(key) == null || "".equals(rowMap.get(key))) ? "" : rowMap.get(key));
			}
		}
		return dataList;
	}
	
	/**
	 * 获取set方法名
	 * @param attrName 属性名
	 * @return 对应set方法
	 */
	public static String getSetMethodName(String attrName){
		String methodName = "";
		if(attrName.length() > 0)
			methodName = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}
	/**
	 * 获取get方法名
	 * @param attrName 属性名
	 * @return 对应get方法名
	 */
	public static String getGetMethodName(String attrName) {
		String methodName = "";
		if(attrName.length() > 0)
			methodName = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}
	
	/**
	 * 获取当前年
	 * tom 2017年3月17日
	 * @return
	 */
	public static String getCurrYear(){
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return currCl.get(Calendar.YEAR)+"";
	}
	/**
	 * 获取当前月
	 * tom 2017年3月17日
	 * @return
	 */
	public static String getCurrMonth(){
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return Utils.leftPadding(currCl.get(Calendar.MONTH) + 1, "0", 2);
	}
	/**
	 * 获取当前日
	 * tom 2017年3月17日
	 * @return
	 */
	public static String getCurrDay(){
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return Utils.leftPadding(currCl.get(Calendar.DAY_OF_MONTH), "0", 2);
		
	}

	/**
	 * 字符串右补位
	 * tom 2017年3月3日
	 * @param src 待补位对象(整型、字符串)
	 * @param padding 补位字符
	 * @param length 补位后字符串长度
	 * @return 补位后字符串
	 */
	public static String rightPadding(Object src,String padding,int length){
		String strRet = src+"";
		int len = strRet.length();
		if(len < length){
			for (int i = 0; i < length - len; i++) {
				strRet = strRet + padding;
			}
		}
		return strRet;
	}

	/**
	 * 字符串左补位
	 * tom 2017年3月3日
	 * @param src 待补位对象(整型、字符串)
	 * @param padding 补位字符
	 * @param length 补位后字符串长度
	 * @return 补位后字符串
	 */
	public static String leftPadding(Object src,String padding,int length){
		String strRet = src+"";
		int len = strRet.length();
		if(len < length){
			for (int i = 0; i < length - len; i++) {
				strRet = padding + strRet;
			}
		}
		return strRet;
	}
	
	/**
	 * 获取微软雅黑字体对象(pdf打印)
	 * tom 2017年2月27日
	 * @param contextPath
	 * @return
	 */
	public static BaseFont getYH(String contextPath){
		BaseFont bf = null;
		try {
			//使用普通字体
//			bf = BaseFont.createFont(contextPath+File.separator+"resources"+File.separator+"font"+File.separator+"MSYH.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			bf = BaseFont.createFont("STSongStd-Light",  "UniGB-UCS2-H", true);
//			bf = BaseFont.createFont("MHei-Medium",  "UniGB-UCS2-H", true);
//			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,  "Cp1252", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}
	
	/**
	 * 获取家庭住址全称
	 * tom 2017年2月9日
	 * @param addrProvinceName 省
	 * @param addrCityName 市
	 * @param addrCountyName 区
	 * @param addrExt 地址明细
	 * @return
	 */
	public static String getAddrFullName(String addrProvinceName,String addrCityName,String addrCountyName,String addrExt){
		String addrFullName = "";//地址全称
		if(addrProvinceName == null)
			addrProvinceName  ="";
		if(addrCityName == null)
			addrCityName = "";
		if(addrCountyName == null)
			addrCountyName = "";
		if(addrExt == null)
			addrExt = "";
		addrFullName = StringUtils.trimAllWhitespace(addrProvinceName+" "+addrCityName+" "+addrCountyName+" "+addrExt);
		return addrFullName;
		
	}
	
	/**格式化对象中的Double对象
	 * tom 2016年11月23日
	 * @param obj 待转换Double成员属性对象
	 * @param scale 转换后Double成员属性小数位数
	 * @return 转换后对象（当前对象）
	 */
	public static Object formateDoubleOfObject(Object obj,int scale){
		Class objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();
		Method[] methods = objClass.getMethods();
		List<Field> fieldList = Utils.getFieldList(objClass);
		for (Field field : fieldList) {
			if(field.getType().getName().equals("double") || field.getType().getName().equals("java.lang.Double")){//目前仅支持转double、Double对象数据
				try {
//					System.out.println(field.getName());
					String getMethodStr = Utils.field2GetMethod(field.getName());
					String setMethodStr = Utils.field2SetMethod(field.getName());
					Method getMethod = objClass.getMethod(getMethodStr);
					Method setMethod = null;
					try {
						setMethod = objClass.getMethod(setMethodStr,Double.class);
					} catch (Exception e) {
						setMethod = objClass.getMethod(setMethodStr,double.class);
					}
					Double score = (Double) getMethod.invoke(obj, null);
					if(score != null)
						setMethod.invoke(obj,Utils.formateDouble2Double(score, scale));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return obj;
	}
	
	/**
	 * 获取当前日期
	 * tom 2016年11月14日
	 * @return 当前日期
	 */
	public static Date getDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	/**
	 * 属性拷贝(拷贝list成员变量)
	 * tom 2016年11月7日
	 * @param source 源对象
	 * @param dest 目标对象
	 */
	/*public static void copyPropertiesDeep(Object source,Object dest){
		Class srcCls = source.getClass();
		Class destCls = dest.getClass();
		List<Field> srcFieldList = Utils.getFieldList(srcCls);
		List<Field> destFieldList = Utils.getFieldList(destCls);
		Method[] srcMethods = srcCls.getMethods();
		Method[] destMethods = destCls.getMethods();
		List destList = new ArrayList();
		for (int i = 0; i < srcFieldList.size(); i++) {
			Field srcField = srcFieldList.get(i);
			String srcFieldName = srcField.getName();
			Class srcFieldType = srcField.getType();
			String srcFieldTypeName = srcFieldType.getName();
			Object srcFieldValue = null;
			try {//处理source中list成员变量
				srcFieldValue = srcField.get(source);
//				System.out.println(srcFieldType.getName().equals(double.class.getName())+"*********|"+srcFieldValue);
				for (Field destField : destFieldList) {
					String destFieldName = destField.getName();
					Class destFieldType = destField.getType();
					if(srcFieldName.equals(destFieldName)){
						if(srcFieldTypeName.equals(List.class.getName())){//判断list变量
//							System.out.println(srcFieldType+"|"+srcFieldName+"|"+srcField.getGenericType());
							List tempSrcFieldValue = (List) srcField.get(source);
							Type gType  = destField.getGenericType();
							ParameterizedType pType = (ParameterizedType) gType;
							Type[] types = pType.getActualTypeArguments();
//							System.out.println("****types[0]:"+types[0]+"|"+"types[0].getTypeName():"+types[0].getTypeName()+"|"+types[0].getTypeName().equals(RepayScheduleDetailPo.class.getTypeName())+"|"+types[0].getClass());
//							System.out.println("ttt:"+srcField.get(source));
							for (Object object : tempSrcFieldValue) {
//								Object rsdv = Class.forName(types[0].getTypeName()).newInstance();//目前仅仅拷贝list泛型中含有一个参数的情况，如：List<String>
								Object rsdv = Class.forName(types[0].toString().split(" ")[1]).newInstance();
								Utils.copyProperties(object, rsdv);
								destList.add(rsdv);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//处理resource中普通成员变量
			for (Field destField : destFieldList) {
				String destFieldName = destField.getName();
				Class destFieldType = destField.getType();
//				System.out.println(destFieldType+"|"+destFieldName);
				if(destFieldName.equals(srcFieldName)){
					for (int j = 0; j < destMethods.length; j++) {
						Method destMethod = destMethods[j];
						String methodName = destMethod.getName();
						if(("set"+destFieldName).toLowerCase().equals(methodName.toLowerCase())){
							try {
								if(srcFieldTypeName.equals(List.class.getName())){
									destMethod.invoke(dest, destList);
								}else if(srcFieldValue != null){
									if(srcFieldTypeName.equals(Date.class.getName())){
										SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
										destMethod.invoke(dest, formater.format(srcFieldValue));
									}else if(srcFieldTypeName.equals(Double.class.getName()) || srcFieldTypeName.equals(double.class.getName())){
										destMethod.invoke(dest, Utils.formateDouble2String((double)srcFieldValue, 2));
//										destMethod.invoke(dest, srcFieldValue);
									}else if(srcFieldTypeName.equals(Integer.class.getName()) || srcFieldTypeName.equals(int.class.getName())){
										destMethod.invoke(dest, srcFieldValue+"");
//										destMethod.invoke(dest, srcFieldValue);
									}else if(!srcFieldType.isPrimitive()){
										destMethod.invoke(dest, srcFieldValue);
									}
//									System.out.println("***********************srcFieldType.isMemberClass():"+srcFieldType.getName()+"|destFieldType:"+destFieldType.getName()+"|"+srcFieldType.getSuperclass());
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}*/
	
	/**
	 * 双精度浮点数转制定格式字符串
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出："123.100"）
	 */
	public static String formateDouble2String(double number,int scale){
		String formateDouble = "";
		BigDecimal formater = new BigDecimal(number);
//		new Double("");
		formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		return formateDouble;
	}
	
	/**
	 * 双精度浮点数转指定格式双进度浮点数
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1对应BigDecimal对象 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(BigDecimal bigDecimal,int scale){
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 双精度浮点数转指定格式双精度浮点数
	 * tom 2016年11月2日
	 * @param number 数据源
	 * @param scale 小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(double number,int scale){
		Double formateDouble = 0.00;
		try {
		BigDecimal formater = new BigDecimal(number);
			formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e) {
		}
		
		return formateDouble;
	}
	
	/**
	 * 获取时间间隔
	 * tom 2016年11月8日
	 * @param beginDate 开始日期
	 * @param endDate 截止日期
	 * @param intervalMode 间隔模式
	 * @return 时间间隔
	 */
	public static long getTimeInterval(Date beginDate,Date endDate,EIntervalMode intervalMode){
		long interval = 0;
		Calendar beginCl = Calendar.getInstance();
		Calendar endCl = Calendar.getInstance();
		beginCl.setTime(beginDate);
		endCl.setTime(endDate);
		switch(intervalMode.name()){
		case "YEARS":
			interval = endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR);
			break;
		case "MONTHS":
			interval = (endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR)) * 12 + endCl.get(Calendar.MONTH) - beginCl.get(Calendar.MONTH);
			break;
		case "DAYS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(24 * 60 * 60 * 1000);
			break;
		case "HOURS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(60 * 60 * 1000);
			break;
		case "MINUTES":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(60 * 1000);
			break;
		case "SECONDS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis())/(1000);
		case "MIllISECCONDS":
			interval = endCl.getTimeInMillis() - beginCl.getTimeInMillis();
			break;
		}
		return interval;
	}
	
	/**
	 * 日期格式化
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateDate(Date date,String formateStr){
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(formate.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
	}
		return dateRet;
	}
	
	/**
	 * 字符串转日期
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateString2Date(String date,String formateStr){
		/*Pattern pt = Pattern.compile("^\\d{4}-{4}-\\d{2}-\\d{2}$");
		
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		String[] patterns = new String[]{"^\\d{4}$","^\\d{4}-\\d{2}$","^\\d{4}-\\d{2}-\\d{2}$","^\\d{4}-\\d{2}-\\d{2} \\d{2}$","^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$","^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$","^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d*$"};
		String[] dateFormats = new String[]{"yyyy","yyyy-MM","yyyy-MM-dd","yyyy-MM-dd HH","yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.S"};
		Date dateRet = null;
		for (int i = 0;i < patterns.length;i++) {
			Pattern pt = Pattern.compile(patterns[i]);
			Matcher matcher = pt.matcher(date);
			if(matcher.find()){
				SimpleDateFormat sdfParse = new SimpleDateFormat(dateFormats[i]);
				SimpleDateFormat sdfFormat = new SimpleDateFormat(formateStr);
				try {
					Date dateSrc = sdfParse.parse(date);
					dateRet = dateSrc;
					System.out.println(sdfFormat.format(dateSrc));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return dateRet;
	}
	
	/**
	 * 日期转字符串
	 * tom 2016年11月7日
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static String formateDate2String(Date date,String formateStr){
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		String dateRet = "";
		try {
			dateRet = formate.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateRet;
	}
	

	/**
	 * @param fieldName 属性名
	 * @return 属性对应get方法
	 */
	public static String field2GetMethod(String fieldName){
		StringBuffer buffer = new StringBuffer();
		buffer.append("get");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}
	
	/**
	 * @param fieldName 属性名
	 * @return 属性对应set方法
	 */
	public static String field2SetMethod(String fieldName){
		StringBuffer buffer = new StringBuffer();
		buffer.append("set");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}
	
	/**
	 * 获取指定日期所在当月天
	 * tom 2016年10月28日
	 */
	public static String getDayOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH)+"";
	}
	
	/**
    * 数字金额转中文大写
    * @param money 数字金额
    * @return 中文大写金额
    */
    public static String number2Chn(double money) {
    	/**
	     * 汉语中数字大写
	     */
	    final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
	            "伍", "陆", "柒", "捌", "玖" };
	    /**
	     * 汉语中货币单位大写，这样的设计类似于占位符
	     */
	    final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
	            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
	            "佰", "仟" };
	    /**
	     * 特殊字符：整
	     */
	    final String CN_FULL = "整";
	    /**
	     * 特殊字符：负
	     */
	    final String CN_NEGATIVE = "负";
	    /**
	     * 金额的精度，默认值为2
	     */
	    final int MONEY_PRECISION = 2;
        /**
        * 特殊字符：零元整
        */
        final String CN_ZEOR_FULL = "零元" + CN_FULL;
    	
    	BigDecimal numberOfMoney = new BigDecimal(money);
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
	
	
	/**
	 * 递归所有父类field
	 * @param obj 当前递归对象
	 * @param fieldList 所有field列表
	 */
	public static void getField(Class obj,List<Field> fieldList){
		Field[] fields = obj.getDeclaredFields();
		if(!obj.getName().equals("java.lang.Object")){
			for (Field field : fields) {
				field.setAccessible(true);
				fieldList.add(field);
			}
			Utils.getField(obj.getSuperclass(), fieldList);
		}
	}
	/**
	 * 获取对象所有field
	 * @param obj
	 * @return
	 */
	public static List<Field> getFieldList(Class obj){
		List<Field> fieldList = new LinkedList<Field>();
		Utils.getField(obj, fieldList);
		return fieldList;
	}
	
	/**
	 * @param date 给定日期
	 * @param interval 间隔天数，示例：5：5天以后;-6:6天以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterDay(Date date,int interval){
		String afterYear = "";
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, interval);
		return calender.getTime();
	}
	/**
	 * @param date 给定日期
	 * @param interval 间隔月份，示例：5：5个月以后;-6:6个月以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterMonth(Date date,int interval){
		String afterYear = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, interval);
		return calendar.getTime();
	}
	/**
	 * @param date 给定日期
	 * @param interval 间隔年份，示例：5：5年以后;-6:6年以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterYear(Date date,int interval){
		String afterYear = "";
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.YEAR, interval);
		return calender.getTime();
	}
	
	/**
	 * 对象属性拷贝
	 * @param source 数据源对象
	 * @param target 目标对象
	 * @author pujjr 2016-10-09
	 */
	public static void copyProperties(Object source, Object target){
		if(source != null)
			BeanUtils.copyProperties(source, target);
		else
			target = null;
	}
	
	public static String convertStr2Utf8(String value) throws UnsupportedEncodingException
	{
		if(value!=null)
		{
			value=new String(value.getBytes("ISO8859-1"),"UTF-8");
		}
		return value;
	}
	
	/**
	 * 过滤null对象
	 * @param obj
	 * @return
	 */
	public static String nullFilter(Object obj){
		return obj == null?"":obj.toString();
	}
	
	/**
	 * 对应数据表列名转对象属性
	 * @param colName  输入格式："my_col_name"
	 * @return 返回格式：：myColName
	 */
	public static String col2Field(String colName){
		StringBuffer fieldNameBuf = new StringBuffer();
		String[] colNames = colName.split("_");
		for (int i = 0; i < colNames.length; i++) {
			String temp = colNames[i];
			if(i==0)
				fieldNameBuf.append(temp);
			else{
				fieldNameBuf.append(temp.substring(0, 1).toUpperCase());
				fieldNameBuf.append(temp.substring(1,temp.length()));
			}
		}
		return fieldNameBuf.toString();
	}
	
	/**
	 * 对象属性转换为对应数据表列名 
	 * @param propName  输入格式："myUserName"
	 * @return 返回格式：：MY_USER_NAME
	 */
	public static String field2Col(String propName){
//		System.out.println("对象属性转换前："+propName);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < propName.length(); i++) {
			char c = propName.charAt(i);
			if(Character.isUpperCase(c)){
				sb.append("_"+Character.toLowerCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 获取日期天数
	 * **/
	public static int getSpaceDay(Date beginDate,Date endDate)
	{
		Date fBeginDate = Utils.formateDate(beginDate, "yyyyMMdd");
		Date fEndDate = Utils.formateDate(endDate, "yyyyMMdd");
		return (int) ((fEndDate.getTime()-fBeginDate.getTime())/(24*60*60*1000));
	}
	/**比较时间大小**/
	public static long compareDateTime(Date beginDate,Date endDate)
	{
		Long space = (endDate.getTime()-beginDate.getTime());
		return Long.compare(space, 0);
	}
	/**
	 * 按照指定格式获取当前日期
	 * **/
	public static String getCurrentTime(String format)
	{
		if(format==null||format==""||format.length()==0)
		{
			format="yyyyMMddHHmmss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		return df.format(new Date());
	}
	/**
	 * 根据日期获取年份
	 * **/
	public static String getYear(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
		return df.format(date);
	}
	/**
	 * 根据日期获取月份
	 * **/
	public static String getMonth(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("MM");//设置日期格式
		return df.format(date);
	}
	/**
	 * 日期转字符串
	 * **/
	public static String getFormatDate(Date date,String format)
	{
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		return df.format(date);
	}
	/**
	 * 根据年份获取当年天数
	 * **/
	public static int getYearDays(String year)
	{
		if((Integer.valueOf(year)%4==0&&Integer.valueOf(year)/100!=0)||(Integer.valueOf(year)/400==0))
		{
			return 366;
		}
		else
		{
			return 365;
		}
	}
	/**
	 * 8日期字符串转日期格式
	 * @throws ParseException 
	 * **/
	public static Date str82date(String date) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		return df.parse(date);
	}
	public static Timestamp str2time(String time) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");//设置日期格式
		return new Timestamp((df.parse(time)).getTime());
	}
	public static String get16UUID()
	{
		String uuid=UUID.randomUUID().toString();
		byte[] outputByteArray;
		 try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			byte[] inputByteArray=uuid.getBytes();
			messageDigest.update(inputByteArray);
			outputByteArray=messageDigest.digest();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} 
		 StringBuffer buf = new StringBuffer("");
         for (int offset = 0; offset < outputByteArray.length; offset++) {
            int  i = outputByteArray[offset];
             if (i < 0)
                 i += 256;
             if (i < 16)
                 buf.append("0");
             buf.append(Integer.toHexString(i));
         }
        return buf.toString().substring(8,24);
	}
	
	public static String getFileSuffix(String fileName)
	{
		int indexSuffix;
		if((indexSuffix=fileName.lastIndexOf("."))!=-1)
		{
			return fileName.substring(indexSuffix, fileName.length());
		}
		return fileName;
	}
	
	/** 
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。 
	 * @param src byte[] data 
	 * @return hex string 
	 */     
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	/**
	 * 克隆List
	 * @param src  源List
	 * **/
	public static List<Object> cloneList(List<Object> src)
	{
		List<Object> dest =new ArrayList<Object>(Arrays.asList(new Object[src.size()]));
		Collections.copy(dest, src);
		return dest;
	}
	/**
	 * 金额元转分
	 * @param amount
	 * @return
	 */
	public static String convertY2F(double amount)
	{
		NumberFormat nf = new DecimalFormat("#");
		return nf.format(amount*100);
	}
	/**
	 * HTML时间转换为JAVA时间
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date htmlTime2Date(String time,String format) throws ParseException
	{
		String tmp = time.replace("Z", " UTC");//注意是空格+UTC
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
		tmp = Utils.getFormatDate(sd.parse(tmp), format);
		return Utils.formateString2Date(tmp, format);
	}
	
	public static int getDateDay(Date date)
	{
		Calendar a = Calendar.getInstance();  
		a.setTime(date);
		return a.get(Calendar.DATE);
	}
	
	public static boolean doubleIsZero(double val)
	{
		if(Math.abs(val-0.00)<0.001)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	 public static void main(String args[]) throws ParseException 
	 { 
		 /*
		    Date firstRepay = Utils.str82date("20170323");
	        System.out.println("计算归档逾期任务"+Utils.formateDate2String(Utils.getDateAfterDay(new Date(), 45),"yyyy-MM-dd"));
	        System.out.println("创建放款电话回访任务"+Utils.formateDate2String(Utils.getDateAfterDay(new Date(), 15),"yyyy-MM-dd"));
	        System.out.println("创建商业保险续保任务"+Utils.formateDate2String(Utils.getDateAfterDay(new Date(), 30),"yyyy-MM-dd"));
	        System.out.println("还款日前1天提醒"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, -1),"yyyy-MM-dd"));
	        System.out.println("还款日前2天提醒"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, -2),"yyyy-MM-dd"));
	        System.out.println("还款日前7天提醒"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, -7),"yyyy-MM-dd"));
	        System.out.println("欢迎短信提醒"+Utils.formateDate2String(Utils.getDateAfterDay(new Date(), 15),"yyyy-MM-dd"));
	        System.out.println("逾期第2"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 2),"yyyy-MM-dd"));
	        System.out.println("逾期第5"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 5),"yyyy-MM-dd"));
	        System.out.println("逾期第7"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 7),"yyyy-MM-dd"));
	        System.out.println("逾期第9"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 9),"yyyy-MM-dd"));
	        System.out.println("逾期第12"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 12),"yyyy-MM-dd"));
	        System.out.println("逾期第15"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 15),"yyyy-MM-dd"));
	        System.out.println("逾期第17"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 17),"yyyy-MM-dd"));
	        System.out.println("逾期第24"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 24),"yyyy-MM-dd"));
	        System.out.println("逾期第30"+Utils.formateDate2String(Utils.getDateAfterDay(firstRepay, 30),"yyyy-MM-dd"));
	        Date insEndDate = Utils.str82date("20170426");
	        System.out.println("保险续保"+Utils.formateDate2String(Utils.getDateAfterDay(insEndDate, -30),"yyyy-MM-dd"));*
	        */
		 String[] arrayA = new String[] { "1", "2", "3"};    
		  String[] arrayB = new String[] { "2","3", "4", "5"}; 
		  List<String> a = Arrays.asList(arrayA);    
		  List<String> b = Arrays.asList(arrayB);    
		  //并集    
		  Collection<String> union = CollectionUtils.union(a, b);    
		  //交集    
		  Collection<String> intersection = CollectionUtils.intersection(a, b);    
		  //交集的补集    
		  Collection<String> disjunction = CollectionUtils.disjunction(a, b);    
		  //集合相减    
		  Collection<String> subtract = CollectionUtils.subtract(a, b); 
		  Collections.sort((List<String>) union);    
		  Collections.sort((List<String>) intersection);    
		  Collections.sort((List<String>) disjunction);    
		  Collections.sort((List<String>) subtract);    
		    
		  System.out.println("A: " + ArrayUtils.toString(a.toArray()));    
		  System.out.println("B: " + ArrayUtils.toString(b.toArray()));    
		  System.out.println("--------------------------------------------");    
		  System.out.println("Union(A, B): " + ArrayUtils.toString(union.toArray()));    
		  System.out.println("Intersection(A, B): " + ArrayUtils.toString(intersection.toArray()));    
		  System.out.println("Disjunction(A, B): " + ArrayUtils.toString(disjunction.toArray()));    
		  System.out.println("Subtract(A, B): " + ArrayUtils.toString(subtract.toArray()));  
	    } 
}