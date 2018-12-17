package org.bran.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBOperation {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public DBOperation(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url="jdbc:mysql://127.0.0.1:3306/library?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
			String user="root";
			String password="jj062416";
			connection=DriverManager.getConnection(url,user,password);
		}catch(ClassNotFoundException e){
			System.out.println("数据库加载出错");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据库连接出错");
			e.printStackTrace();
		}
	}
	
	public int executeUpdate(String sql) throws SQLException{
		statement=connection.createStatement();
		return statement.executeUpdate(sql);
	}

	public ResultSet executeQuery(String sql) throws SQLException{
		statement=connection.createStatement();
		return statement.executeQuery(sql);
	}
	

	public PreparedStatement getPreparedStatement(String sql) throws SQLException{
		return connection.prepareStatement(sql);
	}
	
	public CallableStatement getCallableStatement(String sql) throws SQLException{
		return connection.prepareCall(sql);
	}

	public void close() throws SQLException{
		if(resultSet!=null){
			resultSet.close();
		}
		if(statement!=null){
			statement.close();
		}
		connection.close();
	}
}
