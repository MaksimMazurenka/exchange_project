package com.example.finalproject.activity.offer;
import com.example.finalproject.R;
import com.example.finalproject.activity.market.NewDeal;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerItemClickListener;
import com.example.finalproject.entity.recycler.RecyclerViewAdapterDeal;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class UserProductOffer extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    private final int Pick_image = 1;
    int pos = -1;
    int selectedId =-1;
    int userDeal =-1;
    ImageView image;
    TextView description;
    Product product;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_product_offer);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);
        pos = intent.getIntExtra("userDeal",-1);
        selectedId = intent.getIntExtra("selectedId",-1);
        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        image = findViewById(R.id.image3);
        description = findViewById(R.id.description3);

        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(UserProductOffer.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(position!=0){
                            System.gc();
                            Intent intent2 = new Intent(UserProductOffer.this, GoodOffer.class);
                            intent2.putExtra("selectedId",selectedId);
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
        ArrayList<Product> productWith = new ArrayList<>();
        if(user.getIduser()==000){
            Toast toast = Toast.makeText(getApplicationContext(), "WTF!!!???? Я умываю руки", Toast.LENGTH_SHORT);
            toast.show();
            throw new Exception("NO god! Please no!");
        }
        else {
//            products = database.getProductsNo(user.getId());
//            for(int i=0;i<products.size();i++){
//                if(products.get(i).getOffers().size()>1){
//                    productWith.add(products.get(i));
//                }
//            }
//            product = productWith.get(selectedId);
            product = database.getProduct(selectedId);
        }
        TextView setHead = findViewById(R.id.setHead2);
//            product = products.get(pos);
//            product = products.get(pos);
        image.setImageBitmap(product.getImage());
        description.setText(product.getDescription());
        setHead.setText(product.getName());

        recyclerView = findViewById(R.id.Out3);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter=new RecyclerViewAdapterDeal(product,UserProductOffer.this,database.getUsers(),database.getProducts());
        recyclerView.setAdapter(mAdapter);
    }

    public void Confirm(View view) {
        System.gc();
        Intent intent2 = new Intent(UserProductOffer.this, NewDeal.class);
        intent2.putExtra("selectedId",selectedId);
        if(pos==-2){
            intent2.putExtra("normalBack",0);
        }
        startActivity(intent2);
    }

    public void Back(View view) {
        System.gc();
        if(pos==-2){
            Intent intent2 = new Intent(UserProductOffer.this, UserOffers.class);
            intent2.putExtra("pos",pos);
            startActivity(intent2);
        }
        else {
            Intent intent2 = new Intent(UserProductOffer.this, UserOffers.class);
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