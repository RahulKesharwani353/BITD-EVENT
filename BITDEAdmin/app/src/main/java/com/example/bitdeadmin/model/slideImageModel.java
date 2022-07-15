package com.example.bitdeadmin.model;

public class slideImageModel {
    String slideImgLink,slideImgKey;

    public String getSlideImgLink() {
        return slideImgLink;
    }

    public void setSlideImgLink(String slideImgLink) {
        this.slideImgLink = slideImgLink;
    }

    public String getSlideImgKey() {
        return slideImgKey;
    }

    public void setSlideImgKey(String slideImgKey) {
        this.slideImgKey = slideImgKey;
    }

    public slideImageModel() {
    }

    public slideImageModel(String slideImgLink, String slideImgKey) {
        this.slideImgLink = slideImgLink;
        this.slideImgKey = slideImgKey;
    }
}
