package com.sms.Main;

import com.sms.dao.Admindao;
import com.sms.dao.Bookdao;
import com.sms.dao.IssueBookdao;
import com.sms.dao.Studentdao;
import com.sms.model.Book;
import com.sms.model.Student;
import com.sms.util.Emailvalidator;

import java.util.Scanner;

public class AdminDashboard {
    public static void adminMenu() throws  Exception{
        Scanner sc=new Scanner(System.in);
        Bookdao bookdao=new Bookdao();
        Studentdao studentdao =new Studentdao();
        IssueBookdao issueBookdao=new IssueBookdao();
        Admindao admindao=new Admindao();

        while(true){
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1.Add Books");
            System.out.println("2.view All Books");
            System.out.println("3.Block Books");
            System.out.println("4.Unblock Books");
            System.out.println("5.Add Student");
            System.out.println("6.View All Student");
            System.out.println("7.Block Students");
            System.out.println("8.Unblock Students");
            System.out.println("9.Issued Books");
            System.out.println("10.View issued Books");
            System.out.println("11.Return Book");
            System.out.println("12. View Total Fine collected");
            System.out.println("13.Search Books");
            System.out.println("14.View Total Books Issued to students");
            System.out.println("15.Logout");
            System.out.println("Enter choice:");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    sc.nextLine();
                    System.out.println("Title:");
                    String title=sc.nextLine();
                    System.out.println("Author:");
                    String author=sc.nextLine();
                    System.out.println("quantity:");
                    int quantity=sc.nextInt();
                    bookdao.addBook(new Book(title,author,quantity));
                    break;
                case 2:
                    bookdao.viewBooks();
                    break;
                case 3:
                    System.out.println("Enter Book Id to Block Book:");
                    int bookid=sc.nextInt();
                    if(bookdao.blockedBook(bookid)){
                        System.out.println("Book Blocked Successfully");
                    }else{
                        System.out.println("Invalid Book Id");
                    }
                    adminMenu();
                    break;
                case 4:
                    System.out.println("Enter Book Id to Unblock Book:");
                    int ubookid=sc.nextInt();
                    if(bookdao.unblockedBook(ubookid)){
                        System.out.println("Book Unblock successfully");
                    }else{
                        System.out.println("Invalid Book Id");
                    }
                    adminMenu();
                    break;
                case 5:
                    sc.nextLine();
                    System.out.println("Name:");
                    String name=sc.nextLine();
                    System.out.println("Email:");
                    String email=sc.nextLine();
                    if(!Emailvalidator.isvalidEmail(email)){
                        System.out.println("Invalid Email");
                        return;
                    }
                    System.out.println("Password:");
                    String password=sc.next();

                    studentdao.addstudent(new Student(name,email,password));
                    break;
                case 6:
                    studentdao.viewstudents();
                    break;
                case 7:
                    System.out.println("Enter StudentId to Block:");
                    sc.nextLine();
                    int studentId=sc.nextInt();
                    if( studentdao.blockedstudent(studentId)){
                        System.out.println("Student Blocked Succesfully");
                    }else{
                        System.out.println("Invalid Student Id");
                    }
                    adminMenu();
                    break;
                case 8:
                    System.out.println("Enter StudentId to Unblock:");
                    sc.nextLine();
                    int studentid=sc.nextInt();
                    if(studentdao.unblockedstudent(studentid)){
                        System.out.println("Unblock Student Successfully");
                    }else{
                        System.out.println("Invalid Studnet Id");
                    }
                    adminMenu();
                    break;
                case 9:
                    System.out.println("Book Id:");
                    int bid=sc.nextInt();
                    System.out.println("student Id:");
                    int sid=sc.nextInt();
                    issueBookdao.issueBook(bid,sid);
                    break;

                case 10:
                    issueBookdao.viewissuebook();
                    break;
                case 11:
                    System.out.println("Enter IssueId:");
                    int issueId=sc.nextInt();
                    issueBookdao.returnBookWithfine(issueId);
                    break;
                case  12:
                    issueBookdao.viewTotalfine();
                    break;
                case 13:
                    System.out.println("Enter keyword to search a book:");
                    sc.nextLine();
                    String keyword=sc.nextLine();
                    bookdao.searchbook(keyword);
                    break;
                case 14:
                    admindao.viewIssuedHistorybyadmin();
                    break;
                case 15:
                    System.out.println("Admin logged out:");
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }
    }
}
