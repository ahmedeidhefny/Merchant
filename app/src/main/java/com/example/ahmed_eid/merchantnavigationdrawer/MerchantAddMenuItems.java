package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MerchantAddMenuItems extends AppCompatActivity {
   EditText ET_title,ET_type,ET_price;
   ImageView itemImgV,itemImgG ;
   ImageButton pickerImage ;
   Button btn_addItemMenu ;
   Bitmap bitmap ;

    RequestQueue requestQueue ;
    StringRequest request ;
    AlertDialog alertDialog;
    int placeId ;
    SharedPreferences sharedPreferences ;
    private  String addItemMenuURL = "http://gp.sendiancrm.com/offerall/addItemsMenu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_menu_items);
        ET_title = findViewById(R.id.itemName);
        ET_type = findViewById(R.id.itemType);
        ET_price = findViewById(R.id.itemPrice);
        btn_addItemMenu =findViewById(R.id.button_addMenuItem);
        itemImgV = findViewById(R.id.showPhotoItemG);
        itemImgG =findViewById(R.id.showPhotoItemV);
        pickerImage = findViewById(R.id.itemPicker);

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",0);
        }

        pickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_uploadImage = new Intent();
                intent_uploadImage.setType("image/*");
                intent_uploadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_uploadImage, 888);
            }
        });

        btn_addItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String title = ET_title.getText().toString();
                    String type = ET_type.getText().toString();
                    String price = ET_price.getText().toString();
                    addItemMenuDB(title, type, price);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri_path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_path);
                itemImgV.setImageBitmap(bitmap);
                itemImgV.setVisibility(View.VISIBLE);
                itemImgG.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String encoded_imageString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes_image = stream.toByteArray();
        return Base64.encodeToString(bytes_image, Base64.DEFAULT);
    }



    public  void addItemMenuDB(final String title, final String type, final String price)
    {
      request = new StringRequest(Request.Method.POST, addItemMenuURL, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  JSONObject jsonObject= new JSONObject(response);
                  if (jsonObject.names().get(0).equals("success"))
                  {
                      Toast.makeText(MerchantAddMenuItems.this, ""+jsonObject.get("success"),
                              Toast.LENGTH_LONG).show();
                      Intent in = new Intent(MerchantAddMenuItems.this,MerchantND.class);
                      startActivity(in);

                  }else if (jsonObject.names().get(0).equals("error"))
                  {
                      Toast.makeText(MerchantAddMenuItems.this, ""+jsonObject.get("error"),
                              Toast.LENGTH_LONG).show();
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Toast.makeText(MerchantAddMenuItems.this, error.getMessage(), Toast.LENGTH_SHORT).show();
              alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
              alertDialog.show();

          }
      }) {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              HashMap hashMap = new HashMap();
              hashMap.put("Place_id",""+placeId);
              hashMap.put("Menu_Title",title);
              hashMap.put("itemType",type);
              hashMap.put("Menu_Price",price);
              hashMap.put("encoded_ImageString",encoded_imageString(bitmap));
              return  hashMap ;
          }
      } ;
      requestQueue.add(request);
    }

    public boolean validate() {

        boolean valid = true;
        if(ET_title.getText().toString().matches("")||ET_title.length()>32){
            ET_title.setError("Enter Valid item Name");
            valid=false;
        }
        if(ET_type.getText().toString().matches("")||ET_type.length()>32){
            ET_type.setError("Enter Valid item Type");
            valid=false;
        }
        if(ET_price.getText().toString().matches("")||ET_price.length()>10){
            ET_price.setError("Enter Valid item Price");
            valid=false;
        }

        return valid;
    }
}
