package com.example.ahmed_eid.merchantnavigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MenuItemDetails extends AppCompatActivity {
    TextView TV_title,TV_type,TV_price;
    ImageView Img_ImageOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        TV_title = (TextView) findViewById(R.id.itemmtitle);
        TV_type = (TextView) findViewById(R.id.companyItem);
        TV_price = (TextView) findViewById(R.id.itemprice);
        Img_ImageOffer =(ImageView)findViewById(R.id.imageItem) ;

        Bundle b = getIntent().getExtras();

        String imageURL = b.getString("itemImage");
        String title = b.getString("itemtitle");
        String type = b.getString("itemmark");
        Float price = b.getFloat("itemPrice");

        TV_title.setText(title);
        TV_type.setText(type);
        TV_price.setText("$"+price);

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(Img_ImageOffer);

    }
}
