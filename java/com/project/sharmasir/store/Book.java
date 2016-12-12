package com.project.sharmasir.store;

import java.io.Serializable;

/**
 * Created by Souradeep on 11/8/2016.
 */

public class Book implements Serializable{
    String name,isbn;
    String author,publisher,edition,category,url;
    float size;

    Book(String name,String isbn,String author, String publisher, String edition, String category, String url, float size){
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.category = category;
        this.url = url;
        this.size = size;
        this.isbn = isbn;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public String getPublisher(){
        return publisher;
    }

    public void setEdition(String edition){
        this.edition = edition;
    }

    public String getEdition(){
        return edition;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setSize(float size){
        this.size = size;
    }

    public float getSize(){
        return size;
    }

    @Override
    public String toString() {
        return "["+name+","+author+"]";
    }
}
