package com.sms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
    private  static final String url="jdbc:mysql://localhost:3306/librarymanagement";
    private  static final String username="root";
    private  static final String password="12345678";
    public static Connection getconnection(){
        Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(url,username,password);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return con;
    }
}
