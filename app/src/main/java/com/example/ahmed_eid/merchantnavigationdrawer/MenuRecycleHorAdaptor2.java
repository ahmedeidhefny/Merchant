package com.example.ahmed_eid.merchantnavigationdrawer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.support.v7.widget.RecyclerView.*;

public class MenuRecycleHorAdaptor2 extends RecyclerView.Adapter<MenuRecycleHorAdaptor2.ViewHolder> {

    private  ArrayList<String> menuImages = new ArrayList<>();
    private  ArrayList<Integer> menuId = new ArrayList<>();
    Context context ;

    public MenuRecycleHorAdaptor2(Context context,ArrayList<Integer> menuId,ArrayList<String> menuImages) {
        this.menuImages = menuImages;
        this.menuId= menuId ;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_listitemshap2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        int id = position+1 ;
        holder.id.setText(""+ id);
        Glide.with(context)
                .asBitmap()
                .load(menuImages.get(position))
                .into(holder.menu);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        menuImages.remove(position);
                        notifyItemRemoved(position);

                        DeleteMenuPhoto delete = new DeleteMenuPhoto(context);
                        int menu_Id = menuId.get(position);
                        delete.deleteMenuDB(menu_Id);

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

    }

    @Override
    public int getItemCount() {
        return menuImages.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView menu,delete;
        TextView id ;

        public ViewHolder(View itemView) {
            super(itemView);
            menu = itemView.findViewById(R.id.image_view2);
            id = itemView.findViewById(R.id.menuId);
            delete = itemView.findViewById(R.id.iconDelete2);

        }
    }



}
