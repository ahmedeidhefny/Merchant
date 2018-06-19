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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

public class OfferRecycleHorAdaptor extends RecyclerView.Adapter<OfferRecycleHorAdaptor.ViewHolder> {

    private  ArrayList<String> items_Dis = new ArrayList<>();
    private ArrayList<String> titles_Dis = new ArrayList<>();
    private  ArrayList<String> marks_Dis = new ArrayList<>();
    private ArrayList<Integer> Discount_Dis = new ArrayList<>();
    private ArrayList<Float> Prices_Dis = new ArrayList<>();
    private ArrayList<Integer> offerIdList_Dis = new ArrayList<>();
    private ArrayList<String> starDateList_Dis = new ArrayList<>();
    private ArrayList<String> endDateList_Dis = new ArrayList<>();
    private ArrayList<Float> newPrices_Dis = new ArrayList<>();
    NotificationManager manager;
    Context context ;
    private  int Oday ;
    private  int Omonth ;
    private  int Oyear ;

    private  int OSday ;
    private  int OSmonth ;
    private  int OSyear ;

    private  int mYear;
    private  int mMonth;
    private  int mDay;


    public OfferRecycleHorAdaptor( Context context,ArrayList<Float> newPrices_Dis,ArrayList<String> items_Dis, ArrayList<String> titles_Dis, ArrayList<String> marks_Dis,
                                  ArrayList<Integer> discount_Dis, ArrayList<Float> prices_Dis, ArrayList<Integer> offerIdList_Dis,
                                  ArrayList<String> starDateList_Dis, ArrayList<String> endDateList_Dis) {
        this.items_Dis = items_Dis;
        this.titles_Dis = titles_Dis;
        this.marks_Dis = marks_Dis;
        Discount_Dis = discount_Dis;
        this.newPrices_Dis = newPrices_Dis;
        Prices_Dis = prices_Dis;
        this.offerIdList_Dis = offerIdList_Dis;
        this.starDateList_Dis = starDateList_Dis;
        this.endDateList_Dis = endDateList_Dis;
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
        return new OfferRecycleHorAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(titles_Dis.get(position));
        holder.marke.setText(marks_Dis.get(position));
        holder.oldPrice.setText("$"+Prices_Dis.get(position));
        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.NewPrice.setText("$"+newPrices_Dis.get(position));
        Glide.with(context)
                .asBitmap()
                .load(items_Dis.get(position))
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
            holder.discout.setText(Discount_Dis.get(position)+"%OFF");
            String im =items_Dis.get(position);

            Bitmap b = BitmapFactory.decodeFile("im");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("Attention ?")
                    .setContentText("This Offer *"+titles_Dis.get(position)+"*Not Available Now!")
                    .setSmallIcon(R.drawable.merchant)
                    .setLargeIcon(b);
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setVibrate(new long[]{500,1000,500,1000,500});
            builder.setSound(Uri.parse("android.resource://com.example.ahmed_eid.merchantnavigationdrawer/"+R.raw.notsound));
            manager.notify(id,builder.build());
            id++;
        }else {
            if (mYear < OSyear || (mYear==OSyear)&&(mMonth < OSmonth) || (mYear == OSyear) && (mMonth == OSmonth) && (mDay < OSday)) {
                holder.discout.setText(Discount_Dis.get(position) + "%Wait");
            } else {
                holder.discout.setText(Discount_Dis.get(position) + "%ON");
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

                        titles_Dis.remove(position);
                        marks_Dis.remove(position);
                        items_Dis.remove(position);
                        starDateList_Dis.remove(position);
                        endDateList_Dis.remove(position);
                        Discount_Dis.remove(position);
                        newPrices_Dis.remove(position);
                        Prices_Dis.remove(position);
                        notifyItemRemoved(position);

                        DeleteOfferDiscount delete = new DeleteOfferDiscount(context);
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
                Intent intent = new Intent(context,showOfferDiscountDetails.class);
                intent.putExtra("OfferImage2",items_Dis.get(position));
                intent.putExtra("title2",titles_Dis.get(position));
                intent.putExtra("type2",marks_Dis.get(position));
                intent.putExtra("price2",Prices_Dis.get(position));
                intent.putExtra("discount",Discount_Dis.get(position));
                intent.putExtra("starDate2",starDateList_Dis.get(position));
                intent.putExtra("endDate2",endDateList_Dis.get(position));
                intent.putExtra("newPrice",newPrices_Dis.get(position));
                intent.putExtra("offerDid",offerIdList_Dis.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles_Dis.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
            ImageView item ,delete;
            TextView discout ;
            TextView marke ;
            TextView title ;
            TextView oldPrice ;
            TextView NewPrice ;
        LinearLayout linearLayout ;


            public ViewHolder(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.image_viewOffer);
                discout = itemView.findViewById(R.id.discount);
                marke = itemView.findViewById(R.id.offermarke);
                title = itemView.findViewById(R.id.offertitle);
                oldPrice = itemView.findViewById(R.id.oldprice);
                NewPrice = itemView.findViewById(R.id.newprice);
                delete = itemView.findViewById(R.id.iconDelete);
                linearLayout =itemView.findViewById(R.id.linearOferclick);
            }
        }

    }




