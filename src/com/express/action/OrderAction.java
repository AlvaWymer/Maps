package com.express.action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.express.dao.HOrderDao;
import com.express.model.Order;
import com.express.model.OrderLocation;
import com.express.model.PageBean;
import com.express.util.ExcelUtil;
import com.express.util.ResponseUtil;
import com.express.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OrderAction extends ActionSupport implements ServletRequestAware{

	public File getOrderUploadFile() {
		return orderUploadFile;
	}

	public void setOrderUploadFile(File orderUploadFile) {
		this.orderUploadFile = orderUploadFile;
	}

	private String page;
	private String rows;
	private List<Order> orders;
	private File orderUploadFile;
	
	private HOrderDao hOrderDao = new HOrderDao();
	HttpServletRequest request;		//获取request
	public String list() throws Exception{
		//封装pagebean
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=hOrderDao.list(pageBean);
		System.out.println(jsonArray);
		int total=hOrderDao.ordercount();
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	public String excellist() throws Exception{
		try {
		Workbook wb=new HSSFWorkbook();
		List<Order> orders=hOrderDao.excellist();
		String headers[]={"订单号","生成订单时间","收件人姓名","收件人地址","收件人电话","收件人邮编","寄件人姓名","寄件人地址","寄件人电话","寄件人邮编","操作人","快件重量","费用","快件状态"};
		ExcelUtil.fillExcelData(wb, headers, orders);
		ResponseUtil.export(ServletActionContext.getResponse(), wb, "订单信息导出.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public HOrderDao gethOrderDao() {
		return hOrderDao;
	}
	public void sethOrderDao(HOrderDao hOrderDao) {
		this.hOrderDao = hOrderDao;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	
	
	public String upload()throws Exception{
		POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(orderUploadFile));
		HSSFWorkbook wb=new HSSFWorkbook(fs);
		HSSFSheet hssfSheet=wb.getSheetAt(0);  // 获取第一个Sheet页
		JSONObject result=new JSONObject();
		if(hssfSheet!=null){
			for(int rowNum=1;rowNum<=hssfSheet.getLastRowNum();rowNum++){
				HSSFRow hssfRow=hssfSheet.getRow(rowNum);
				if(hssfRow==null){
					continue;
				}
				
				/*// 取值后会带一个E的问题   
				double OrderValue = hssfRow.getCell(0).getNumericCellValue();   
				String Order = new DecimalFormat("#").format(OrderValue);  */
				
				/*// 取值后会带一个E的问题   
				double RtelValue = hssfRow.getCell(4).getNumericCellValue();   
				String Rtel = new DecimalFormat("#").format(RtelValue);  */
				
				/*// 取值后会带一个E的问题   
				double StelValue = hssfRow.getCell(8).getNumericCellValue();   
				String Stel = new DecimalFormat("#").format(RtelValue);  */
				 try {
					 Order order=new Order();
						order.setOrderid(ExcelUtil.formatCell(hssfRow.getCell(0)));
						System.out.println(ExcelUtil.formatCell(hssfRow.getCell(0)));
						order.setPubtime(ExcelUtil.formatCell(hssfRow.getCell(1)));
						order.setRname(ExcelUtil.formatCell(hssfRow.getCell(2)));
						order.setRaddress(ExcelUtil.formatCell(hssfRow.getCell(3)));
						//order.setRtel(Rtel);
						order.setRtel(ExcelUtil.formatCell(hssfRow.getCell(4)));
						
						order.setRpostcode(ExcelUtil.formatCell(hssfRow.getCell(5)));
						order.setSname(ExcelUtil.formatCell(hssfRow.getCell(6)));
						order.setSaddress(ExcelUtil.formatCell(hssfRow.getCell(7)));
						//order.setStel(Stel);
						order.setStel(ExcelUtil.formatCell(hssfRow.getCell(8)));
						order.setSpostcode(ExcelUtil.formatCell(hssfRow.getCell(9)));
						order.setUsername(ExcelUtil.formatCell(hssfRow.getCell(10)));
						order.setWeight(ExcelUtil.formatCell(hssfRow.getCell(11)));
						order.setCost(ExcelUtil.formatCell(hssfRow.getCell(12)));
						order.setStatus(1);
						hOrderDao.add(order);
						
						OrderLocation orderLocation = new OrderLocation();
						orderLocation.setOrderid(ExcelUtil.formatCell(hssfRow.getCell(0)));
						orderLocation.setContext("揽件成功");
						orderLocation.setStatus(Integer.parseInt(ExcelUtil.formatCell(hssfRow.getCell(13))));
						//System.out.println(ExcelUtil.formatCell(hssfRow.getCell(13))+"strCell+++++++++++++++++++++++++");
						orderLocation.setTime(time());
						hOrderDao.addloc(orderLocation);
				} catch (Exception e) {
					// TODO: handle exception
					result.put("success", "true");
				}
				
				//******************************
			}
		}
		
		result.put("success", "true");
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}

	//获取当前的时间转换为String类型
	public String time(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		Date date=new Date();  
		return sdf.format(date);  
	}
}
