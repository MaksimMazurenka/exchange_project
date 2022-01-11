package com.example.finalproject.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.DataFromInternet;
import com.example.finalproject.entity.database.Database;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.SocketException;

public class Register extends AppCompatActivity {
    boolean isChecked = false;
    EditText passwordInput;
    TextInputLayout emailInput;
    TextInputLayout nameInput;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        passwordInput = findViewById(R.id.password2);
        emailInput = findViewById(R.id.email);
        nameInput =  findViewById(R.id.name);
    }

    public void back(View view){
        Intent intent1 = new Intent(Register.this, Login.class);
        startActivity(intent1);
    }

    public void register(View view) throws SocketException {
        String name = nameInput.getEditText().getText().toString();
        String email = nameInput.getEditText().getText().toString();
        int password = Integer.parseInt (passwordInput.getText().toString());
        if(name.length()<=4 || name.length()>=20){
            Toast toast = Toast.makeText(getApplicationContext(), "Имя должно вклюать от 4 до 20 символов", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            if(email.length()<=4 || email.length()>=20){
                Toast toast = Toast.makeText(getApplicationContext(), "Email введен неверно", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                if(password<=99999){
                    Toast toast = Toast.makeText(getApplicationContext(), "Пароль не достаточно надежен", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    Database database = new Database(db);
                    database.addNewUser(name,email,password);
                    DataFromInternet dataFromInternet = new DataFromInternet();
                    dataFromInternet.AddNewUser(name,email,password);
                    Intent intent1 = new Intent(Register.this, Login.class);
                    startActivity(intent1);
                }
            }
        }
    }

    public void show(View view){
        if (isChecked) {
            passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isChecked = false;
        } else {
            passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isChecked = true;
        }
    }
}