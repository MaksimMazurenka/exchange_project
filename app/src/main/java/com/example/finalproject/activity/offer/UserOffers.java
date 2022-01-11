package com.example.finalproject.activity.offer;
import com.example.finalproject.R;
import com.example.finalproject.activity.user.MainWindow;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerItemClickListener;
import com.example.finalproject.entity.recycler.RecyclerViewAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserOffers extends AppCompatActivity {
    SQLiteDatabase db;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000, "", "", 000);
    int selectedId = -1;
    ArrayList<Product> productWith = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_offers);
        Intent intent = getIntent();
        selectedId = intent.getIntExtra("selectedId", -1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(UserOffers.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectedId = productWith.get(position).getIdproduct();
                    System.gc();
                    Intent intent2 = new Intent(UserOffers.this, UserProductOffer.class);
                    intent2.putExtra("selectedId", selectedId);
                    startActivity(intent2);
                }
                @Override
                public void onLongItemClick(View view, int position) {
                }
            })
        );
    }

    public void Back(View view) {
        System.gc();
        Intent intent2 = new Intent(UserOffers.this, MainWindow.class);
        startActivity(intent2);
    }

    public void onInit() throws Exception {
        try {
            deserializeListOfObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user.getIduser() == 000) {
            Toast toast = Toast.makeText(getApplicationContext(), "WTF!!!???? Я умываю руки", Toast.LENGTH_SHORT);
            toast.show();
            throw new Exception("NO god! Please no!");
        } else {
            Database database = new Database(db);
            products = database.getProducts(user.getIduser());
            for(int i=0;i<products.size();i++){
                if(products.get(i).getOffers().size()>1){
                    productWith.add(products.get(i));
                }
            }
            recyclerView = findViewById(R.id.Out1);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new RecyclerViewAdapter(productWith, UserOffers.this);
            recyclerView.setAdapter(mAdapter);
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