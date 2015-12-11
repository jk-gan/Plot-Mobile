package com.example.jkgan.pmot.Shops;

/**
 * Created by JKGan on 08/12/2015.
 */
public class Promotion {
    private String name;
    private String description;
    private String tnc;
    private String image;
    private String id;
    private String smallImage;
    private Shop shop = new Shop();

    public Promotion(String name, String description, String id, String image, String smallImage, String tnc, String shopName, String address, String shopIdentity) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.image = image;
        this.smallImage = smallImage;
        this.tnc = tnc;
        this.shop.setName(shopName);
        this.shop.setAddress(address);
        this.shop.setId(shopIdentity);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
