package org.bransummer.db_coursedesign_libaray;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bran.db.DBOperation;
/**
 *Description:根据课程设计实验要求，本类实现 读者基本信息的输入
 */
public class ReaderRegistFrame extends JFrame {
	//姓名，地址，电话，邮件
	private JTextField name,address,tele,email,deptId;
	//备注
	private JTextArea remarks;
	//读者种类
	private JComboBox<String> sort;
	//密码
	private JPasswordField pwd1,pwd2;
	//性别
	private JRadioButton male,female;
	//注册，取消按钮
	private JButton submit,cancel;
	
	 ReaderRegistFrame(final DBOperation db){
		super("注册读者号");
		this.setSize(400, 400);
		this.setLocationRelativeTo(getOwner());
		Container container=this.getContentPane();
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridLayout(8,2));
		//姓名
		panel1.add(new JLabel("姓名",JLabel.CENTER));
		name=new JTextField(20);
		panel1.add(name);
		//密码
		panel1.add(new JLabel("密码",JLabel.CENTER));
		pwd1=new JPasswordField(20);
		panel1.add(pwd1);
		panel1.add(new JLabel("再次输入密码",JLabel.CENTER));
		pwd2=new JPasswordField(20);
		panel1.add(pwd2);
		//性别
		panel1.add(new JLabel("性别",JLabel.CENTER));
		JPanel sexpanel=new JPanel(new GridLayout(1, 2));
		male=new JRadioButton("男",true);
		female=new JRadioButton("女");
		final ButtonGroup group=new ButtonGroup();
		group.add(male);
		group.add(female);
		sexpanel.add(male);
		sexpanel.add(female);
		panel1.add(sexpanel);
		//类别
		sort=new JComboBox<String>();
		sort.addItem("学生");
		sort.addItem("教师");
		sort.addItem("其他");
		panel1.add(new JLabel("类别",JLabel.CENTER));
		panel1.add(sort);
		//工作单位
		panel1.add(new JLabel("系号",JLabel.CENTER));
		deptId=new JTextField(20);
		panel1.add(deptId);
		//家庭住址
//		panel1.add(new JLabel("家庭住址",JLabel.CENTER));
//		address=new JTextField(20);
//		panel1.add(address);
		//电话号码
		panel1.add(new JLabel("电话号码",JLabel.CENTER));
		tele=new JTextField(20);
		panel1.add(tele);
		//邮件地址
		panel1.add(new JLabel("电子邮件",JLabel.CENTER));
		email=new JTextField(20);
		panel1.add(email);
		//备注
		JPanel panel2=new JPanel(new GridLayout(1, 2));
		panel2.add(new JLabel("备注",JLabel.CENTER));
		remarks=new JTextArea(5,20);
		JScrollPane scroll=new JScrollPane(remarks);
		
		panel2.add(scroll);
		//注册，取消按钮
		JPanel panel3=new JPanel(new GridLayout(1, 2));
		submit=new JButton("提交");
		/**
		 * 【提交】注册监听器
		 */
		submit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String nameText=name.getText();
				if(!pwd1.getText().equals(pwd2.getText())){
					JOptionPane.showMessageDialog(null, "两次密码不一致！","failure", JOptionPane.OK_CANCEL_OPTION);
				}else {
					String pwdText=pwd1.getText();
					String deptText=deptId.getText();
					String teleText=tele.getText();
					String emailText=email.getText();
					String sortText=sort.getSelectedItem().toString();
					String genderText="";
					if(male.isSelected()){
						genderText="男";
					}else genderText="女";
					String remarkText=remarks.getText();
					Date date=new Date();
					java.sql.Date sqlDate=new java.sql.Date(date.getTime());
					int readerId = 1;
					/**
					 * 调用存储过程
					 */

					try {
						String sql="{call pro_regist(?,?,?,?,?,?,?,?,?,?)} ";
						CallableStatement cStmt=db.getCallableStatement(sql);
						cStmt.setString(2, nameText);
						cStmt.setString(3, genderText);
						cStmt.setString(4, sortText);
						cStmt.setString(5, deptText);
						cStmt.setDate(6,sqlDate);
						cStmt.setString(7, remarkText);
						cStmt.setString(8, pwdText);
						cStmt.setString(9, teleText);
						cStmt.setString(10, emailText);
						cStmt.registerOutParameter(1,Types.INTEGER);

						boolean isSuccess=cStmt.execute();
						readerId =cStmt.getInt(1);
						String message="注册成功\n "+nameText+",你的读者号为"+readerId+"\n 请妥善保管信息！ ";
						JOptionPane.showMessageDialog(null, message);
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		cancel=new JButton("取消");
		/**
		 * 【取消】注册监听器
		 */
		cancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
		});
		panel3.add(submit);
		panel3.add(cancel);

		container.add(panel1);
		container.add(panel2);
		container.add(panel3);
		this.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
				
			}
			
			public void windowIconified(WindowEvent e) {
				
			}
			
			public void windowDeiconified(WindowEvent e) {

			}
			
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
			
			public void windowClosed(WindowEvent e) {
				
			}
			
			public void windowActivated(WindowEvent e) {

			}
		});
		this.setVisible(false);
	}
	
}
