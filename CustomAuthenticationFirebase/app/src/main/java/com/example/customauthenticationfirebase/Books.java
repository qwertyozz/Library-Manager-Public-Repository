package com.example.customauthenticationfirebase;

public class Books{

    private String title, author, publisher, year, genre, key;

    public Books(){

    }

    public Books(String title, String author, String publisher, String year, String genre){
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public String getPublisher(){
        return publisher;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getYear(){
        return year;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }

}
