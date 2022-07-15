package com.example.bitdeadmin.model;

public class EventModel {
    private  String title,Organisation,category,date,description,
    guest,img,openFor,time,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventModel() {
    }

    public EventModel(String title, String organisation, String category, String date, String description, String guest, String img, String openFor, String time, String id) {
        this.title = title;
        Organisation = organisation;
        this.category = category;
        this.date = date;
        this.description = description;
        this.guest = guest;
        this.img = img;
        this.openFor = openFor;
        this.time = time;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganisation() {
        return Organisation;
    }

    public void setOrganisation(String organisation) {
        Organisation = organisation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOpenFor() {
        return openFor;
    }

    public void setOpenFor(String openFor) {
        this.openFor = openFor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
