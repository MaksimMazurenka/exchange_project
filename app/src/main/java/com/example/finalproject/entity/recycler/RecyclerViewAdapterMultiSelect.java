package com.example.finalproject.entity.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.entity.Product;

import java.util.ArrayList;

public class RecyclerViewAdapterMultiSelect extends RecyclerView.Adapter<RecyclerViewAdapterMultiSelect.MyViewHolder> {

    ArrayList<Product> products = new ArrayList<>();
    Context context;
    boolean isEnable=false;
    public static ArrayList<Product> selectedItem = new ArrayList<>();

    public RecyclerViewAdapterMultiSelect(ArrayList<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    public ArrayList<Product> getChecked(){
        return selectedItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_product_multy,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.description.setText(products.get(position).getDescription());
        Bitmap bitmap = products.get(position).getImage();
        holder.imageView.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    selectedItem.remove(products.get(position));
                    for(int i=0;i<selectedItem.size();i++){
                        if(selectedItem.get(i).getIdproduct()==products.get(position).getIdproduct()){
                            selectedItem.remove(i);
                        }
                    }
                }else{
                    holder.checkBox.setChecked(true);
                    selectedItem.add(products.get(position));
                }
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if(!isEnable){
//                    ActionMode.Callback callback = new ActionMode.Callback() {
//                        @Override
//                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                            MenuInflater menuInflater = mode.getMenuInflater();
//                            menuInflater.inflate(R.menu.menu,menu);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                            return false;
//                        }
//
//                        @Override
//                        public void onDestroyActionMode(ActionMode mode) {
//
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView description;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}
