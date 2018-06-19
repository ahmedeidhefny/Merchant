package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

public class EditMenuItem extends AppCompatActivity {

    private  EditText ET_title,ET_type,ET_price;
    private  ImageView itemImgV,itemImgG ;
    private  ImageButton pickerImage ;
    private  Button btn_addItemMenu ;
    private Bitmap bitmap ;
    private String encoded_image ="" ;
    private RequestQueue requestQueue ;
    private StringRequest request ;
    AlertDialog alertDialog;
    int itemId ;
    SharedPreferences sharedPreferences ;
    private  String EditItemMenuURL = "http://gp.sendiancrm.com/offerall/editItemMenu.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);
        ET_title = findViewById(R.id.EitemName);
        ET_type = findViewById(R.id.EitemType);
        ET_price = findViewById(R.id.EitemPrice);
        btn_addItemMenu =findViewById(R.id.button_EditMenuItem);
        itemImgV = findViewById(R.id.EshowPhotoItemG);
        itemImgG =findViewById(R.id.EshowPhotoItemV);
        pickerImage = findViewById(R.id.EitemPicker);

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();

        Bundle b = getIntent().getExtras();
        final String title = b.getString("ItemTitle");
        final String type = b.getString("ItemType");
        final Float price = b.getFloat("ItemPrice");
        itemId = b.getInt("ItemID");

        ET_price.setText(""+price);
        ET_title.setText(title);
        ET_type.setText(type);
        Toast.makeText(this, ""+itemId, Toast.LENGTH_SHORT).show();

        pickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_uploadImage = new Intent();
                intent_uploadImage.setType("image/*");
                intent_uploadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_uploadImage, 256);
            }
        });


        btn_addItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String title = ET_title.getText().toString();
                    String type = ET_type.getText().toString();
                    String price = ET_price.getText().toString();
                    EditItemMenuDB(title, type, price);
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 256 && resultCode == RESULT_OK && data != null) {
            Uri uri_path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_path);
                itemImgV.setImageBitmap(bitmap);
                itemImgV.setVisibility(View.VISIBLE);
                itemImgG.setVisibility(View.GONE);
                if (bitmap != null)
                {
                    encoded_image = encoded_imageString(bitmap);
                }
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

    public  void EditItemMenuDB(final String title, final String type, final String price)
    {
        request = new StringRequest(Request.Method.POST, EditItemMenuURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(EditMenuItem.this, ""+jsonObject.get("success"),
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(EditMenuItem.this,MerchantND.class);
                        startActivity(in);

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(EditMenuItem.this, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditMenuItem.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.setMessage(""+error.getMessage());
                alertDialog.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("EitemId",""+itemId);
                hashMap.put("EMenu_Title",title);
                hashMap.put("EitemType",type);
                hashMap.put("EMenu_Price",price);
                hashMap.put("Eencoded_ImageString",encoded_image);
                return  hashMap ;
            }
        } ;
        requestQueue.add(request);
    }

}
