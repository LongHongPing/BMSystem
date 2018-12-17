package org.bran.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.bran.db.DBOperation;
import org.bran.valueBean.Reader;

public class BorrowPanel extends JPanel {
	//标签 显示,姓名，是否在黑名单，已借阅册数，最大借阅数
	private JLabel borrowNum;
	private JLabel maxNum;
	private JLabel name;
	private JLabel blackList;
	private JLabel typeName;

	//文本框 搜索输入
	private JTextField input;
	//按钮 查找 借阅 归还 更新
	private JButton queryB;
	private JButton borrowB;
	private JButton returnB;
	private JButton updateB;
	//表格 用于借阅
	private JTable table;
	private DefaultTableModel model;
	private String[] headers={"勾选","书号","ISBN","书名","出版社","状态"};
	private Object[][] cellData={{false,"","","","",""}};
	private Object[] rowData={false,"","","","",""};
	//表格 用于归还
	private JTable table2;
	private DefaultTableModel model2;
	private String[] headers2={"勾选","书号","书名","借阅日期","期限/月","归还日期"};
	private Object[][] cellData2={{false,"","","",""}};
	private Object[] rowData2={false,"","","",""};
	//下拉框
		private JComboBox<String> combobox;
		private String[] querySort={"查询书名","查询书号","查询ISBN","查询出版社","查询作者"};
	
