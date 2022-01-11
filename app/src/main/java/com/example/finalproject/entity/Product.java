package com.example.finalproject.entity;

import android.graphics.Bitmap;

import com.example.finalproject.entity.support.Offer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Product {
    private int idproduct;
    private int iduser;
    private String name;
    private String description;
    private Bitmap image;
    private ArrayList<Offer> offers;

    public Product(int iduser, String name, String description, Bitmap image) {
        this.iduser = iduser;
        this.name = name;
        this.description = description;
        this.image = image;
        offers = new ArrayList<>();
    }

    public Product(int idproduct, int iduser, String name, String description, Bitmap image) {
        this.idproduct = idproduct;
        this.iduser = iduser;
        this.name = name;
        this.description = description;
        this.image = image;
        offers = new ArrayList<>();
    }

    public Product() {

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] bitmapToByte(){
        Bitmap bmp = this.image;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
