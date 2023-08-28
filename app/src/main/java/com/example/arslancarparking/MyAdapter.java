package com.example.arslancarparking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recName.setText(dataList.get(position).getProductName());
        holder.recRegistar.setText("Reg: "+dataList.get(position).getProductRegistration());
        holder.recFee.setText("Fee: "+dataList.get(position).getProductPrice());
        holder.recPhone.setText("Ph: "+dataList.get(position).getProductPhone());
        holder.recCateg.setText("Cat: "+dataList.get(position).getProductCategory());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, activity_detail.class);
//
//                intent.putExtra("Name", dataList.get(holder.getAdapterPosition()).getProductName());
//                intent.putExtra("Code", dataList.get(holder.getAdapterPosition()).getProductRegistration());
//                intent.putExtra("Price", dataList.get(holder.getAdapterPosition()).getProductPrice());
//                intent.putExtra("Percentage", dataList.get(holder.getAdapterPosition()).getProductPhone());
//                intent.putExtra("Category", dataList.get(holder.getAdapterPosition()).getProductCategory());
//                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
//                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recName, recRegistar, recFee, recPhone, recCateg;
    CardView recCard;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recName = itemView.findViewById(R.id.recName);
        recRegistar = itemView.findViewById(R.id.recRegistar);
        recFee = itemView.findViewById(R.id.recFee);
        recPhone = itemView.findViewById(R.id.recPhone);
        recCateg = itemView.findViewById(R.id.recCateg);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
