package com.example.finalproject.entity.database;
import android.graphics.Bitmap;

import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.support.ProductByte;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomData {
    @GET("GetAll")
    Call<ArrayList<User>> allUsers();

    @GET("GetUserByEmail")
    Call<User> GetUserByEmail(@Query("email") String email);

    @GET("GetUserById")
    Call<User> GetUserById(@Query("id") int id);

    @GET("GetAllProducts")
    Call<ArrayList<ProductByte>> allProducts();

    @GET("ProductById")
    Call<Product> ProductById(@Query("id") int id);

    @GET("allProductsByUserId")
    Call<ArrayList<Product>> allProductsByUserId(@Query("id") int id);

    @GET("allProductsByNoUserId")
    Call<ArrayList<Product>> allProductsByNoUserId(@Query("id") int id);

    @GET("AddNewUser")
    Call<String> AddNewUser(@Query("name") String name,@Query("email") String email,@Query("password") int password);

    @GET("AddNewProduct")
    Call<String> AddNewProduct(@Query("idproduct") int idproduct, @Query("iduser") int iduser,@Query("name") String name,@Query("description") String description,@Query("image") byte[] image,@Query("offers") String offers);
}