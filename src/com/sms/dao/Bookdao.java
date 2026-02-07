package com.sms.dao;

import com.sms.model.Book;
import com.sms.util.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Bookdao {
    public void addBook(Book book)throws Exception{
        Connection con= DBconnection.getconnection();
        String query="insert into books(title,author,quantity)values(?,?,?)";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setString(1,book.getTitle());
        ps.setString(2,book.getAuthor());
        ps.setInt(3,book.getQuantity());
        ps.executeUpdate();
        con.close();
        System.out.println("Book added succesfully");
    }
    public void viewBooks()throws Exception{
        Connection con=DBconnection.getconnection();
        String query="select*from books where isactive=1";
        PreparedStatement ps=con.prepareStatement(query);
        ResultSet resultSet=ps.executeQuery();
        System.out.println("\nID | Title | Author | quantity");
        while(resultSet.next()){
            System.out.println(
                    resultSet.getInt(1)+" | "+
                            resultSet.getString(2)+" | "+
                            resultSet.getString(3)+" | "+
                            resultSet.getInt(4)

            );
        }
        con.close();
    }
    public boolean deleteBook(int book_id)throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="update books set isactive=0 where book_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,book_id);
        int rows=ps.executeUpdate();
        con.close();
        return rows>0;
    }
    public void searchbook(String keyword){
        try {
            Connection con=DBconnection.getconnection();
            String query="select*from books where (title like ? or author like ?) ";
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
    public boolean blockedBook(int bookId)throws  Exception{
        Connection con=DBconnection.getconnection();
        boolean status=false;
        String query="update books set isactive=0 where book_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,bookId);
        int rows=ps.executeUpdate();
        if(rows>0){
            status=true;
        }
        return status;
    }
    public boolean unblockedBook(int bookId)throws  Exception{
        Connection con=DBconnection.getconnection();
        boolean status=false;
        String query="update books set isactive=1 where book_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,bookId);
        int rows=ps.executeUpdate();
        if(rows>0){
            status=true;
        }
        return status;
    }


}
