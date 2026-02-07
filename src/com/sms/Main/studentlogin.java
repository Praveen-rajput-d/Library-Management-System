package com.sms.Main;

import com.sms.dao.Studentdao;
import com.sms.util.Emailvalidator;

import java.util.Scanner;

public class studentlogin {
    public static void login()throws  Exception{
        Scanner sc=new Scanner(System.in);
        Studentdao studentdao=new Studentdao();
        StudentDashboard studentDashboard=new StudentDashboard();
        System.out.println("===== STUDENT LOGIN =====");
        System.out.println("1.Login");
        System.out.println("2.Forgot Password");
        System.out.println("3.Edit Email");
        System.out.println("4.Exit");
        System.out.println("Enter choice to Login/Forgot Password:");
        int choice=sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                System.out.println("Student ID:");
                int studentId=sc.nextInt();
                System.out.println("Email:");
                String email=sc.next();
                System.out.println("Password:");
                String password=sc.next();
                if(!studentdao.studentLogin(studentId,email,password)){
                    System.out.println("Invalid Details");
                    studentlogin.login();
                    return;
                }
                System.out.println("Login Successfully");
                studentdao.showdashboard(studentId);
                studentDashboard.studentmenu(studentId);
                break;
            case 2:
                //forgot password
                System.out.println("Enter username:");
                String username=sc.nextLine();
                System.out.println("Enter new password:");
                String newpassword=sc.nextLine();
                System.out.println("Confirm password:");
                String confirmpassword=sc.nextLine();
                if(!newpassword.equals(confirmpassword)){
                    System.out.println("Password do not match");
                }else{
                    boolean status=studentdao.resetpassword(username,newpassword);
                    if(status){
                        System.out.println("Password reset succesfully");
                    }else{
                        System.out.println("user not found");
                    }
                }
                studentlogin.login();
                break;
            case 3:
                System.out.println("Enter the Student Id:");
                int studentid=sc.nextInt();
                sc.nextLine();
                System.out.println("Enter student Id to update Email:");
                String newemail=sc.nextLine();
                if(!Emailvalidator.isvalidEmail(newemail)){
                    System.out.println("Invalid Email");
                    return;
                }
                studentdao.EditEmail(studentid,newemail);
                System.out.println("Email Update successfully");
                studentlogin.login();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice");


        }

    }
}
