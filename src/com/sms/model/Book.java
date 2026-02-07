package com.sms.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private int quantity;
    public Book(){}
    public Book(String title,String author,int quantity){
        this.title=title;
        this.author=author;
        this.quantity=quantity;
    }
    //getter and setter method
    public int getBookId(){
        return bookId;
    }
    public void setBookId(int bookId){
        this.bookId=bookId;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
}
