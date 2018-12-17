package org.bran.module;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bran.db.DBOperation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

public class MyChartPanel extends JPanel {
	//ChartPanel
	private ChartPanel chartPanel;
	//列表框
	private JList list;
	String[] elements={"图书类型饼图"};
	/**
	 * constructor
	 */
	public MyChartPanel(DBOperation db){
		super();
		//分割面板  竖直分割
		JSplitPane sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.add(sp);
		//创建列表框
		JScrollPane leftPanel=new JScrollPane();	
		list=new JList<String>(elements);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent arg0) {
				
			}
		});
		leftPanel.setViewportView(list);
		
		//创建图表面板
		//创建一个默认的饼图
		ResultSet rs=null;
		DefaultPieDataset pdSet=new DefaultPieDataset();
//		pdSet.setValue("计算机", 50);
//		pdSet.setValue("小说", 80);
//		pdSet.setValue("文学", 30);
//		pdSet.setValue("艺术", 20);
		try {
			rs=db.executeQuery("select booksort,count(booksort) as count from book group by booksort");
			while(rs.next()){
				pdSet.setValue(rs.getString("booksort"), rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//TODO 饼图
		JFreeChart chart=ChartFactory.createPieChart("统计", pdSet, true,true,false);
		PiePlot plot=(PiePlot) chart.getPlot();
		 plot.setLabelFont(new Font("黑体", Font.PLAIN, 20));
	        TextTitle textTitle = chart.getTitle();
	        textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
	        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
		chartPanel=new ChartPanel(chart);
		
		//加入分割面板
		sp.add(leftPanel, JSplitPane.LEFT);
		sp.add(chartPanel, JSplitPane.RIGHT);
	}

	public static void main(String[] args) {
		JFrame test=new JFrame("test");
		test.setSize(600, 400);
		test.getContentPane().add(new MyChartPanel(new DBOperation()));
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}
}
