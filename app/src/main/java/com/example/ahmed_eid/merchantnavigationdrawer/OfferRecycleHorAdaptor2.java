package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

public class OfferRecycleHorAdaptor2 extends RecyclerView.Adapter<OfferRecycleHorAdaptor2.ViewHolder> {

        private  ArrayList<String> items = new ArrayList<>();
        private ArrayList<String> titles = new ArrayList<>();
        private  ArrayList<String> marks = new ArrayList<>();
        private ArrayList<Integer> points = new ArrayList<>();
        private ArrayList<Float> Prices = new ArrayList<>();
        private ArrayList<Integer> offerIdList_Dis = new ArrayList<>();
        private ArrayList<String> starDateList_Dis = new ArrayList<>();
        private ArrayList<String> endDateList_Dis = new ArrayList<>();

        Context context ;
    NotificationManager manager;
    private  int Oday ;
    private  int Omonth ;
    private  int Oyear ;

    private  int OSday ;
    private  int OSmonth ;
    private  int OSyear ;

    private  int mYear;
    private  int mMonth;
    private  int mDay;

    public OfferRecycleHorAdaptor2(Context context,ArrayList<String> items,ArrayList<String> titles, ArrayList<String> marks, ArrayList<Integer> points,
                                   ArrayList<Float> prices,ArrayList<String> starDateList_Dis,ArrayList<String> endDateList_Dis,ArrayList<Integer> offerIdList_Dis) {
        this.titles = titles;
        this.items = items;
        this.marks = marks;
        this.points = points;
        this.offerIdList_Dis = offerIdList_Dis ;
        this.starDateList_Dis = starDateList_Dis ;
        this.endDateList_Dis = endDateList_Dis ;
        Prices = prices;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        int xMonth = c.get(Calendar.MONTH);
        mMonth = xMonth+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_listitemshap,parent,false);
        return new OfferRecycleHorAdaptor2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.marke.setText(marks.get(position));
        holder.points.setText(points.get(position)+" Point");
        Glide.with(context)
                .asBitmap()
                .load(items.get(position))
                .into(holder.item);

        String endDate = endDateList_Dis.get(position);
        String[] x= endDate.split("-");
        Oyear = Integer.parseInt(x[0]);
        Omonth = Integer.parseInt(x[1]);
        Oday = Integer.parseInt(x[2]);

        String starDate = starDateList_Dis.get(position);
        String[] y= starDate.split("-");
        OSyear = Integer.parseInt(y[0]);
        OSmonth = Integer.parseInt(y[1]);
        OSday = Integer.parseInt(y[2]);


        int id=1 ;
        if (mDay==Oday&& mMonth==Omonth && mYear == Oyear||mDay>Oday&& mMonth==Omonth && mYear == Oyear||mMonth>Omonth && mYear == Oyear||mYear>Oyear)
        {
            holder.price.setText("$"+Prices.get(position)+"/OFF");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("Attention ?")
                    .setContentText("This Offer *"+titles.get(position)+"*Not Available Now!")
                    .setSmallIcon(R.drawable.merchant);
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setVibrate(new long[]{500,1000,500,1000,500});
            builder.setSound(Uri.parse("android.resource://com.example.ahmed_eid.merchantnavigationdrawer/"+R.raw.notsound));
            manager.notify(id,builder.build());
            id++;
        }else {
            if (mYear < OSyear || (mYear==OSyear)&&(mMonth < OSmonth) || (mYear == OSyear) && (mMonth == OSmonth) && (mDay < OSday)) {
                holder.price.setText("$"+Prices.get(position)+"/Wait");
            } else {
                holder.price.setText("$"+Prices.get(position)+"/ON");
            }
        }


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
                        items.remove(position);
                        starDateList_Dis.remove(position);
                        endDateList_Dis.remove(position);
                        points.remove(position);
                        Prices.remove(position);
                        notifyItemRemoved(position);

                        DeleteOfferPoints delete = new DeleteOfferPoints(context);
                        int offerId = offerIdList_Dis.get(position);
                        delete.deleteOfferDiscountDB(offerId);

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



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,starDateList_Dis.get(position) , Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,endDateList_Dis.get(position) , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,showOfferDetails.class);
                intent.putExtra("OfferImage",items.get(position));
                intent.putExtra("title",titles.get(position));
                intent.putExtra("type",marks.get(position));
                intent.putExtra("price",Prices.get(position));
                intent.putExtra("points",points.get(position));
                intent.putExtra("starDate",starDateList_Dis.get(position));
                intent.putExtra("endDate",endDateList_Dis.get(position));
                intent.putExtra("offerPid",offerIdList_Dis.get(position));
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
            TextView points ;
            TextView marke ;
            TextView title ;
            TextView price ;
            LinearLayout linearLayout ;



            public ViewHolder(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.image_viewOffer);
                price = itemView.findViewById(R.id.discount);
                marke = itemView.findViewById(R.id.offermarke);
                title = itemView.findViewById(R.id.offertitle);
                points = itemView.findViewById(R.id.newprice);
                linearLayout = itemView.findViewById(R.id.linearOferclick);
                delete = itemView.findViewById(R.id.iconDelete);
            }
        }



    }




