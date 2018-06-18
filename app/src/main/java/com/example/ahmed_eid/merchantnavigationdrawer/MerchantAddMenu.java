package com.example.ahmed_eid.merchantnavigationdrawer;

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

import static android.widget.Toast.LENGTH_LONG;

public class MerchantAddMenu extends AppCompatActivity {
    ImageButton pickerImage ;
    ImageView imageGane,imageVisable;
    Button btn_addMenu ;
    Bitmap bitmap;

    private RequestQueue requestQueue;
    private StringRequest request;
    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;

    int placeId ;
    private final static String addMenuURL = "http://gp.sendiancrm.com/offerall/addMenu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_menu);

        pickerImage =(ImageButton) findViewById(R.id.PickImageMenu);
        imageGane =(ImageView) findViewById(R.id.showUploadedMenu);
        imageVisable =(ImageView) findViewById(R.id.showUploadedMenu2);
        btn_addMenu = findViewById(R.id.but_addMenu);

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",0);
        }

        pickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,77);
            }
        });

        btn_addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenuInDB();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 77 && resultCode == RESULT_OK && data !=null)
        {
           Uri imageURi = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageURi);
                imageVisable.setImageBitmap(bitmap);
                imageVisable.setVisibility(View.VISIBLE);
                imageGane.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public  String encoded_imageString(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes_image = stream.toByteArray();

        return Base64.encodeToString(bytes_image, Base64.DEFAULT);
    }

    public  void addMenuInDB()
    {
        request = new StringRequest(Request.Method.POST, addMenuURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(MerchantAddMenu.this,response, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    //String Response = jsonObject.getString("response");
                    //Toast.makeText(MerchantAddMenu.this, Response, Toast.LENGTH_SHORT).show();
                    Log.v("response",response.toString());
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(MerchantAddMenu.this, ""+jsonObject.get("success"),
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(MerchantAddMenu.this,MerchantND.class);
                        startActivity(in);

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(MerchantAddMenu.this, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error",error.toString());
                Toast.makeText(MerchantAddMenu.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Place_Id",""+placeId);
                hashMap.put("encoded_stringImg",encoded_imageString(bitmap));
                return  hashMap ;
            }
        };
        requestQueue.add(request);
    }


}
