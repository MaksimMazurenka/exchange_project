package com.example.finalproject.entity.database;

import android.graphics.Bitmap;

import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class DataFromInternet {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:44353/Users/")
            .addConverterFactory( GsonConverterFactory.create())
            .build();
    CustomData messagesApi = retrofit.create(CustomData.class);


    public User getUsersFromInternetByEmail(String email){
        final User[] usersFromInternet = {new User()};
        Call<User> messages = messagesApi.GetUserByEmail(email);

        messages.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    usersFromInternet[0] = response.body();
                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return usersFromInternet[0];
    }

    public User getUsersFromInternetById(int id){
        final User[] usersFromInternet = {new User()};
        Call<User> messages = messagesApi.GetUserById(id);

        messages.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    usersFromInternet[0] = response.body();
                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return usersFromInternet[0];
    }
    //----------------------------------------------------------------------------------------------Products----------------------------------------------------------------------------------

//    public ArrayList<Product> getProductsFromInternet(){
//        ArrayList<Product> productsFromInternet = new ArrayList<Product>();
//        Call<ArrayList<Product>> messages = messagesApi.allProducts();
//        messages.enqueue(new Callback<ArrayList<Product>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
//                if (response.isSuccessful()) {
//                    for (int x = 0; x < response.body().size(); x = x + 1) {
//                        productsFromInternet.add(response.body().get(x));
//                    }
//                } else {
//                    System.out.println("response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
//                System.out.println("failure " + t);
//            }
//        });
//
//        return productsFromInternet;
//    }


    public Product getProductFromInternetById(int id){
        final Product[] productFromInternet = {new Product()};
        Call<Product> messages = messagesApi.ProductById(id);

        messages.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    productFromInternet[0] = response.body();
                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return productFromInternet[0];
    }

    public ArrayList<Product> getProductsFromInternetByUserId(int id){
        ArrayList<Product> productsFromInternet = new ArrayList<Product>();
        Call<ArrayList<Product>> messages = messagesApi.allProductsByUserId(id);
        messages.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    for (int x = 0; x < response.body().size(); x = x + 1) {
                        productsFromInternet.add(response.body().get(x));
                    }
                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });

        return productsFromInternet;
    }

    public ArrayList<Product> getProductsFromInternetByNoUserId(int id){
        ArrayList<Product> productsFromInternet = new ArrayList<Product>();
        Call<ArrayList<Product>> messages = messagesApi.allProductsByNoUserId(id);
        messages.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    for (int x = 0; x < response.body().size(); x = x + 1) {
                        productsFromInternet.add(response.body().get(x));
                    }
                } else {
                    System.out.println("response code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return productsFromInternet;
    }

    public String AddNewUser(String name, String email, int password){
        Call<String> messages = messagesApi.AddNewUser(name, email, password);
        final String[] result = {""};
        messages.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                   result[0] = response.body();
                } else {
                    System.out.println("response code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return result[0];
    }


    public String AddNewProduct(int idproduct, int iduser, String name, String description, byte[] image, String offers){
        Call<String> messages = messagesApi.AddNewProduct(idproduct, iduser, name, description, image , offers);
        final String[] result = {""};
        messages.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    result[0] = response.body();
                } else {
                    System.out.println("response code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });
        return result[0];
    }

}
