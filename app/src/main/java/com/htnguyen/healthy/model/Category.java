package com.htnguyen.healthy.model;

import java.util.List;


public class Category{
    private String cateId;
    private String title;
    private String description;
    private String nameValue;
    private List<Items> itemsList;

    public Category() {
        //Required emty contructor
    }

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Category(String title, String description, String nameValue, List<Items> itemsList) {
        this.title = title;
        this.description = description;
        this.nameValue = nameValue;
        this.itemsList = itemsList;
    }

    public Category(String cateId, String title, String description, String nameValue, List<Items> itemsList) {
        this.cateId = cateId;
        this.title = title;
        this.description = description;
        this.nameValue = nameValue;
        this.itemsList = itemsList;
    }

    public Category(String title, String description, String nameValue) {
        this.title = title;
        this.description = description;
        this.nameValue = nameValue;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public List<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
