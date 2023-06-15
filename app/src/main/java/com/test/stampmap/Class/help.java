package com.test.stampmap.Class;

public class help {
    private String title, description;

    public help(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setTitle(String name){
        title = name;
    }

    public void setDescription(String text){
        description = text;
    }
}
