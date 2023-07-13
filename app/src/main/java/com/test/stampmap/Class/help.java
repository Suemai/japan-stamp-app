package com.test.stampmap.Class;

public class help {
    private String title, description;
    private boolean expanded;

    public help(String title, String description){
        this.title = title;
        this.description = description;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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
