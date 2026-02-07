package com.sms.Main;

import com.sms.dao.Admindao;

import java.util.Scanner;

public class Adminlogin {
    public static void login()throws  Exception{
        Scanner sc=new Scanner(System.in);
        Admindao admindao=new Admindao();
        AdminDashboard adminDashboard=new AdminDashboard();
        LibraryApp libraryApp=new LibraryApp();
        System.out.println("\n====ADMIN LOGIN====");
        System.out.println("1.Login");
        System.out.println("2.Reset Password");
        System.out.println("3.Exit");
        System.out.println("Enter choice to Login/Reset Password:");
        int choice=sc.nextInt();
        sc.nextLine();
        switch (choice){
            case 1:
                System.out.println("Username:");
                String username=sc.next();
                System.out.println("Password:");
                String password=sc.next();
                if(!admindao.login(username,password)) {
                    System.out.println("Invalid User Details");
                    Adminlogin.login();
                }
                System.out.println("Login Successfull");
                admindao.showdashboard();
                adminDashboard.adminMenu();
                break;
            case 2:
                sc.nextLine();
                System.out.println("Enter username:");
                String  usernames=sc.nextLine();
                System.out.println("Enter new Password:");
                String newpassword=sc.nextLine();
                System.out.println("Enter confirm password:");
                String confirmpassword=sc.nextLine();
                if(!newpassword.equals(confirmpassword)){
                    System.out.println("Password not match");
                }else{
                    boolean status= admindao.resetpassword(usernames,newpassword);
                    if(status){
                        System.out.println("Password reset successfully");
                    }else{
                        System.out.println("user not found");
                    }
                }
                Adminlogin.login();
                break;
            case 3:
                return;

            default:
                System.out.println("invalid choice");


        }

    }
}
