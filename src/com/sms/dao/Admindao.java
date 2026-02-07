package com.sms.dao;

import com.sms.util.DBconnection;
import com.sms.util.passwordutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Admindao {
    public void addadmin(String username,String password){
        String hashedpassword= passwordutil.hashpassword(password);
        try {
            Connection con= DBconnection.getconnection();
            String query="Insert into admin(username,password)values(?,?)";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,hashedpassword);
            ps.executeUpdate();
            con.close();
            System.out.println("Admin added succesfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public boolean login(String username,String password)throws Exception{
        Connection con= DBconnection.getconnection();
        String query="select*from admin where username=? and password=?";
        PreparedStatement ps=con.prepareStatement(query);
        String hashpassword=passwordutil.hashpassword(password);
        ps.setString(1,username);
        ps.setString(2,hashpassword);
        ResultSet rs=ps.executeQuery();
        boolean status=rs.next();
        con.close();
        return  status;
    }

    public boolean resetpassword(String username,String newpassword) throws  Exception{
        Connection con=DBconnection.getconnection();
        //check the user exist or not
        String query1="select*from admin where username=?";
        PreparedStatement ps1=con.prepareStatement(query1);
        ps1.setString(1,username);
        ResultSet rs1=ps1.executeQuery();
        if(!rs1.next()){
            return false;//student not exist in the database
        }
        //if the user exist in database reset the pasword
        String query2="update admin set password=? where username=?";
        PreparedStatement ps2=con.prepareStatement(query2);
        String hashpassword=passwordutil.hashpassword(newpassword);
        ps2.setString(1,hashpassword);
        ps2.setString(2,username);
        ps2.executeUpdate();
        con.close();
        return true;

    }
    public boolean blockedstudent(int student_id){
        boolean status=false;
        try {
            Connection con=DBconnection.getconnection();
            String query="Update students set is_blocked=1 where student_id=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1,student_id);
            int rows=ps.executeUpdate();
            if(rows>0){
                status=true;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  status;
    }
    public boolean unblockedstudent(int student_id){
        boolean status=false;
        try {
            Connection con=DBconnection.getconnection();
            String query="Update students set is_blocked=0 where student_id=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1,student_id);
            int rows=ps.executeUpdate();
            if(rows>0){
                status=true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }
    public void showdashboard()throws  Exception{
        Connection con=DBconnection.getconnection();
        String totalBookquery="select count(*) from books";//count of books
        PreparedStatement ps1=con.prepareStatement(totalBookquery);
        ResultSet rs1=ps1.executeQuery();
        rs1.next();
        int totalbookquery=rs1.getInt(1);
        String issuedBookquery="select count(*) from issue_books where return_date is null";//count for issue books
        PreparedStatement ps2=con.prepareStatement(issuedBookquery);
        ResultSet rs2=ps2.executeQuery();
        rs2.next();
        int issuedBooks=rs2.getInt(1);
        String totalstudentquery="select count(*) from students";
        PreparedStatement ps3=con.prepareStatement(totalstudentquery);//total students
        ResultSet rs3=ps3.executeQuery();
        rs3.next();
        int totalstudents=rs3.getInt(1);


        String totalfinequery="select sum(fine) from issue_books";
        PreparedStatement ps5=con.prepareStatement(totalfinequery);
        ResultSet rs5=ps5.executeQuery();
        rs5.next();
        int totalFine=rs5.getInt(1);
        System.out.println("Total Books       :"+totalbookquery);
        System.out.println("Issued Books      :"+issuedBooks);
        System.out.println("Total Students    :"+totalstudents);
        System.out.println("Total Fine        :"+totalFine);
        System.out.println("================================\n");
        con.close();


    }
    public void viewIssuedHistorybyadmin()throws Exception{
        Connection con=DBconnection.getconnection();
        String query="select i.issue_id,b.title,s.name,i.issue_date,i.return_date,i.fine  from issue_books i join books b  on i.book_id=b.book_id join students s on i.student_id=s.student_id order by i.issue_date desc";
        PreparedStatement ps=con.prepareStatement(query);
        ResultSet rs=ps.executeQuery();
        System.out.println("\n======ISSUE HISTORY===========");
        System.out.printf("%-5s %-20s %-15s %-12s %-12s %-6s %-10s\n","ID","Book","Student","IssueDate","ReturnDate","Fine","status");
        System.out.println("------------------------------------------------------------------------------------");
        while(rs.next()){
            int issueId=rs.getInt("issue_id");
            String bookName=rs.getString("title");
            String studentName=rs.getString("name");
            String issueDate=rs.getString("issue_date");
            String returnDate=rs.getString("return_date");
            int fine=rs.getInt("fine");
            String status=(returnDate==null)?"ISSUED":"RETURNED";
            System.out.printf("%-5d %-20s %-15s %12s %-12s %-6d %-10s\n",issueId,bookName,
                    studentName,issueDate,(returnDate==null?"----":returnDate),fine,status);
        }
        con.close();
    }





}
