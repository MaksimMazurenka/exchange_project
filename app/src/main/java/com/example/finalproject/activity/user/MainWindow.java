package com.example.finalproject.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.activity.login.Login;
import com.example.finalproject.activity.market.Market;
import com.example.finalproject.activity.offer.UserOffers;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;
import com.example.finalproject.entity.database.Database;
import com.example.finalproject.entity.recycler.RecyclerItemClickListener;
import com.example.finalproject.entity.recycler.RecyclerViewAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainWindow extends AppCompatActivity {
    SQLiteDatabase db;
    int pos=-1;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Product> products = new ArrayList<>();
    User user = new User(000,"","",000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);

        db = getBaseContext().openOrCreateDatabase("newApp.db", MODE_PRIVATE, null);
        try {
            onInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainWindow.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        pos = position;
                        System.gc();
                        Intent intent2 = new Intent(MainWindow.this, AddProduct.class);
                        intent2.putExtra("pos",pos);
                        startActivity(intent2);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
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
        TextView textView = findViewById(R.id.textView9);
        textView.setText("Пользователь: " + user.getName());
        if(user.getIduser()==000){
            Toast toast = Toast.makeText(getApplicationContext(), "WTF!!!???? Я умываю руки", Toast.LENGTH_SHORT);
            toast.show();
            throw new Exception("NO god! Please no!");
        }
        else {
            Database database = new Database(db);
            products = database.getProducts(user.getIduser());
        }
        recyclerView = findViewById(R.id.Out);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter=new RecyclerViewAdapter(products,MainWindow.this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void createTwoButtonsAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainWindow.this);
        builder.setTitle(title);
        builder.setNegativeButton("Ваши сделки",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.gc();
                        pos =-2;
                        Intent intent2 = new Intent(MainWindow.this, Market.class);
                        intent2.putExtra("pos",pos);
                        startActivity(intent2);
                    }
                });
        builder.setPositiveButton("Предложения пользователей",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.gc();
                        Intent intent2 = new Intent(MainWindow.this, UserOffers.class);
                        intent2.putExtra("pos",pos);
                        startActivity(intent2);
                    }
                });
        builder.show();
    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.deal_settings :
                System.gc();
                if(netIsAvailable()){
                    createTwoButtonsAlertDialog("Выберите тип сдлеки");
                }else {

                }
                return true;
            case R.id.market_settings:
                System.gc();
                if(netIsAvailable()){
                    Intent intent3 = new Intent(MainWindow.this, Market.class);
                    startActivity(intent3);
                }else{

                }
                return true;
            case R.id.add_settings:
                System.gc();
                Intent intent2 = new Intent(MainWindow.this, AddProduct.class);
                intent2.putExtra("pos",pos);
                startActivity(intent2);
                return true;
            case R.id.exit_settings:
                final String filename1 = "Login.json";
                File file = new File(super.getFilesDir(), filename1);
                file.delete();
                System.gc();
                Intent intent1 = new Intent(MainWindow.this, Login.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
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