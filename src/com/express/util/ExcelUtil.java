package com.express.util;

import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.express.model.Order;
import com.express.model.User;

import java.util.Date;
import java.util.List;
public class ExcelUtil {

/*	public static void fillExcelData(ResultSet rs,Workbook wb,String[] headers)throws Exception{
		int rowIndex=0;
		Sheet sheet=wb.createSheet();
		Row row=sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		while(rs.next()){
			row=sheet.createRow(rowIndex++);
			for(int i=0;i<headers.length;i++){
				row.createCell(i).setCellValue(rs.getObject(i+1).toString());
			}
		}
	}*/
	
	public static void fillExcelData2(Workbook wb,String[] headers,List<User> users){
		int rowIndex=0;
		Sheet sheet=wb.createSheet();
		Row row=sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		for(int j=0;j<users.size();j++){
			row=sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(users.get(j).getUserId());
			row.createCell(1).setCellValue(users.get(j).getUserName());
			row.createCell(2).setCellValue(users.get(j).getPassword());
			row.createCell(3).setCellValue(users.get(j).getRoleName());
			row.createCell(4).setCellValue(users.get(j).getUserDescription());
		}

	}
	public static void fillExcelData(Workbook wb,String[] headers,List<Order> orders){
		int rowIndex=0;
		Sheet sheet=wb.createSheet();
		Row row=sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		for(int j=0;j<orders.size();j++){
			row=sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(orders.get(j).getOrderid());
			row.createCell(1).setCellValue(orders.get(j).getPubtime());
			row.createCell(2).setCellValue(orders.get(j).getRname());
			row.createCell(3).setCellValue(orders.get(j).getRaddress());
			row.createCell(4).setCellValue(orders.get(j).getRtel());
			row.createCell(5).setCellValue(orders.get(j).getRpostcode());
			row.createCell(6).setCellValue(orders.get(j).getSname());
			row.createCell(7).setCellValue(orders.get(j).getSaddress());
			row.createCell(8).setCellValue(orders.get(j).getStel());
			row.createCell(9).setCellValue(orders.get(j).getSpostcode());
			row.createCell(10).setCellValue(orders.get(j).getUsername());
			row.createCell(11).setCellValue(orders.get(j).getWeight());
			row.createCell(12).setCellValue(orders.get(j).getCost());
			row.createCell(13).setCellValue(orders.get(j).getStatus());
			
		}

	}
	
	
	/*public static String formatCell(HSSFCell hssfCell){
		if(hssfCell==null){
			return "";
		}else{
			if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
				return String.valueOf(hssfCell.getBooleanCellValue());
			}else if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
				return String.valueOf(hssfCell.getNumericCellValue());
			}else{
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}
	}*/
	
	public static String formatCell(HSSFCell cell) {
	        String strCell = "";
	        switch (cell.getCellType()) {
	        
	        case HSSFCell.CELL_TYPE_STRING:
	            strCell = cell.getStringCellValue();
	            break;
	        case HSSFCell.CELL_TYPE_NUMERIC:
	        	// 取得当前Cell的数值
	        	 Integer num = new Integer((int) cell.getNumericCellValue());
	        	//转换为字符串
	        	 strCell = String.valueOf(num); 

	        	 
	        	
	       // strCell = String.valueOf(cell.getNumericCellValue());
	        System.out.println(strCell+"strCell+++++++++++++++++++++++++");
	        	// strCell = cell.getStringCellValue();
	            break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:
	            strCell = String.valueOf(cell.getBooleanCellValue());
	            break;
	        case HSSFCell.CELL_TYPE_BLANK:
	            strCell = "";
	            break;
	        default:
	            strCell = "";
	            break;
	        }
	        if (strCell.equals("") || strCell == null) {
	            return "";
	        }
	        if (cell == null) {
	            return "";
	        }
	        return strCell;
	    }
}
