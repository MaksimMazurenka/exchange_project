package com.example.finalproject.entity.support;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProductByte {
    private int idproduct;
    private int iduser;
    private String name;
    private String description;
    private byte[] image;
    private ArrayList<Offer> offers;

    public ProductByte(int iduser, String name, String description, byte[] image) {
        this.iduser = iduser;
        this.name = name;
        this.description = description;
        this.image = image;
        offers = new ArrayList<>();
    }

    public ProductByte(int idproduct, int iduser, String name, String description, byte[] image) {
        this.idproduct = idproduct;
        this.iduser = iduser;
        this.name = name;
        this.description = description;
        this.image = image;
        offers = new ArrayList<>();
    }

    public ProductByte() {

    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
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

    public void addOffer(Offer offer){
        for(int i=0;i<this.offers.size();i++){
            if(this.offers.get(i).getIduser()==offer.getIduser()){
                this.offers.remove(i);
            }
        }
        this.offers.add(offer);
    }

    public void delOffer(int userID){
        for(int i = 0;i<this.offers.size();i++){
            if(offers.get(i).getIduser()==userID){
                offers.remove(i);
            }
        }
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "Product{" + idproduct + "} userId:" + iduser + ", name:'" + name + '\'' + '}';
    }
}
