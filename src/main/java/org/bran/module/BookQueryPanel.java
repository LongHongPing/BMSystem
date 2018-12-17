package org.bran.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.bran.db.DBOperation;

public class BookQueryPanel extends JPanel {
	//日期
	private DatePanel preDate,nexDate;
	//查询框
	private JTextField query;
	//按钮
	private JButton queryb;
	//表格
	private JTable table;
	private DefaultTableModel model;
	private String[] headers={"书号","书名","书籍类别","作者","出版社","出版日期","价格","页数","关键词","登记日期","备注","状态"};
	private Object[][] cellData={{"","","","","","","","","","","",""}};
	private Object[] rowData={"","","","","","","","","","","",""};
	//下拉框
	private JComboBox<String> combobox;
	private String[] querySort={"查询书名","查询书号","查询ISBN","查询出版社","查询作者"};
	public BookQueryPanel(final DBOperation db){
		super();
		//创建分割面板 水平分割
		JSplitPane sp=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.add(sp);
		//创建默认表格模型
		final DefaultTableModel model=new DefaultTableModel(cellData,headers){
			@Override
			public boolean isCellEditable(int row, int column) {
				return super.isCellEditable(row, column);
			}
		};
		table=new JTable(model);
		//JPanel panel2=new JPanel();
		JScrollPane scroll=new JScrollPane();
		scroll.setViewportView(table);
		//创建输入类别
		combobox=new JComboBox<String>();
		for(int i=0;i<querySort.length;i++){
			combobox.addItem(querySort[i]);
		}
	//	panel2.add(table);
		//创建标签
		JPanel panel1=new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.add(combobox);
		//输入区
		JPanel minPanel1=new JPanel();
		minPanel1.setLayout(new BoxLayout(minPanel1, BoxLayout.X_AXIS));
		minPanel1.add(new JLabel("查询内容:"));
		query=new JTextField(30);
		minPanel1.add(query);
		queryb=new JButton("查询");
		minPanel1.add(queryb);	

		queryb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Date preTime=preDate.getDate();
				Date nexTime=nexDate.getDate();
				String content="%"+query.getText()+"%";
				model.setRowCount(1);
				
				ResultSet rs=null;
				PreparedStatement preStmt=null;
				try{
					//在使用preparedStatement时不要用+连接字符串
					if(preTime!=null&&nexTime!=null){
						if(preTime.compareTo(nexTime)>0){
							JOptionPane.showMessageDialog(null, "请输入正确的时间", "error", JOptionPane.ERROR_MESSAGE);
						}else{
							String sql="select book.bookId,bookname,booksort,author,publishname,publishdate,price,pagenum,keywords,registerdate,remarks ,status from book where (publishdate between ? and ?) and ISBN like ? or bookname like  ? or author like ?";
							preStmt=db.getPreparedStatement(sql);
							preStmt.setDate(1, preTime);
							preStmt.setDate(2, nexTime);
							preStmt.setString(3, content);
							preStmt.setString(4, content);
							preStmt.setString(5, content);
						}
					}else if(preTime==null&&nexTime==null){
						String sql="select bookId,bookname,booksort,author,publishname,publishdate,price,pagenum,keywords,registerdate,remarks ,status from book where ISBN like ? or bookname like ? or author like ?";
						preStmt=db.getPreparedStatement(sql);
						preStmt.setString(1, content);
						preStmt.setString(2, content);
						preStmt.setString(3, content);
						
					}else if(preTime!=null&&nexTime==null){
						String sql="select bookId,bookname,booksort,author,publishname,publishdate,price,pagenum,keywords,registerdate,remarks,status from book where publishdate > ? and ISBN like ? or bookname like ? or author like ?";
						preStmt=db.getPreparedStatement(sql);
						preStmt.setDate(1, preTime);
						preStmt.setString(2, content);
						preStmt.setString(3, content);
						preStmt.setString(4, content);
					}else if(preTime==null&&nexTime!=null){
						String sql="select bookId,bookname,booksort,author,publishname,publishdate,price,pagenum,keywords,registerdate,remarks,status from book where publishdate < ? and ISBN like ? or bookname like ? or author like ?";
						preStmt=db.getPreparedStatement(sql);
						preStmt.setDate(1, nexTime);
						preStmt.setString(2, content);
						preStmt.setString(3, content);
						preStmt.setString(4, content);
					}
					
					rs=preStmt.executeQuery();
					
					for(int i=0;rs.next();i++){
						model.setValueAt(rs.getString("bookId"), i, 0);
						model.setValueAt(rs.getString("bookname"), i,1);
						model.setValueAt(rs.getString("booksort"), i, 2);
						model.setValueAt(rs.getString("author"), i, 3);
						model.setValueAt(rs.getString("publishname"), i, 4);
						model.setValueAt(rs.getString("publishdate"), i, 5);
						model.setValueAt(rs.getString("price"), i, 6);
						model.setValueAt(rs.getString("pageNum"), i, 7);
						model.setValueAt(rs.getString("keywords"), i, 8);
						model.setValueAt(rs.getString("registerdate"), i, 9);
						model.setValueAt(rs.getString("remarks"), i, 10);
						model.setValueAt(rs.getString("status"), i, 11);
						model.addRow(rowData);
					}
				}catch(SQLException arg){
					arg.printStackTrace();
				}
			}
		});
		panel1.add(minPanel1);
		//日期
		JPanel minPanel2=new JPanel();
		minPanel2.setLayout(new BoxLayout(minPanel2, BoxLayout.X_AXIS));
		preDate=new DatePanel();
		nexDate=new DatePanel();
		minPanel2.add(new JLabel("起始时间："));
		minPanel2.add(Box.createVerticalGlue());
		minPanel2.add(preDate);
		minPanel2.add(new JLabel("至 终止时间："));
		minPanel2.add(nexDate);
		minPanel2.add(Box.createVerticalGlue());
		panel1.add(minPanel2);
		
		//加入分割面板
		sp.add(panel1, JSplitPane.TOP);
		sp.add(scroll, JSplitPane.BOTTOM);
	}
/*
public static void main(String[] args) {
		JFrame test=new JFrame("test");
		test.setSize(600, 400);
		test.getContentPane().add(new BookQueryPanel(new DBOperation()));
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}
*/	
}
