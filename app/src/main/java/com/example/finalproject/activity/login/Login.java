package com.example.finalproject.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.activity.user.MainWindow;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.CustomData;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.support.ProductByte;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    SQLiteDatabase db;
    boolean isChecked = false;
    EditText password;
    TextInputLayout email;
    TextInputEditText emailInput;
    User user = new User(000,"","",000);

    public Login() throws SocketException {
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:44353/Users/")
            .addConverterFactory( GsonConverterFactory.create())
            .build();
    CustomData messagesApi = retrofit.create(CustomData.class);

    public ArrayList<User> getUsersFromInternet(Database database){
        ArrayList<User> usersFromInternet = new ArrayList<User>();
        Call<ArrayList<User>> messages = messagesApi.allUsers();
        messages.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    for (int x = 0; x < response.body().size(); x = x + 1) {
                        usersFromInternet.add(response.body().get(x));
                    }
                    database.dellAllUser();
                    for(int i = 0;i<usersFromInternet.size();i++){
                        database.addNewUser(usersFromInternet.get(i).getName(),usersFromInternet.get(i).getEmail(),usersFromInternet.get(i).getPassword());
                    }

                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });

        return usersFromInternet;
    }

    public ArrayList<ProductByte> getProductsFromInternet(Database database){
        ArrayList<ProductByte> productsFromInternet = new ArrayList<ProductByte>();
        Call<ArrayList<ProductByte>> messages = messagesApi.allProducts();
        messages.enqueue(new Callback<ArrayList<ProductByte>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductByte>> call, Response<ArrayList<ProductByte>> response) {
                if (response.isSuccessful()) {
                    for (int x = 0; x < response.body().size(); x = x + 1) {
                        productsFromInternet.add(response.body().get(x));
                    }
                    database.dellAllProduct();
//                    for(int i = 0;i<productsFromInternet.size();i++){
//                        database.addNewProductWithId(productsFromInternet.get(i));
//                    }
                } else {
                    System.out.println("response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductByte>> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });

        return productsFromInternet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        emailInput =  findViewById(R.id.emailInput);

        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);

        onInit();
        try {
            synchronize();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void synchronize() throws SocketException {
        if(isOnline(this)){
            Database database =  new Database(db);
            getUsersFromInternet(database);
            getProductsFromInternet(database);
        }
    }

    public void onInit(){
        try {
            deserializeListOfObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(user.getIduser()!=000){
            password.setText(user.getPassword()+"");
            emailInput.setText(user.getEmail());
        }
    }

    public void login(View view) throws IOException {
        Database database =  new Database(db);
        if(isOnline(this)) {
            user = database.getUser(email.getEditText().getText().toString());
            if (user.getIduser() != 000) {
                if (user.getPassword() == Integer.parseInt(password.getText().toString())) {
                    serializationToJson();
                    Intent intent1 = new Intent(Login.this, MainWindow.class);
                    startActivity(intent1);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Нет пользователя с так имейлом", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    public void register(View view){
        Intent intent1 = new Intent(Login.this, Register.class);
        startActivity(intent1);
    }

    public void show(View view){
        if (isChecked) {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isChecked = false;
        } else {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isChecked = true;
        }
    }

    public void deserializeListOfObjects() throws IOException {
        final String filename1 = "Login.json";
        File file = new File(super.getFilesDir(), filename1);

        if (file.exists()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                user = mapper.readValue(file, User.class);
            }
            catch (Exception e)
            {
                Log.i("Log_json", "Oops, your serialization doesn't work" + e);
            }
        } else {
            file.createNewFile();
        }
    }

    public void serializationToJson() throws IOException {
        final String filename1 = "Login.json";
        File file = new File(super.getFilesDir(), filename1);
        file.createNewFile();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,user);
        } catch (IOException e) {
            Log.i("Log_json", "Oops, your serialization doesn't work" + e);
        }
    }
}