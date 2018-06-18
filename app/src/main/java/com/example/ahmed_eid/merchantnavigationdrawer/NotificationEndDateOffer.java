package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationEndDateOffer {

    Context context ;
    int Oday ;
    int Omonth ;
    int Oyear ;

    int mYear;
    int mMonth;
    int mDay;
    ArrayList<String> endDate =new ArrayList<>();

    public NotificationEndDateOffer(Context context) {
        this.context = context;
    }

    public  void notifi_EndDateOffer()
    {
        endDate.add("2018-05-11");
        endDate.add("2018-06-0");
        endDate.add("2018-06-0");
        endDate.add("2018-06-00");
        endDate.add("2018-05-00");
        endDate.add("2018-05-00");

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        int xMonth = c.get(Calendar.MONTH);
        mMonth = xMonth+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        for (String s : endDate)
        {
            String[] x= s.split("-");
            Oyear = Integer.parseInt(x[0]);
            Omonth = Integer.parseInt(x[1]);
            Oday = Integer.parseInt(x[2]);

        //Toast.makeText(context, ""+mYear+mMonth+mDay, Toast.LENGTH_SHORT).show();
             int id =1 ;
            if (mDay==Oday&& mMonth==Omonth && mYear == Oyear){
                //Toast.makeText(context, "day1", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("OFFer Date ?")
                        .setContentText("Attention the End Date this Offer Today !")
                        .setSmallIcon(R.drawable.date);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(id,builder.build());
                id ++ ;
            }

            }if (mDay==Oday&& mYear == Oyear){
                Toast.makeText(context, "d2", Toast.LENGTH_SHORT).show();


       }



    }
}
