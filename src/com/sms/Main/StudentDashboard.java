package com.sms.Main;

import com.sms.dao.Bookdao;
import com.sms.dao.IssueBookdao;

import java.util.Scanner;

public class StudentDashboard {
    public static void studentmenu(int studentId)throws Exception{
        Scanner sc=new Scanner(System.in);
        IssueBookdao issueBookdao=new IssueBookdao();
        Bookdao bookdao=new Bookdao();

        while (true){
            System.out.println("\n====== STUDENT DASHBOARD =====");
            System.out.println("1. View my Issued Books");
            System.out.println("2. View My Total fine");
            System.out.println("3.Search Books");
            System.out.println("4.View My Issue History");
            System.out.println("5. Logout");
            System.out.println("Enter choice:");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    issueBookdao.viewIssueBooksByStudent(studentId);
                    break;
                case 2:
                    issueBookdao.viewStudentFine(studentId);
                    break;
                case 3:
                    sc.nextLine();
                    System.out.println("Enter keyword to search books:");
                    String keyword=sc.nextLine();
                    bookdao.searchbook(keyword);
                    break;
                case 4:
                    issueBookdao.viewIssueBooksByStudent(studentId);
                    break;

                case 5:
                    System.out.println("student logged out");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
