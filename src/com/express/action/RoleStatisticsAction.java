package com.express.action;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.express.dao.HRoleDao;
import com.express.dao.HUserDao;
import com.express.model.Role;
import com.opensymphony.xwork2.ActionSupport;

public class RoleStatisticsAction extends ActionSupport {
	public HUserDao gethUserDao() {
		return hUserDao;
	}

	public void sethUserDao(HUserDao hUserDao) {
		this.hUserDao = hUserDao;
	}

	public HRoleDao gethRoleDao() {
		return hRoleDao;
	}

	public void sethRoleDao(HRoleDao hRoleDao) {
		this.hRoleDao = hRoleDao;
	}

	private HUserDao hUserDao=new HUserDao();
	private HRoleDao hRoleDao=new HRoleDao();
	private JFreeChart chart;
	public JFreeChart getChart(){
		chart = ChartFactory.createBarChart3D(
							"用户统计图", // 图表标题
							"角色", // 目录轴的显示标签
							"人数", // 数值轴的显示标签
							getDataSet(), // 数据集
							//PlotOrientation.HORIZONTAL , // 图表方向：水平
							PlotOrientation.VERTICAL , // 图表方向：垂直
							false, 	// 是否显示图例(对于简单的柱状图必须是false)
							false, 	// 是否生成工具
							false 	// 是否生成URL链接
							);
							
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("用户统计图", new Font("黑体", Font.ITALIC , 22))); 
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		//取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		//设置横轴显示标签的字体
		categoryAxis.setLabelFont(new Font("宋体" , Font.BOLD , 22));
		//分类标签以45度角倾斜
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体" , Font.BOLD , 18));
		//取得纵轴
		NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();
		//设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体" , Font.BOLD , 22));

		return chart;
	}
	
	private  CategoryDataset getDataSet()
	{
		List<Integer> valnum=new ArrayList<Integer>();
		List<Role> roles=new ArrayList<Role>();
		try {
			valnum=hUserDao.statisticsUser();
			roles=hRoleDao.statisticsRole();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if(roles.size()>valnum.size()){
			for(int i=0;i<roles.size()-valnum.size();i++){
				valnum.add(0);
			}
		}
		for(int i=0;i<roles.size();i++){
			dataset.addValue(valnum.get(i) , "" , roles.get(i).getRoleName());
		}
		
		return dataset;
	}
}
