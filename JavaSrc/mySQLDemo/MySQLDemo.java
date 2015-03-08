package mySQLDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class MySQLDemo {
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	
	private void getConnect() {
		String url = "jdbc:mysql://210.140.168.112:3306/ohd";
		String userName = "ohd";
		String passWord = "ohd";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, userName, passWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void dataUpload() {
		String sqlStr = "insert into photo (data) values (?)";
		try {
			//File fi = new File("C:\\Users\\DdsSmiLe\\Desktop\\abc_small.jpg");
			File fi = new File("/tmp/tmp.jpg");
			byte[] fileContent = Files.readAllBytes(fi.toPath());
			InputStream is = new FileInputStream(fi);
			
			preparedStatement = connect.prepareStatement(sqlStr);
			preparedStatement.setBinaryStream(1, is, fileContent.length);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnect() {
		try {
			if (connect != null)
				connect.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MySQLDemo mysqlDemo = new MySQLDemo();
		mysqlDemo.getConnect();
		mysqlDemo.dataUpload();
		mysqlDemo.closeConnect();
		System.out.println("Pic uploaded!!");
	}
}