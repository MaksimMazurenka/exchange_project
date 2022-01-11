package com.example.finalproject.entity.support;

import java.util.ArrayList;

public class Offer {
    private int iduser;
    private ArrayList<Integer> idproduct;

    public Offer(int iduser, ArrayList<Integer> idproduct) {
        this.iduser = iduser;
        this.idproduct = idproduct;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public ArrayList<Integer> getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(ArrayList<Integer> idproduct) {
        this.idproduct = idproduct;
    }
}
