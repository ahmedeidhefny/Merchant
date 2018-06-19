package com.example.ahmed_eid.merchantnavigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class showOfferDiscountDetails extends AppCompatActivity {
    TextView TV_starDate,TV_endDate,TV_title,TV_type,TV_price,TV_discount,TV_newprice ;
    ImageView Img_ImageOffer ,editOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer_discount_details);

        TV_starDate = (TextView) findViewById(R.id.Star_Date2);
        TV_endDate = (TextView) findViewById(R.id.End_Date2);
        TV_title = (TextView) findViewById(R.id.Otitle2);
        TV_type = (TextView) findViewById(R.id.company2);
        TV_price = (TextView) findViewById(R.id.OOldprice);
        TV_newprice = (TextView) findViewById(R.id.Onewprice);
        TV_discount = (TextView) findViewById(R.id.oDis);
        Img_ImageOffer =(ImageView)findViewById(R.id.OIImage2) ;
        editOffer =(ImageView)findViewById(R.id.OfferEditIcon) ;
        Bundle b = getIntent().getExtras();

        final String starDate = b.getString("starDate2");
        final String endDate = b.getString("endDate2");
        String imageURL = b.getString("OfferImage2");
        final String title = b.getString("title2");
        final String type = b.getString("type2");
        final Float price = b.getFloat("price2");
        final int discoint = b.getInt("discount");
        Float newPrice = b.getFloat("newPrice");
        final int offerDId = b.getInt("offerDid");

        TV_starDate.setText(starDate);
        TV_endDate.setText(endDate);
        TV_title.setText(title);
        TV_type.setText(type);
        TV_price.setText("$"+price);
        TV_newprice.setText("$"+newPrice);
        TV_discount.setText(discoint+"%");

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(Img_ImageOffer);

     editOffer.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent in = new Intent(getApplicationContext(),EditOfferDiscount.class);
             in.putExtra("ti",title);
             in.putExtra("ty",type);
             in.putExtra("pr",price);
             in.putExtra("dis",discoint);
             in.putExtra("str",starDate);
             in.putExtra("end",endDate);
             in.putExtra("id",offerDId);
             startActivity(in);
         }
     });

    }
}
