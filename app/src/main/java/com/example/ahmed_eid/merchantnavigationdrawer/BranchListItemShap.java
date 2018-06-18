package com.example.ahmed_eid.merchantnavigationdrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BranchListItemShap {

    public int id ;
    public String branchName ;

    public BranchListItemShap(String branchName,int id) {
        this.branchName = branchName;
        this.id = id ;
    }

}

class arrayAdaptorHandle extends BaseAdapter {

    ArrayList<BranchListItemShap> items = new ArrayList<BranchListItemShap>();
    ArrayList<Integer> branchId = new ArrayList<Integer>();

    LayoutInflater inflater ;
    Context context;
    DeleteBranch deleteBranch ;
    String delBranchName;

    arrayAdaptorHandle(Context context, ArrayList<BranchListItemShap> items,ArrayList<Integer> branchId) {
        this.items = items;
        this.branchId = branchId ;
        this.context=context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position).branchName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myLayout = inflater.inflate(R.layout.branch_list_shap, null);

        TextView BName = (TextView) myLayout.findViewById(R.id.Branch_Name) ;
        TextView Bid = (TextView) myLayout.findViewById(R.id.Branch_id) ;
        ImageButton delete = (ImageButton) myLayout.findViewById(R.id.image_delete);

        BName.setText(items.get(position).branchName);
        Bid.setText(items.get(position).id+"-");


        BName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,BranchesSignIn.class);
                int Bid = branchId.get(position);
                intent.putExtra("OBId",Bid);
                context.startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        notifyDataSetChanged();
                        DeleteBranch deleteBranch = new DeleteBranch(context);
                        int Bid = branchId.get(position);
                        deleteBranch.deleteBranchDB(Bid);
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

        return myLayout;
    }
}
