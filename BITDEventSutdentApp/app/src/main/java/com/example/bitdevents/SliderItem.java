package com.example.bitdevents;

public class SliderItem {
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

    public SliderItem() {
    }

    public SliderItem(String slideImgLink, String slideImgKey) {
        this.slideImgLink = slideImgLink;
        this.slideImgKey = slideImgKey;
    }
}
