package com.pujjr.utils;

import java.util.HashMap;

/**
 * 系统变量池
 * @author tom
 *
 */
public class TransactionMapData implements Cloneable{
	private static HashMap<String,Object> hashMap;
	private static TransactionMapData tmd = null;
	public TransactionMapData(){
		
	}
	public static void main(String[] args) {
		TransactionMapData tmd = new TransactionMapData();
	}
	
	public synchronized static TransactionMapData getInstance(){
		if(tmd == null){
			tmd = new TransactionMapData();
			hashMap = new HashMap<String,Object>();
			tmd.setHashMap(hashMap);
		}else{
			tmd = TransactionMapData.tmd;
		}	
		return tmd;
	}
	
	public void put(String key,Object value){
		this.tmd.getHashMap().put(key, value);
	}
	
	public Object get(String key){
		return this.tmd.getHashMap().get(key);
	}
	
	public static HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public static void setHashMap(HashMap<String, Object> hashMap) {
		TransactionMapData.hashMap = hashMap;
	}
	
}
