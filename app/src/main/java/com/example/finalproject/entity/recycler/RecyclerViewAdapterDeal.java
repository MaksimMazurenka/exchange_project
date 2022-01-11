package com.example.finalproject.entity.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.User;

import java.util.ArrayList;

public class RecyclerViewAdapterDeal extends RecyclerView.Adapter<RecyclerViewAdapterDeal.MyViewHolder> {

    Product product;
    Context context;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();

    public RecyclerViewAdapterDeal(Product product, Context context, ArrayList<User> users, ArrayList<Product> products){
        this.product = product;
        this.context = context;
        this.users=users;
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_product_deals,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String owner ="";
        for(int i=0;i<users.size();i++){
            if(product.getOffers().get(position).getIduser()==users.get(i).getIduser()){
                owner = users.get(i).getName();
            }
        }
        String production = "";
        for(int i=0;i<products.size();i++){
            for(int j = 0; j<product.getOffers().get(position).getIdproduct().size(); j++){
                if(products.get(i).getIdproduct()==product.getOffers().get(position).getIdproduct().get(j)){
                    production = production + products.get(i).getName() + "; " ;
                }
            }
        }
        if(owner.length()<3|| production.length()<6){
            holder.name.setText("Имя влядельца");
            holder.description.setText("Список товаров");
        }else{
            System.out.println(owner + " " + production);
            holder.name.setText(owner);
            holder.description.setText(production);
        }
    }

    @Override
    public int getItemCount() {
        return product.getOffers().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }
    }

}

