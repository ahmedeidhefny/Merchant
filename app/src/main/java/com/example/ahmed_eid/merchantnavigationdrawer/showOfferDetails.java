package com.example.ahmed_eid.merchantnavigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URL;

public class showOfferDetails extends AppCompatActivity {

    TextView TV_starDate,TV_endDate,TV_title,TV_type,TV_price,TV_points ;
    ImageView Img_ImageOffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer_details);
        TV_starDate = (TextView) findViewById(R.id.Star_Date);
        TV_endDate = (TextView) findViewById(R.id.End_Date);
        TV_title = (TextView) findViewById(R.id.Otitle);
        TV_type = (TextView) findViewById(R.id.company);
        TV_price = (TextView) findViewById(R.id.Oprice);
        TV_points = (TextView) findViewById(R.id.numpoints);
        Img_ImageOffer =(ImageView)findViewById(R.id.OIImage) ;

        Bundle b = getIntent().getExtras();

        String starDate = b.getString("starDate");
        String endDate = b.getString("endDate");
        String imageURL = b.getString("OfferImage");
        String title = b.getString("title");
        String type = b.getString("type");
        Float price = b.getFloat("price");
        int points = b.getInt("points");


        TV_starDate.setText(starDate);
        TV_endDate.setText(endDate);
        TV_title.setText(title);
        TV_type.setText(type);
        TV_price.setText("$"+price);
        TV_points.setText(points+" Point");

        //Toast.makeText(this, imageURL, Toast.LENGTH_SHORT).show();

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(Img_ImageOffer);


    }
}
