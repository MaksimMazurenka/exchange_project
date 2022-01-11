package com.example.finalproject.activity.market;
import com.example.finalproject.R;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerViewAdapterMultiSelect;
import com.example.finalproject.entity.support.Offer;
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
import java.net.SocketException;
import java.util.ArrayList;

public class NewDeal extends AppCompatActivity {
    SQLiteDatabase db;
    RecyclerView recyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    int selectedId = -1;
    int pos =-1;
    int normalBack = -1;
    int userDeal = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_deal);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);
        normalBack = intent.getIntExtra("normalBack",-1);
        selectedId = intent.getIntExtra("selectedId",-1);
        userDeal = intent.getIntExtra("userDeal",-1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Confirm(View view) throws SocketException {
        RecyclerViewAdapterMultiSelect recyclerViewAdapterMultiSelect = new RecyclerViewAdapterMultiSelect(products,NewDeal.this);
        ArrayList<Product> selected = recyclerViewAdapterMultiSelect.getChecked();
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i=0;i<selected.size();i++){
            ids.add(selected.get(i).getIdproduct());
            System.out.println(selected.get(i).getIdproduct() + " " + user.getIduser());
        }
        Offer offer = new Offer(user.getIduser(),ids);
        Database database = new Database(db);
        if(userDeal!=-1){
            products = database.getProducts();
            ArrayList<Product> productsWithDeals = new ArrayList<>();
            for(int i=0;i<products.size();i++){
                ArrayList<Offer> offers = products.get(i).getOffers();
                for(int j=0;j<offers.size();j++){
                    if(offers.get(j).getIduser()==user.getIduser()){
                        productsWithDeals.add(products.get(i));
                        break;
                    }
                }
            }
            Product product = productsWithDeals.get(userDeal);
            database.addOffer(offer,product.getIdproduct());
        }else {
            database.addOffer(offer,selectedId);
        }
        System.out.println(selected.size());
        System.gc();
        Intent intent2 = new Intent(NewDeal.this, ProductWithDeals.class);
        if(normalBack==0){
            pos=-2;
        }
        intent2.putExtra("pos",pos);
        intent2.putExtra("userDeal",userDeal);
        intent2.putExtra("selectedId",selectedId);
        startActivity(intent2);
    }

    public void Back(View view){
        System.gc();
        if(normalBack==0){
            pos=-2;
        }
        Intent intent2 = new Intent(NewDeal.this, Market.class);
        intent2.putExtra("pos",pos);
        startActivity(intent2);
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
        recyclerView = findViewById(R.id.Out4);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter=new RecyclerViewAdapterMultiSelect(products,NewDeal.this);
        recyclerView.setAdapter(mAdapter);
        RecyclerViewAdapterMultiSelect recyclerViewAdapterMultiSelect = new RecyclerViewAdapterMultiSelect(products,NewDeal.this);
        recyclerViewAdapterMultiSelect.getChecked().clear();
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