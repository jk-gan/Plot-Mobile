package com.example.jkgan.pmot.Shops;

/**
 * Created by JKGan on 29/11/2015.
 */
public class Shop {

    private String name;
    private String address;
    private String id;
    private String image;
    private String phone;
    private String description;
    private String smallImage;

    public Shop() {

    }

    public Shop(String name, String address, String id, String image, String smallImage, String phone, String description) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.image = image;
        this.smallImage = smallImage;
        this.phone = phone;
        this.description = description;
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

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}