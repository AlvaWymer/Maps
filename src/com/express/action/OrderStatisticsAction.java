package com.express.action;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import com.express.dao.HOrderDao;
import com.opensymphony.xwork2.ActionSupport;

public class OrderStatisticsAction extends ActionSupport {

	public HOrderDao gethOrderDao() {
		return hOrderDao;
	}
	public void sethOrderDao(HOrderDao hOrderDao) {
		this.hOrderDao = hOrderDao;
	}
	private JFreeChart chart;
	public JFreeChart getChart()
	{
		chart = ChartFactory.createPieChart3D(
			"订单统计图",  // 图表标题
			getDataSet(), //数据
			true, // 是否显示图例
			false, //是否显示工具提示
			false //是否生成URL
		);
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("订单统计图", new Font("黑体", Font.ITALIC , 22))); 
		//取得统计图标的第一个图例
		LegendTitle legend = chart.getLegend(0);
		//修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14)); 
		//获得饼图的Plot对象
		PiePlot plot = (PiePlot)chart.getPlot();
		//设置饼图各部分的标签字体
		plot.setLabelFont(new Font("隶书", Font.BOLD, 18)); 
		//设定背景透明度（0-1.0之间）
        plot.setBackgroundAlpha(0.9f);
		//设定前景透明度（0-1.0之间）
        plot.setForegroundAlpha(0.50f);
		return chart;
	}
	private HOrderDao hOrderDao=new HOrderDao();
	private DefaultPieDataset getDataSet()
	{
		int[] valnum=hOrderDao.statisticsAdmin();
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("总收入"+Double.parseDouble(valnum[0]+"")/1000+"(千元)",Double.parseDouble(valnum[0]+"")/1000);
		dataset.setValue("总订单数"+valnum[1]+"(个)",valnum[1]);
		dataset.setValue("已签收订单"+valnum[2]+"(个)",valnum[2]);
		dataset.setValue("正在派送订单"+valnum[4]+"(个)",valnum[4]);
		dataset.setValue("问题订单"+valnum[3]+"(个)",valnum[3]);
		return dataset;
	}
}