	/**
	 * constructor
	 */
	public BorrowPanel(final Reader reader,final DBOperation db){
		super();
		//分割面板
		JSplitPane sp=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.add(sp);
		//读者信息显示
		name=new JLabel(reader.getName());
		maxNum=new JLabel(reader.getMaximum()+"");
		borrowNum=new JLabel("");
		blackList=new JLabel("当前借阅号可正常使用");
		
		typeName=new JLabel("");

		//表格 用于借阅
				 model=new DefaultTableModel(cellData,headers){
					@Override
					public boolean isCellEditable(int row, int column) {
						return super.isCellEditable(row, column);
					}
					/**
					 * jtabel内置复选框功能，须重写此方法
					 */
					@Override
		            public Class<?> getColumnClass(int column) {
		                Object value=getValueAt(0, column);
		                if(value!=null){
		                	return value.getClass();
		                }else return super.getClass();
		            }
					
				};
					
				table=new JTable(model);
				JScrollPane scroll=new JScrollPane();
				//显示表格 借阅
				scroll.setViewportView(table);
				JPanel panel1=new JPanel();
				panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
				//创建输入类别
				combobox=new JComboBox<String>();
				for(int i=0;i<querySort.length;i++){
					combobox.addItem(querySort[i]);
				}
				panel1.add(combobox);
				JPanel minPanel0=new JPanel();
				minPanel0.setLayout(new BoxLayout(minPanel0, BoxLayout.X_AXIS));
				minPanel0.add(new JLabel("姓名："));
				minPanel0.add(name);
				minPanel0.add(Box.createVerticalStrut(5));
				minPanel0.add(typeName);
				minPanel0.add(Box.createVerticalStrut(5));
				minPanel0.add(new JLabel("最大可借阅数："));
				minPanel0.add(maxNum);
				JPanel minPanel2=new JPanel();
				minPanel2.setLayout(new BoxLayout(minPanel2, BoxLayout.X_AXIS));
				minPanel2.add(blackList);
				minPanel2.add(Box.createGlue());
				JPanel minPanel1=new JPanel();	
				minPanel1.setLayout(new BoxLayout(minPanel1, BoxLayout.X_AXIS));
				minPanel1.add(new JLabel("查询:"));
				input=new JTextField(20);
				minPanel1.add(input);
				queryB=new JButton("查询");
				
				/**
				 * 【查询】注册监听器
				 */
				queryB.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						String inputText=input.getText();
						int bookId=0;
						model.setRowCount(1);
						try{
							bookId=Integer.parseInt(inputText);
						}catch(NumberFormatException e1){
							bookId=0;
						}
						inputText="%"+inputText+"%";
						ResultSet rs=null;
						PreparedStatement preStmt=null;
						String sql="select bookId,bookname,ISBN,publishName,status from book where bookId=? or bookname like ? or ISBN like ?";
						try {
							preStmt=db.getPreparedStatement(sql);
							preStmt.setInt(1, bookId);
							preStmt.setString(2, inputText);
							preStmt.setString(3, inputText);
							
							rs=preStmt.executeQuery();
							for(int i=0;rs.next();i++){
								model.setValueAt(false, i, 0);
								model.setValueAt(rs.getString("bookId"), i,1);
								model.setValueAt(rs.getString("ISBN"), i, 2);
								model.setValueAt(rs.getString("bookName"), i, 3);
								model.setValueAt(rs.getString("publishName"), i, 4);
								model.setValueAt(rs.getString("status"), i, 5);
								model.addRow(rowData);
							}
							preStmt.close();
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				minPanel1.add(queryB);
				borrowB=new JButton("借阅");
				borrowB.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						int rows=model.getRowCount()-1;
						int borrowed=model2.getRowCount()-1;
						int max=Integer.parseInt(maxNum.getText());
						int selected=0;
						boolean isBorrowed=false;
						for(int i=0;i<rows;i++){
							if((Boolean)model.getValueAt(i, 0)==true){
								selected++;
							}
							if(model.getValueAt(i, 5).equals("在库")){
								isBorrowed=true;
							}
						}
						if(isBorrowed){
							JOptionPane.showMessageDialog(null, "已借出", "sucess", JOptionPane.OK_OPTION);
						}else if(borrowed+selected>max){
							JOptionPane.showMessageDialog(null, "超过最大借阅限制", "sucess", JOptionPane.OK_OPTION);
						}else if(!reader.isViolate()){		
							Date date=new Date();
							java.sql.Date sqlDate=new java.sql.Date(date.getTime());
							CallableStatement cStmt = null;

							String sql = "call pro_borrow(?,?,?)";
							try {
								cStmt = db.getCallableStatement(sql);
								int count=0;
								for(int i=0;i<rows;i++){
									if((Boolean)model.getValueAt(i, 0)==true){
										int bookid=Integer.parseInt((String)model.getValueAt(i, 1));
										int readerid=reader.getId();

										cStmt.setInt(1, readerid);
										cStmt.setInt(2, bookid);
										cStmt.setDate(3, sqlDate);
										cStmt.addBatch();
										count++;
									}
								}
								int[] checkrows=cStmt.executeBatch();
								model.setRowCount(1);
								model.removeRow(0);
								if(checkrows.length==count){
									JOptionPane.showMessageDialog(null, "借阅成功");
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}else
							JOptionPane.showMessageDialog(null, "不能借阅", "error", JOptionPane.ERROR_MESSAGE);
					}
				});
				minPanel1.add(borrowB);
				panel1.add(minPanel0);
				panel1.add(minPanel2);
				panel1.add(minPanel1);

				//表格 用于还书
				model2=queryModel(db, reader);
				table2=new JTable(model2);
				JScrollPane scroll2=new JScrollPane();
				//显示表格 还书
				scroll2.setViewportView(table2);
				//还书控制面板
				JPanel panel2=new JPanel();
				panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
				panel2.add(new JLabel("已借阅的图书，选中可归还"));
				panel2.add(Box.createGlue());
				returnB=new JButton("归还");
				returnB.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						Date date=new Date();
						java.sql.Date sqlDate=new java.sql.Date(date.getTime());
						//PreparedStatement pStmt=null;
						CallableStatement cStmt = null;
						int rows=model2.getRowCount()-1;
						int count=0;
						//String sql="update borrow set returnTime=? where bookId=?";
						String sql = "call pro_return(?,?)";
						try {
							//pStmt=db.getPreparedStatement(sql);
							cStmt = db.getCallableStatement(sql);

							for(int i=0;i<rows;i++){
								if((Boolean)model2.getValueAt(i, 0)==true){
									int bookid=(Integer)model2.getValueAt(i, 1);
									cStmt.setInt(1,bookid);
									//pStmt.setInt(2, bookid);

									int readerid=reader.getId();
									cStmt.setInt(2,readerid);

									//pStmt.setDate(1, sqlDate);
									model2.removeRow(i);

									//pStmt.addBatch();
									cStmt.addBatch();
									count++;
								}
							}
							//int[] checkrows=pStmt.executeBatch();
							int[] checkrows=cStmt.executeBatch();
							if(checkrows.length==count){
								JOptionPane.showMessageDialog(null, "还书成功");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				panel2.add(returnB);
				updateB=new JButton("更新");
				updateB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						model2=queryModel(db, reader);

					}
				});
				panel2.add(updateB);
				//TODO 分割面板
				JSplitPane sp2=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp2.add(panel1,JSplitPane.TOP);
				sp2.add(scroll, JSplitPane.BOTTOM);	
				sp.add(sp2, JSplitPane.TOP);
				JSplitPane sp3=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.add(panel2, JSplitPane.TOP);
				sp3.add(scroll2, JSplitPane.BOTTOM);
				sp.add(sp3, JSplitPane.BOTTOM);
				sp.setDividerLocation(0.2);
	}
	private DefaultTableModel queryModel(DBOperation db,Reader reader){
		DefaultTableModel model3=new DefaultTableModel(cellData2,headers2){
			@Override
			public boolean isCellEditable(int row, int column) {
				return super.isCellEditable(row, column);
			}
			/**
			 * jtabel内置复选框功能，须重写此方法
			 */
			@Override
            public Class<?> getColumnClass(int column) {
                Object value=getValueAt(0, column);
                if(value!=null){
                	return value.getClass();
                }else return super.getClass();
            }
			
		};//连接jdbc获取用户的信息
		PreparedStatement preStatement=null;
		ResultSet rs1=null;
		ResultSet rs2=null;
		String sql1="select readerName,borrow.bookid as id,bookName,borrowDate,maximum,overDueCount,returnTime "
				+ "from book,borrow,reader left join blacklist on blacklist.readerId=reader.readerId "
				+ "where reader.readerId=borrow.readerId and book.bookid=borrow.bookId and reader.readerId=?";
		String sql2="select maximum from reader where readerId=?";
		try {
			preStatement=db.getPreparedStatement(sql1);
			preStatement.setInt(1, reader.getId());
			rs1=preStatement.executeQuery();

				for(int i=0;rs1.next();i++){
				model3.setValueAt(false, i, 0);
				model3.setValueAt(rs1.getInt("Id"), i, 1);
				model3.setValueAt(rs1.getString("bookName"), i, 2);
				model3.setValueAt(rs1.getDate("borrowDate").toString(), i, 3);
				model3.setValueAt(rs1.getInt("maximum"), i, 4);
				model3.setValueAt(rs1.getDate("returnTime").toString(), i, 5);
				if(rs1.getInt("overDueCount")==0){
					blackList.setText("当前借阅号可正常使用");
				}else{
					reader.setViolate(true);
					blackList.setText("当前借阅号在黑名单中，请尽快归还到期图书");
				}
				model3.addRow(rowData2);
			}
			preStatement=db.getPreparedStatement(sql2);
			preStatement.setInt(1,reader.getId());
			rs2=preStatement.executeQuery();
			if(rs2.next()){
				maxNum.setText(rs2.getInt("maximum")+"");
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return model3;
	}
	/*模块测试用
	public static void main(String[] args) {
		JFrame test=new JFrame("test");
		test.setSize(600, 400);
		Reader reader=new Reader();
		reader.setName("test");
		reader.setId(10000);
		test.getContentPane().add(new BorrowPanel(reader, new DBOperation()));
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}
	*/
}
