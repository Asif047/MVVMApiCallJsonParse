package com.asif047.jsonparsingmvvm.apiUtils;

public class Image {

    public static final String SMALL_IMAGE_SIZE = "w300/";
    public static final String MEDIUM_IMAGE_SIZE = "w700/";
    public static final String BIG_IMAGE_SIZE = "w1280/";

    private String baseUrl = ServiceGenerator.IMAGE_BASE_URL;
    private String imageKey;


    public Image(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getLowQualityImagePath() {
        return baseUrl+SMALL_IMAGE_SIZE;
    }

    public String getMediumImageSizeQualityImagePath() {
        return baseUrl+MEDIUM_IMAGE_SIZE+imageKey;
    }

    public String getHighImageSizeQualityImagePath() {
        return baseUrl+BIG_IMAGE_SIZE+imageKey;
    }

}
