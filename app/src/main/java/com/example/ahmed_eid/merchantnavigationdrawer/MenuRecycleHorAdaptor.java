package com.example.ahmed_eid.merchantnavigationdrawer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.support.v7.widget.RecyclerView.*;

public class MenuRecycleHorAdaptor extends RecyclerView.Adapter<MenuRecycleHorAdaptor.ViewHolder> {

    Context context ;
    private  ArrayList<String> itemsImg = new ArrayList<>();
    private  ArrayList<String> titles = new ArrayList<>();
    private  ArrayList<String> marks = new ArrayList<>();
    private ArrayList<Float> prices = new ArrayList<>();
    private ArrayList<Integer> itemsID = new ArrayList<>();

    public MenuRecycleHorAdaptor(Context context, ArrayList<String> itemsImg, ArrayList<String> titles, ArrayList<String> marks,
                                 ArrayList<Float> prices, ArrayList<Integer> itemsID) {
        this.context = context;
        this.itemsImg = itemsImg;
        this.titles = titles;
        this.marks = marks;
        this.prices = prices;
        this.itemsID = itemsID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_listitemshap,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.marke.setText(marks.get(position));
        holder.price.setText("$"+prices.get(position));

        Glide.with(context)
                .asBitmap()
                .load(itemsImg.get(position))
                .into(holder.item);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        titles.remove(position);
                        marks.remove(position);
                        prices.remove(position);
                        itemsImg.remove(position);
                        notifyItemRemoved(position);

                        DeleteItemMenu delete = new DeleteItemMenu(context);
                        int itemId = itemsID.get(position);
                        delete.deleteItemDB(itemId);

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();


            }
        });

        holder.linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MenuItemDetails.class);
                intent.putExtra("itemImage",itemsImg.get(position));
                intent.putExtra("itemtitle",titles.get(position));
                intent.putExtra("itemmark",marks.get(position));
                intent.putExtra("itemPrice",prices.get(position));
                intent.putExtra("itemId",itemsID.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView item,delete;
        TextView price ;
        TextView marke ;
        TextView title ;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.image_view);
            price = itemView.findViewById(R.id.price);
            marke = itemView.findViewById(R.id.marke);
            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.iconDelete3);
            linearLayout =itemView.findViewById(R.id.linearItemClick);
        }
    }



}
