package com.example.finalproject.activity.user;
import com.example.finalproject.R;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    private final int Pick_image = 1;
    int pos = -1;
    ImageView image;
    TextInputLayout name;
    EditText description;
    Bitmap bitmap = null;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);

        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Pick_image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void Confirm(View view) throws SocketException {
        if(bitmap == null){
            Toast toast = Toast.makeText(getApplicationContext(), "Выберите изображение из галереи", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            if(name.getEditText().getText().toString().length()< 3){
                Toast toast = Toast.makeText(getApplicationContext(), "Название тавара должно быть не меньше 3х символов", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                if(description.getText().toString().length()<8){
                    Toast toast = Toast.makeText(getApplicationContext(), "Описание товара должно быть не менее 8 символов", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Product product = new Product(user.getIduser(),name.getEditText().getText().toString(),description.getText().toString(),bitmap);
                    Database database = new Database(db);
                    if(pos ==-1){
                        database.addNewProduct(product);
                        System.gc();
                        Intent intent1 = new Intent(AddProduct.this, MainWindow.class);
                        startActivity(intent1);
                    }else{
                        database.updateProduct(product);
                        System.gc();
                        Intent intent1 = new Intent(AddProduct.this, MainWindow.class);
                        startActivity(intent1);
                    }
                }
            }
        }
    }

    public void Delete(View view) throws SocketException {
        Database database = new Database(db);
        database.dellProduct(product.getIdproduct());
        Intent intent1 = new Intent(AddProduct.this, MainWindow.class);
        startActivity(intent1);
    }

    public void Back(View view){
        Intent intent1 = new Intent(AddProduct.this, MainWindow.class);
        startActivity(intent1);
    }
    public void onInit() throws Exception {
        try {
            deserializeListOfObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(user.getIduser()==000){
            Toast toast = Toast.makeText(getApplicationContext(), "WTF!!!???? Я умываю руки", Toast.LENGTH_SHORT);
            toast.show();
            throw new Exception("NO god! Please no!");
        }
        else {
            Database database = new Database(db);
            products = database.getProducts(user.getIduser());
        }

        if(pos!=-1){
            TextInputEditText setHead = findViewById(R.id.setHead);
            Button delete = findViewById(R.id.delete_btn);
            delete.setVisibility(View.VISIBLE);
            TextView head = findViewById(R.id.Head);
            head.setText("Изменить товар");
            product = products.get(pos);
            image.setImageBitmap(product.getImage());
            description.setText(product.getDescription());
            setHead.setText(product.getName());
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
}