package com.example.customauthenticationfirebase;

public class BookBorrower extends Books{

    public String bookTitle, bookAuthor, bookPublisher, bookYear, borrowerName, borrowerEmail,
    dateBorrower, borrowerKey;

    public BookBorrower(){

    }

    public BookBorrower(String Title, String Author, String Publisher, String Year, String Name,
                        String Email, String date, String key){
        this.bookTitle = Title;
        this.bookAuthor = Author;
        this.bookPublisher = Publisher;
        this.bookYear = Year;
        this.borrowerName = Name;
        this.borrowerEmail = Email;
        this.dateBorrower = date;
        this.borrowerKey = key;
    }

    public void setBorrowerKey(String borrowerKey){
        this.borrowerKey = borrowerKey;
    }

    public String getBorrowerKey(){
        return borrowerKey;
    }

    public String getBookTitle(){
        return bookTitle;
    }

    public void setBookTitle(String bookTitle){
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor(){
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor){
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher(){
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher){
        this.bookPublisher = bookPublisher;
    }

    public String getBookYear(){
        return bookYear;
    }

    public void setBookYear(String bookYear){
        this.bookYear = bookYear;
    }

    public String getBorrowerName(){
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName){
        this.borrowerName = borrowerName;
    }

    public String getBorrowerEmail(){
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail){
        this.borrowerEmail = borrowerEmail;
    }

    public String getDateBorrower(){
        return dateBorrower;
    }

    public void setDateBorrower(String dateBorrower){
        this.dateBorrower = dateBorrower;
    }

}
