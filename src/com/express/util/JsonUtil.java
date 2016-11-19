package com.express.util;
/*将后台的数据转换为json格式的，easy ui 才能够识别数据
 * 
 * 
 * */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static JSONArray formatRsToJsonArray(ResultSet rs)throws Exception{
		ResultSetMetaData md=rs.getMetaData();//获取列信息
		int num=md.getColumnCount();
		JSONArray array=new JSONArray();//定义返回数组
		while(rs.next()){
			JSONObject mapOfColValues=new JSONObject();//定义一个JSONObject
			for(int i=1;i<=num;i++){
				Object o=rs.getObject(i);
				if(o instanceof Date){
					mapOfColValues.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
				}else{
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));					
				}
			}
			array.add(mapOfColValues);
		}
		return array;
	}
}
