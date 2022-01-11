package com.example.finalproject.entity.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.support.Offer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Database extends AppCompatActivity {
    SQLiteDatabase db;

    public Database(SQLiteDatabase dba) throws SocketException {
        db = dba;
        createUsersTable();
        createProductsTable();
    }
    private void createUsersTable(){
        db.execSQL("PRAGMA foreign_keys=on");
        db.execSQL("CREATE TABLE IF NOT EXISTS USER (IDUSER INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL, EMAIL TEXT NOT NULL UNIQUE, PASSWORD INTEGER NOT NULL)");
    }
    private void createProductsTable(){
        db.execSQL("PRAGMA foreign_keys=on");
        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT (IDPRODUCT INTEGER PRIMARY KEY AUTOINCREMENT, IDUSER INTEGER NOT NULL,NAME TEXT NOT NULL,DESCRIPTION TEXT NOT NULL,IMAGE BLOB NOT NULL,OFFERS TEXT NOT NULL," +
                   "FOREIGN KEY (IDUSER) REFERENCES USER(IDUSER) ON DELETE CASCADE) ");
    }

    public void addNewUser(String name, String email, int password){
        db.execSQL("INSERT INTO USER VALUES ("+ null + ",'" + name + "','" + email + "'," + password + ");");
    }


    public User getUser(String email) throws SocketException {
        User user = new User(000,"","",000);
            try {
                Cursor query = db.rawQuery("SELECT * FROM USER WHERE EMAIL = '"+ email +"';", null);
                query.moveToFirst();
                int idColIndex = query.getColumnIndex("IDUSER");
                int facultyColIndex = query.getColumnIndex("NAME");
                int curseColIndex = query.getColumnIndex("EMAIL");
                int nameOfGroupColIndex = query.getColumnIndex("PASSWORD");
                do{
                    user = new User(query.getInt(idColIndex), query.getString(facultyColIndex), query.getString(curseColIndex),query.getInt(nameOfGroupColIndex));
                }
                while (query.moveToNext());
            }
            catch (Exception e)
            {
                System.out.println("NumberFormatException: " + e);
            }
        return user;
    }
    public User getUser(int id) throws SocketException {
        User user = new User(000,"","",000);
            try {
                Cursor query = db.rawQuery("SELECT * FROM USER WHERE IDUSER = "+ id +";", null);
                query.moveToFirst();
                int idColIndex = query.getColumnIndex("IDUSER");
                int facultyColIndex = query.getColumnIndex("NAME");
                int curseColIndex = query.getColumnIndex("EMAIL");
                int nameOfGroupColIndex = query.getColumnIndex("PASSWORD");
                do{
                    user = new User(query.getInt(idColIndex), query.getString(facultyColIndex), query.getString(curseColIndex),query.getInt(nameOfGroupColIndex));
                }
                while (query.moveToNext());
            }
            catch (Exception e)
            {
                System.out.println("NumberFormatException: " + e);
            }
            if(user.getIduser()==000){
                Toast toast = Toast.makeText(getApplicationContext(), "No such user in database", Toast.LENGTH_SHORT);
                toast.show();
            }
        return user;
    }

    public ArrayList<User> getUsers() throws SocketException {
        User user = new User(000,"","",000);
        ArrayList<User> users = new ArrayList<>();
            try {
                Cursor query = db.rawQuery("SELECT * FROM USER ;", null);
                query.moveToFirst();
                int idColIndex = query.getColumnIndex("IDUSER");
                int facultyColIndex = query.getColumnIndex("NAME");
                int curseColIndex = query.getColumnIndex("EMAIL");
                int nameOfGroupColIndex = query.getColumnIndex("PASSWORD");
                do{
                    user = new User(query.getInt(idColIndex), query.getString(facultyColIndex), query.getString(curseColIndex),query.getInt(nameOfGroupColIndex));
                    users.add(user);
                }
                while (query.moveToNext());
            }
            catch (Exception e)
            {
                System.out.println("NumberFormatException: " + e);
            }
            if(users.size()==0){
                Toast toast = Toast.makeText(getApplicationContext(), "No users in database", Toast.LENGTH_SHORT);
                toast.show();
            }
        return users;
    }

    public void addNewProduct(Product product){
        String offers = "";
        for (int i =0;i<product.getOffers().size();i++){
            offers = offers + " "  + product.getOffers().get(i).getIduser() + " ";
            for(int j = 0; j<product.getOffers().get(i).getIdproduct().size(); j++){
                offers = offers + product.getOffers().get(i).getIdproduct().get(j) + " ";
            }
            offers =  offers + ";";
        }
        byte[] image = product.bitmapToByte();
//        db.execSQL("INSERT INTO PRODUCT VALUES (" + null + ", " + product.getUserId() + ",'" + product.getName() + "','" + product.getDescription() + "', " + someString + " ,'" + offers + "');");
        String sql = "INSERT INTO PRODUCT (IDUSER,NAME,DESCRIPTION,IMAGE,OFFERS) VALUES(?,?,?,?,?)";
        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1, Integer.toString(product.getIduser()));
        insertStmt.bindString(2,product.getName());
        insertStmt.bindString(3, product.getDescription());
        insertStmt.bindBlob(4,image);
        insertStmt.bindString(5, offers);
        insertStmt.executeInsert();
        db.close();
    }

    public void addNewProductWithId(Product product){
        String offers = "";
        for (int i =0;i<product.getOffers().size();i++){
            offers = offers + " "  + product.getOffers().get(i).getIduser() + " ";
            for(int j = 0; j<product.getOffers().get(i).getIdproduct().size(); j++){
                offers = offers + product.getOffers().get(i).getIdproduct().get(j) + " ";
            }
            offers =  offers + ";";
        }
        byte[] image = product.bitmapToByte();
//        db.execSQL("INSERT INTO PRODUCT VALUES (" + null + ", " + product.getUserId() + ",'" + product.getName() + "','" + product.getDescription() + "', " + someString + " ,'" + offers + "');");
        String sql = "INSERT INTO PRODUCT (IDPRODUCT,IDUSER,NAME,DESCRIPTION,IMAGE,OFFERS) VALUES(?,?,?,?,?,?)";
        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1, Integer.toString(product.getIdproduct()));
        insertStmt.bindString(2, Integer.toString(product.getIduser()));
        insertStmt.bindString(3,product.getName());
        insertStmt.bindString(4, product.getDescription());
        insertStmt.bindBlob(5,image);
        insertStmt.bindString(6, offers);
        insertStmt.executeInsert();
        db.close();
    }

    public Product getProduct(int productId){
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Product product = new Product(000,000,"","",bitmap);
        try {
            Cursor query = db.rawQuery("SELECT * FROM PRODUCT WHERE IDPRODUCT = "+ productId +";", null);
            query.moveToFirst();
            int idColIndex = query.getColumnIndex("IDPRODUCT");
            int facultyColIndex = query.getColumnIndex("IDUSER");
            int curseColIndex = query.getColumnIndex("NAME");
            int nameOfGroupColIndex = query.getColumnIndex("DESCRIPTION");
            int nameOfGroupCol = query.getColumnIndex("OFFERS");
            int nameOfImage = query.getColumnIndex("IMAGE");
            do{
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap image = BitmapFactory.decodeByteArray(query.getBlob(nameOfImage), 0, query.getBlob(nameOfImage).length);
                product = new Product(query.getInt(idColIndex), query.getInt(facultyColIndex), query.getString(curseColIndex),query.getString(nameOfGroupColIndex),image);

                String offers = query.getString(nameOfGroupCol);
                String[] offer = offers.split("\\;");
                ArrayList<Offer> productOffers =  new ArrayList<>();
                for(int i = 0; i < offer.length; i++) {
                    String[] forOffer = offer[i].split("\\s");
                    ArrayList<Integer> products =  new ArrayList<>();
                    int user = 000;
                    for(int j = 1; j < forOffer.length; j++) {
                        if(j==1){
                            user =Integer.parseInt (forOffer[j]);
                        }else{
                            products.add(Integer.parseInt (forOffer[j]));
                        }
                    }
                    Offer offer1 = new Offer(user,products);
                    productOffers.add(offer1);
                }
                if(productOffers.size()>0){
                    product.setOffers(productOffers);
                }
            }
            while (query.moveToNext());
        }
        catch (Exception e)
        {
            System.out.println("NumberFormatException: " + e);
        }
        return product;
    }
    public ArrayList<Product> getProducts(){
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Product product = new Product(000,000,"","",bitmap);
        ArrayList<Product> arrayWithProducts =  new ArrayList<>();
        try {
            Cursor query = db.rawQuery("SELECT * FROM PRODUCT;", null);
            query.moveToFirst();
            int idColIndex = query.getColumnIndex("IDPRODUCT");
            int facultyColIndex = query.getColumnIndex("IDUSER");
            int curseColIndex = query.getColumnIndex("NAME");
            int nameOfGroupColIndex = query.getColumnIndex("DESCRIPTION");
            int nameOfGroupCol = query.getColumnIndex("OFFERS");
            int nameOfImage = query.getColumnIndex("IMAGE");
            do{
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap image = BitmapFactory.decodeByteArray(query.getBlob(nameOfImage), 0, query.getBlob(nameOfImage).length);
                product = new Product(query.getInt(idColIndex), query.getInt(facultyColIndex), query.getString(curseColIndex),query.getString(nameOfGroupColIndex),image);

                String offers = query.getString(nameOfGroupCol);
                String[] offer = offers.split("\\;");
                ArrayList<Offer> productOffers =  new ArrayList<>();
                for(int i = 0; i < offer.length; i++) {
                    String[] forOffer = offer[i].split("\\s");
                    ArrayList<Integer> products =  new ArrayList<>();
                    int user = 000;
                    for(int j = 1; j < forOffer.length; j++) {
                        if(j==1){
                            user =Integer.parseInt (forOffer[j]);
                        }else{
                            products.add(Integer.parseInt (forOffer[j]));
                        }
                    }
                    Offer offer1 = new Offer(user,products);
                    productOffers.add(offer1);
                }
                if(productOffers.size()>0){
                    product.setOffers(productOffers);
                }
                arrayWithProducts.add(product);
            }
            while (query.moveToNext());
        }
        catch (Exception e)
        {
            System.out.println("NumberFormatException: " + e);
        }
        if(product.getIduser()==000){
            Toast toast = Toast.makeText(getApplicationContext(), "No such product in database", Toast.LENGTH_SHORT);
            toast.show();
        }
        return arrayWithProducts;
    }
    public ArrayList<Product> getProducts(int userId){
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Product product = new Product(000,000,"","",bitmap);
        ArrayList<Product> arrayWithProducts =  new ArrayList<>();
        try {
            Cursor query = db.rawQuery("SELECT * FROM PRODUCT WHERE IDUSER="+ userId +";", null);
            query.moveToFirst();
            int idColIndex = query.getColumnIndex("IDPRODUCT");
            int facultyColIndex = query.getColumnIndex("IDUSER");
            int curseColIndex = query.getColumnIndex("NAME");
            int nameOfGroupColIndex = query.getColumnIndex("DESCRIPTION");
            int nameOfGroupCol = query.getColumnIndex("OFFERS");
            int nameOfImage = query.getColumnIndex("IMAGE");
            do{
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap image = BitmapFactory.decodeByteArray(query.getBlob(nameOfImage), 0, query.getBlob(nameOfImage).length);
                product = new Product(query.getInt(idColIndex), query.getInt(facultyColIndex), query.getString(curseColIndex),query.getString(nameOfGroupColIndex),image);

                String offers = query.getString(nameOfGroupCol);
                String[] offer = offers.split("\\;");
                ArrayList<Offer> productOffers =  new ArrayList<>();
                for(int i = 0; i < offer.length; i++) {
                    String[] forOffer = offer[i].split("\\s");
                    ArrayList<Integer> products =  new ArrayList<>();
                    int user = 000;
                    for(int j = 1; j < forOffer.length; j++) {
                        if(j==1){
                            user =Integer.parseInt (forOffer[j]);
                        }else{
                            products.add(Integer.parseInt (forOffer[j]));
                        }
                    }
                    Offer offer1 = new Offer(user,products);
                    productOffers.add(offer1);
                }
                if(productOffers.size()>0){
                    product.setOffers(productOffers);
                }
                arrayWithProducts.add(product);
            }
            while (query.moveToNext());
        }
        catch (Exception e)
        {
            System.out.println("NumberFormatException: " + e);
        }
        return arrayWithProducts;
    }
    public ArrayList<Product> getProductsNo(int userId){
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Product product = new Product(000,000,"","",bitmap);
        ArrayList<Product> arrayWithProducts =  new ArrayList<>();
        try {
            Cursor query = db.rawQuery("SELECT * FROM PRODUCT WHERE IDUSER !="+ userId +";", null);
            query.moveToFirst();
            int idColIndex = query.getColumnIndex("IDPRODUCT");
            int facultyColIndex = query.getColumnIndex("IDUSER");
            int curseColIndex = query.getColumnIndex("NAME");
            int nameOfGroupColIndex = query.getColumnIndex("DESCRIPTION");
            int nameOfGroupCol = query.getColumnIndex("OFFERS");
            int nameOfImage = query.getColumnIndex("IMAGE");
            do{
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap image = BitmapFactory.decodeByteArray(query.getBlob(nameOfImage), 0, query.getBlob(nameOfImage).length);
                product = new Product(query.getInt(idColIndex), query.getInt(facultyColIndex), query.getString(curseColIndex),query.getString(nameOfGroupColIndex),image);
                String offers = query.getString(nameOfGroupCol);
                String[] offer = offers.split("\\;");
                ArrayList<Offer> productOffers =  new ArrayList<>();
                for(int i = 0; i < offer.length; i++) {
                    String[] forOffer = offer[i].split("\\s");
                    ArrayList<Integer> products =  new ArrayList<>();
                    int user = 000;
                    for(int j = 1; j < forOffer.length; j++) {
                        if(j==1){
                            user =Integer.parseInt (forOffer[j]);
                        }else{
                            products.add(Integer.parseInt (forOffer[j]));
                        }
                    }
                    Offer offer1 = new Offer(user,products);
                    productOffers.add(offer1);
                }
                if(productOffers.size()>0){
                    product.setOffers(productOffers);
                }
                arrayWithProducts.add(product);
            }
            while (query.moveToNext());
        }
        catch (Exception e)
        {
            System.out.println("NumberFormatException: " + e);
        }
        return arrayWithProducts;
    }
    public void addOffer(Offer offerForProduct,int productId){
        Product product = getProduct(productId);;
        if(product.getIduser()!=000){
            System.gc();
            product.addOffer(offerForProduct);
            String offers = "";
            for (int i =0;i<product.getOffers().size();i++){
                System.gc();
                offers = offers + " "  + product.getOffers().get(i).getIduser() + " ";
                for(int j = 0; j<product.getOffers().get(i).getIdproduct().size(); j++){
                    offers = offers + product.getOffers().get(i).getIdproduct().get(j) + " ";
                }
                offers =  offers + ";";
            }
            db.execSQL("UPDATE PRODUCT SET OFFERS = '"+ offers +"' WHERE IDPRODUCT = "+ product.getIdproduct() +";");
        }
    }

    public void dellOffers(int userId,ArrayList<Product> productId){
        ArrayList<Product> productsP = getProductsNo(userId);
        for(int i=0;i<productsP.size();i++){
            ArrayList<Offer> offersO = productsP.get(i).getOffers();
            for(int j=0;j<offersO.size();j++){
                if(offersO.get(j).getIduser()==userId){
                    ArrayList<Integer> integers = offersO.get(j).getIdproduct();
                    for(int k=0;k<integers.size();k++) {
                        for (int n = 0; n < productId.size(); n++)
                            if (integers.get(k) == productId.get(n).getIdproduct()) {
                                Product product2 = productsP.get(i);
                                dellOffer(userId, product2.getIdproduct());
                                break;
                            }
                        }
                    }
                }
            }
    }


    public void dellOffer(int userId,int productId){
        Product product = getProduct(productId);;
        if(product.getIduser()!=000){
            System.gc();
            product.delOffer(userId);

            String offers = "";
            for (int i =0;i<product.getOffers().size();i++){
                System.gc();
                offers = offers + " "  + product.getOffers().get(i).getIduser() + " ";
                for(int j = 0; j<product.getOffers().get(i).getIdproduct().size(); j++){
                    offers = offers + product.getOffers().get(i).getIdproduct().get(j) + " ";
                }
                offers =  offers + ";";
            }
            db.execSQL("UPDATE PRODUCT SET OFFERS = '"+ offers +"' WHERE IDPRODUCT = "+ product.getIdproduct() +";");
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No such product in database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void dellProduct(int productId){
        db.execSQL("DELETE FROM PRODUCT WHERE IDPRODUCT = "+ productId +";");
    }
    public void dellAllUser(){
        db.execSQL("DELETE FROM USER;");
    }

    public void dellAllProduct(){
        db.execSQL("DELETE FROM PRODUCT;");
    }
    public void changeOwner(int productID,int newUserId){
        Product product = getProduct(productID);
        if(product.getIduser()!=000){
            db.execSQL("UPDATE PRODUCT SET IDUSER = "+ newUserId +" WHERE IDPRODUCT = "+ product.getIdproduct() +";");
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No such product in database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void updateProduct(Product product){
        dellProduct(product.getIdproduct());
        String sql = "INSERT INTO PRODUCT (IDPRODUCT,IDUSER,NAME,DESCRIPTION,IMAGE,OFFERS) VALUES(?,?,?,?,?,?)";
        String offers = "";
        for (int i =0;i<product.getOffers().size();i++){
            offers = offers + " "  + product.getOffers().get(i).getIduser() + " ";
            for(int j = 0; j<product.getOffers().get(i).getIdproduct().size(); j++){
                offers = offers + product.getOffers().get(i).getIdproduct().get(j) + " ";
            }
            offers =  offers + ";";
        }
        System.gc();
        byte[] image = product.bitmapToByte();
        System.gc();
        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1, Integer.toString(product.getIdproduct()));
        insertStmt.bindString(2, Integer.toString(product.getIduser()));
        insertStmt.bindString(3,product.getName());
        insertStmt.bindString(4, product.getDescription());
        insertStmt.bindBlob(5,image);
        insertStmt.bindString(6, offers);
        insertStmt.executeInsert();
        db.close();
    }
    public void goodOffer(int productID, Offer offer ,ArrayList<Product> noNoNoId){
        Product mainProduct = getProduct(productID);
        int sellerId = mainProduct.getIduser();
        int buyerId = offer.getIduser();
        dellOffers(buyerId,noNoNoId);//----------------------------------------------------
        dellOffer(buyerId,mainProduct.getIdproduct());
        changeOwner(mainProduct.getIdproduct(),buyerId);
        for (int i = 0; i<offer.getIdproduct().size(); i++){
            changeOwner(offer.getIdproduct().get(i),sellerId);
        }
    }
}
