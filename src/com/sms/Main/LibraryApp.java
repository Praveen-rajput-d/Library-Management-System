package com.sms.Main;

import com.sms.dao.Admindao;
import com.sms.dao.Bookdao;
import com.sms.dao.IssueBookdao;
import com.sms.dao.Studentdao;

import java.util.Scanner;

public class LibraryApp {
    public static void main(String args[])throws  Exception{
        Scanner sc=new Scanner(System.in);
        Bookdao bookdao=new Bookdao();
        Studentdao studentdao=new Studentdao();
        IssueBookdao issueBookdao=new IssueBookdao();
        System.out.println("======LIBRARY MANAGEMENT SYSTEM=======");
        while(true){
            System.out.println("1.Admin Login\n2.Student Login\n3.Add Admin\n4.Exit");
            System.out.println("Enter choice:");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    Adminlogin.login();
                    break;


                case 2:
                    studentlogin.login();
                    break;
                case 3:
                    sc.nextLine();
                    System.out.println("Enter admin username:");
                    String username=sc.nextLine();
                    System.out.println("Enter admin password:");
                    String password=sc.nextLine();
                    Admindao admindao=new Admindao();
                    admindao.addadmin(username,password);
                    break;
                case 4:
                    System.exit(0);
            }
        }


    }
}
