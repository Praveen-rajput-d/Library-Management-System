package com.sms.util;

import java.security.MessageDigest;

public class passwordutil {
    public static String hashpassword(String password){
        try {
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte []bytes= md.digest(password.getBytes());
            StringBuilder sb=new StringBuilder();
            for(byte b:bytes){
                sb.append(String.format("%02x",b));
            }return  sb.toString();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
