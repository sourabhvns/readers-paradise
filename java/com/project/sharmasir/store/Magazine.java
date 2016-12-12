package com.project.sharmasir.store;

import java.io.Serializable;

/**
 * Created by Souradeep on 12/3/2016.
 */

public class Magazine implements Serializable {
    String name,ismn;
    String publisher,edition,category,url;
    float size;

    Magazine(String name,String ismn,String publisher, String edition, String category, String url, float size){
        this.name = name;
        this.publisher = publisher;
        this.edition = edition;
        this.category = category;
        this.url = url;
        this.size = size;
        this.ismn = ismn;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setIsmn(String ismn){
        this.ismn = ismn;
    }

    public String getIsmn(){
        return ismn;
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
        return "["+name+","+publisher+"]";
    }
}
