package com.example.finalproject.activity.market;
import com.example.finalproject.R;
import com.example.finalproject.activity.user.MainWindow;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerItemClickListener;
import com.example.finalproject.entity.recycler.RecyclerViewAdapter;
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
import java.util.ArrayList;

public class Market extends AppCompatActivity {
    SQLiteDatabase db;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    int pos = -1;
    int selectedId = -1;
    int normalBack = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        Intent intent = getIntent();
        selectedId = intent.getIntExtra("selectedId",-1);
        pos = intent.getIntExtra("pos",-1);
        normalBack = intent.getIntExtra("normalBack",-1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Market.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(pos !=-2){
                            pos = position;
                        }
                        selectedId = products.get(position).getIdproduct();
                        System.gc();
                        Intent intent2 = new Intent(Market.this, ProductWithDeals.class);
                        intent2.putExtra("pos",pos);
                        if(pos == -2){
                            intent2.putExtra("userDeal",position);
                        }
                        intent2.putExtra("selectedId",selectedId);
                        startActivity(intent2);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    public void Back(View view){
        System.gc();
        if(selectedId==-1 && pos==-1)
        {
            Intent intent2 = new Intent(Market.this, MainWindow.class);
            startActivity(intent2);
        }else{
            if(pos==-2){
                Intent intent2 = new Intent(Market.this, MainWindow.class);
                startActivity(intent2);
            }
            else {
                if(normalBack==0){
                    pos=-2;
                    Intent intent2 = new Intent(Market.this, ProductWithDeals.class);
                    intent2.putExtra("selectedId",selectedId);
                    intent2.putExtra("pos",pos);
                    startActivity(intent2);
                }else{
                    Intent intent2 = new Intent(Market.this, ProductWithDeals.class);
                    intent2.putExtra("selectedId",selectedId);
                    intent2.putExtra("pos",pos);
                    startActivity(intent2);
                }
            }
        }
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
            if(selectedId==-1 && pos==-1){
                products = database.getProductsNo(user.getIduser());
                recyclerView = findViewById(R.id.Out1);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                mAdapter=new RecyclerViewAdapter(products,Market.this);
                recyclerView.setAdapter(mAdapter);
            }else {
                if(pos==-2){
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
                    recyclerView = findViewById(R.id.Out1);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(layoutManager);
                    mAdapter=new RecyclerViewAdapter(productsWithDeals,Market.this);
                    recyclerView.setAdapter(mAdapter);
                }else {
                    Product product = database.getProduct(selectedId);
                    Offer offer = product.getOffers().get(pos);
                    for(int i = 0; i<offer.getIdproduct().size(); i++){
                        products.add(database.getProduct(offer.getIdproduct().get(i)));
                    }
                    recyclerView = findViewById(R.id.Out1);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(layoutManager);
                    mAdapter=new RecyclerViewAdapter(products,Market.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }
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