package com.test.stampmap.Class;

public class help {
    private final int titleId;
    private final int descriptionId;
    private boolean expanded;

    public help(int titleResourceId, int descriptionResourceId){
        this.titleId = titleResourceId;
        this.descriptionId = descriptionResourceId;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getTitleResourceId(){
        return this.titleId;
    }
    public int getDescriptionResourceId(){
        return this.descriptionId;
    }
}
