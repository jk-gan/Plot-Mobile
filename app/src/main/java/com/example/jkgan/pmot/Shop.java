package com.example.jkgan.pmot;

/**
 * Created by JKGan on 29/11/2015.
 */
public class Shop {

    private String name;
    private String address;
    private int photoId;
    private String id;
    private String image;

    public Shop(String name, String address, int photoId, String id, String image) {
        this.name = name;
        this.address = address;
        this.photoId = photoId;
        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}