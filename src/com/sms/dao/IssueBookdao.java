package com.sms.dao;

import com.sms.util.DBconnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueBookdao {
    public static  final int MAX_BOOK_LIMIT=3;
    public void  issueBook(int bookId,int studentId)throws  Exception{

        Connection con= DBconnection.getconnection();
        if(!canIssueBook(studentId)){
            return;
        }
        String query1="insert into issue_books(book_id,student_id,issue_date)values(?,?,?)";
        PreparedStatement ps1=con.prepareStatement(query1);
        ps1.setInt(1,bookId);
        if(isBookBlocked(bookId)){
            System.out.println("Book is blocked cannot issue book");
            return;
        }
        ps1.setInt(2,studentId);
        if(isStudentBlocked(studentId)){
            System.out.println("Student is blocked cannot issue book");
            return;
        }
        ps1.setDate(3, Date.valueOf(LocalDate.now()));
        ps1.executeUpdate();
        String query2="update books set quantity=quantity-1 where book_id=?";//decrease book count
        PreparedStatement ps2=con.prepareStatement(query2);
        ps2.setInt(1,bookId);
        ps2.executeUpdate();
        //increase  issue book count
        String  countupdate="update students set issue_coutn=issue_coutn+1 where student_id=?";
        PreparedStatement pscount=con.prepareStatement(countupdate);
        pscount.setInt(1,studentId);
        pscount.executeUpdate();
        con.close();
        System.out.println("Book issued successfully");
    }
    public void returnBook(int issueId){
        try {
            Connection con=DBconnection.getconnection();
            String query="update issue_books set return_date=? where issue_id=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setDate(1,Date.valueOf(LocalDate.now()));
            ps.setInt(2,issueId);
            ps.executeUpdate();
            con.close();
            System.out.println("Book Returned succesfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void returnBookWithfine(int issueId) throws  Exception{
        Connection con=DBconnection.getconnection();
        PreparedStatement ps1=con.prepareStatement("select issue_date,book_id,student_id from issue_books where issue_id=?");
        ps1.setInt(1,issueId);
        ResultSet res=ps1.executeQuery();
        if(!res.next()){
            System.out.println("invalid issue id");
            return;
        }
        Date issueDate=res.getDate("issue_date");
        int bookId=res.getInt("book_id");
        int studentId=res.getInt("student_id");
        LocalDate issue=issueDate.toLocalDate();
        LocalDate today=LocalDate.now();
        Long daysBetween= ChronoUnit.DAYS.between(issue,today);
        int allowdDays=7;
        int fineperday=5;
        int fine=0;
        if(daysBetween>allowdDays){
            fine=(int)(daysBetween-allowdDays)*fineperday;
        }
        //update return date and fine
        PreparedStatement ps2=con.prepareStatement("Update issue_books set return_date=? ,fine=? where issue_id=?");
        ps2.setDate(1,Date.valueOf(today));
        ps2.setInt(2,fine);
        ps2.setInt(3,issueId);
        ps2.executeUpdate();
        //4 increase the book quantity
        PreparedStatement ps3=con.prepareStatement("Update books set quantity=quantity+1 where book_id=?");
        ps3.setInt(1,bookId);
        ps3.executeUpdate();
        //decrease issued_count
        PreparedStatement pissued_count=con.prepareStatement("Update students set issue_coutn=issue_coutn-1 where student_id=?");
        pissued_count.setInt(1,studentId);
        pissued_count.executeUpdate();
        con.close();
        System.out.println("Book returned successfully");
        System.out.println("Days used:"+daysBetween);
        if(fine>0){
            System.out.println("Fine stored:"+fine);
        }else{
            System.out.println("No Fine");
        }

    }
    public void viewissuebook()throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="SELECT i.issue_id, s.name, b.title, i.issue_date, i.return_date, i.fine "+
                "FROM issue_books i " +
                "JOIN students s ON i.student_id = s.student_id " +
                "JOIN books b ON i.book_id = b.book_id";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        System.out.println("\nIssueID | Student | Book | Issue Date | Return Date | Fine");
        while(rs.next()){
            System.out.println(
                    rs.getInt("issue_id")+ " | "+
                            rs.getString("name")+" | "+
                            rs.getString("title")+" | "+
                            rs.getDate("issue_date")+" | "+
                            rs.getDate("return_date")+" | "+
                            rs.getInt("fine")
            );
        }
        con.close();
    }
    public void viewTotalfine() throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="select sum(fine) as totalfine From issue_books";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        if(rs.next()){
            System.out.println("Total Fine Collected:"+rs.getInt("totalfine"));
        }
        con.close();

    }
    public void viewIssueBooksByStudent(int studentId)throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="select b.title,i.issue_date,i.return_date,i.fine " +
                "  from issue_books i join books b on i.book_id=b.book_id where i.student_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentId);
        ResultSet rs=ps.executeQuery();
        System.out.println("\nBook | Issue Date | Return Date | Fine");
        while(rs.next()){
            System.out.println(
                    rs.getString("title")+" | "+
                            rs.getDate("issue_date")+ " | "+
                            rs.getDate("return_date")+ " | "+
                            rs.getInt("fine")
            );
        }
        con.close();
    }
    public void viewStudentFine(int studentId)throws Exception{
        Connection con=DBconnection.getconnection();
        String query="select sum(fine) as totalFine from issue_books where student_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentId);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            System.out.println("your Total Fine:"+rs.getInt("totalfine"));
        }
        con.close();
    }
    public boolean isStudentBlocked(int studentId)throws Exception{
        boolean blocked=false;
        Connection con=DBconnection.getconnection();
        String query="Select is_blocked from students where student_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentId);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            blocked=rs.getInt("is_blocked")==1;
        }
        return blocked;
    }
    public boolean isBookBlocked(int bookId)throws  Exception{
        boolean blocked=false;
        Connection con=DBconnection.getconnection();
        String query="select isactive from books where book_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,bookId);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            blocked  =rs.getInt("isactive")==0;
        }
        return blocked;

    }
    public static boolean getissuecount(int studentId) throws  Exception{
        Connection con=DBconnection.getconnection();
        String query="select count(*) from issue_books"+ "where student_id=? and return_date is null";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentId);
        ResultSet rs=ps.executeQuery();
        int count=rs.getInt(1);
        con.close();
        return count>=3;
    }
    public static int getIssuedCount(int studentId) throws Exception{
        int count=0;
        Connection con=DBconnection.getconnection();
        PreparedStatement ps=con.prepareStatement("select issue_coutn from students where student_id=?");
        ps.setInt(1,studentId);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            count=rs.getInt("issue_coutn");
        }
        return count;
    }
    public static boolean canIssueBook(int studentId) throws  Exception{
        int issuedCount=getIssuedCount(studentId);
        if(issuedCount>=MAX_BOOK_LIMIT){
            System.out.println("Book Limit reached! Max allowed:"+MAX_BOOK_LIMIT);
            return false;
        }
        return true;
    }
    public void getissuehistoryofstudent(int studentid)throws Exception{
        Connection con=DBconnection.getconnection();
        String query="select b.title,i.issue_date,i.return_date"+
                "from issue_books i"+
                "join books b on i.book_id=b.book_id"+
                "where i.student_id=?"+
                "order by i.issue_date desc";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1,studentid);
        ResultSet rs=ps.executeQuery();
        System.out.println("\n=====My Issue History=====");
        System.out.printf("%-20s%-15s %-10s%n",
                "Book Name","issue Date","Return Date","Status");
        System.out.println("--------------------------------------");
        boolean found=false;
        while(rs.next()){
            found=true;
            String bookname=rs.getString("title");
            Date issueDate=rs.getDate("issue_date");
            Date returnDate=rs.getDate("return_date");
            String status=(returnDate==null)?"Issued":"Returned";
            String returntxt=(returnDate==null)?"Not Returned":returnDate.toString();
            System.out.printf("%-20s %-15s %-15s %-10s%n",
                    bookname,issueDate,returntxt,status);

        }
        if(!found){
            System.out.println("No Issue History Found");
        }
    }

}
