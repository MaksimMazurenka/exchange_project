package com.example.finalproject.activity.market;
import com.example.finalproject.R;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerItemClickListener;
import com.example.finalproject.entity.recycler.RecyclerViewAdapterDeal;
import com.example.finalproject.entity.support.Offer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProductWithDeals extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    private final int Pick_image = 1;
    int pos = -1;
    int userDeal = -1;
    int selectedId =-1;
    ImageView image;
    TextInputLayout name;
    TextView description;
    Product product;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_with_deals);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);
        selectedId = intent.getIntExtra("selectedId",-1);
        userDeal = intent.getIntExtra("userDeal",-1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        image = findViewById(R.id.image2);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description3);

        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ProductWithDeals.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(position!=0){
                            System.gc();
                            Intent intent2 = new Intent(ProductWithDeals.this, Market.class);
                            selectedId = product.getIdproduct();
                            intent2.putExtra("selectedId",selectedId);
                            intent2.putExtra("userDeal",userDeal);
                            if(pos==-2){
                                intent2.putExtra("normalBack",0);
                            }
                            intent2.putExtra("pos",position);
                            startActivity(intent2);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    public void onInit() throws Exception {
        try {
            deserializeListOfObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Database database = new Database(db);
        if(user.getIduser()==000){
            Toast toast = Toast.makeText(getApplicationContext(), "WTF!!!???? Я умываю руки", Toast.LENGTH_SHORT);
            toast.show();
            throw new Exception("NO god! Please no!");
        }
        else {
            if(pos ==-2){
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
                if(userDeal==-1){
                    product = database.getProduct(selectedId);
                }
                else{
                    product = productsWithDeals.get(userDeal);
                }
            }else {
                product = database.getProduct(selectedId);
            }
        }

        TextView setHead = findViewById(R.id.setHead);
        TextView head = findViewById(R.id.head);
        head.setText("Товар пользователя: " + product.getIduser());
        image.setImageBitmap(product.getImage());
        description.setText(product.getDescription());
        setHead.setText(product.getName());

        recyclerView = findViewById(R.id.Out3);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter=new RecyclerViewAdapterDeal(product,ProductWithDeals.this,database.getUsers(),database.getProducts());
        recyclerView.setAdapter(mAdapter);

    }

    public void Confirm(View view) {
        System.gc();
        Intent intent2 = new Intent(ProductWithDeals.this, NewDeal.class);
        intent2.putExtra("selectedId",selectedId);
        intent2.putExtra("userDeal",userDeal);
        if(pos==-2){
            intent2.putExtra("normalBack",0);
        }
        startActivity(intent2);
    }

    public void Back(View view) {
        System.gc();
        if(pos==-2){
            Intent intent2 = new Intent(ProductWithDeals.this, Market.class);
            intent2.putExtra("pos",pos);
            startActivity(intent2);
        }
        else {
            Intent intent2 = new Intent(ProductWithDeals.this, Market.class);
            startActivity(intent2);
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