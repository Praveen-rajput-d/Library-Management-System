package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBconnection;
import com.sms.util.Emailvalidator;
import com.sms.util.passwordutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Studentdao {
    public void addstudent(Student student){
        if(!Emailvalidator.isvalidEmail(student.getEmail())){
            System.out.println("Invalid email");
            return;
        }

        try {
            Connection con= DBconnection.getconnection();
            String query="insert into students(name,email,password)values(?,?,?)";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,student.getName());
            ps.setString(2,student.getEmail());
            String hashedpassword= passwordutil.hashpassword(student.getPassword());
            ps.setString(3,hashedpassword);
            ps.executeUpdate();
            con.close();
            System.out.println("Student added successfully");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void viewstudents()throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="select*from students where is_blocked=0";
        PreparedStatement ps=con.prepareStatement(query);
        ResultSet resultSet=ps.executeQuery();
        System.out.println("\nstudent_id | name | Email");
        while(resultSet.next()){
            System.out.println(resultSet.getInt(1)+" | "+
                    resultSet.getString(2)+" | "+
                    resultSet.getString(3)
            );
        }
        con.close();

    }
    public boolean studentLogin(int studentId,String email,String password)throws Exception{
        Connection con=DBconnection.getconnection();
        String hashedpassword=passwordutil.hashpassword(password);
        String query="select*from students where student_id=? and email=? and password=? and is_blocked=0";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentId);
        ps.setString(2,email);
        ps.setString(3,hashedpassword);
        ResultSet rs=ps.executeQuery();
        boolean status=rs.next();
        con.close();
        return status;
    }
    public boolean resetpassword(String name,String newpassword) throws  Exception{
        Connection con=DBconnection.getconnection();
        //check the student exist or not
        String query1="select*from students where name=?";
        PreparedStatement ps1=con.prepareStatement(query1);
        ps1.setString(1,name);
        ResultSet rs1=ps1.executeQuery();
        if(!rs1.next()){
            return false;//student not exist in the database
        }
        //if the student exist in database reset the pasword
        String query2="update students set password=? where name=?";
        PreparedStatement ps2=con.prepareStatement(query2);
        String hashpassword=passwordutil.hashpassword(newpassword);
        ps2.setString(1,hashpassword);
        ps2.setString(2,name);
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
    public void searchbook(String keyword){
        try {
            Connection con=DBconnection.getconnection();
            String query="select*from books where (title like ? or author like ?) isactive=1";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,"%"+keyword+"%");
            ps.setString(2,"%"+keyword+"%");
            ResultSet rs=ps.executeQuery();
            System.out.println("Book_ID | Title | Author | Quantity");
            while(rs.next()){
                System.out.println(
                        rs.getInt("book_id")+" | "+
                                rs.getString("title")+" | "+
                                rs.getString("author")+" | "+
                                rs.getInt("quantity")
                );
            }
            con.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public boolean EditEmail(int student_id,String newEmail) throws Exception{
        Connection con=DBconnection.getconnection();
        String query="update students set email=? where student_id=? and is_blocked=0";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setString(1,newEmail);
        ps.setInt(2,student_id);
        int rows=ps.executeUpdate();
        return rows>0;
    }
    public void showdashboard(int studentid)throws  Exception{
        Connection con=DBconnection.getconnection();
        //total books issued
        String totalbookissued="select count(*) from issue_books where student_id=?";
        PreparedStatement ps1=con.prepareStatement(totalbookissued);
        ps1.setInt(1,studentid);
        ResultSet rs=ps1.executeQuery();
        int totalissued=0;
        if( rs.next()){
            totalissued=rs.getInt(1);
        }
        //currently issued book(not returned
        String currentIssuedquery="select count(*) from issue_books where student_id=? and return_date is null";
        PreparedStatement ps2=con.prepareStatement(currentIssuedquery);
        ps2.setInt(1,studentid);
        ResultSet rs2=ps2.executeQuery();
        int currentIssued=0;
        if(rs2.next()){
            currentIssued=rs2.getInt(1);
        }
        //Return Books
        int returnBooks=totalissued-currentIssued;
        //Total fine
        String finequery="select sum(fine) from issue_books where student_id=?";
        PreparedStatement ps3=con.prepareStatement(finequery);
        ps3.setInt(1,studentid);
        ResultSet rs3=ps3.executeQuery();
        int totalfine=0;
        if(rs3.next()){
            totalfine=rs3.getInt(1);
        }
        //Display Dashboard
        System.out.println("\n======STUDENT DASHBOARD====");
        System.out.println("Total Books Issued:"+totalissued);
        System.out.println("currently Issued Books:"+currentIssued);
        System.out.println("Returned Books:"+returnBooks);
        System.out.println("Total Fine:"+totalfine);
        System.out.println("============================");
        con.close();

    }
}
