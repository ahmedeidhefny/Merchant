package com.example.ahmed_eid.merchantnavigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MerchantSignUp extends AppCompatActivity {

    private ImageView imageUpload, imageUploadedGane;
    private String encoded_ImageString;
    private Bitmap bitmap;
    AlertDialog alertDialog;

    private EditText ET_email, ET_password, ET_PlaceName;

    private RequestQueue requestQueue;
    private StringRequest request;
    private final static String registerPlaceURL = "http://gp.sendiancrm.com/offerall/registerPlace.php";
    Button btn_register;

    private int spinner_position = 1;
    private MaterialBetterSpinner betterSpinner;

   // ArrayList<Category> Allcategories;
    ArrayList<String> categoriesName;
    //String[] catogry = {"cloth and fashion", "sports and fitness", "supermarkets", "restaurants"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_sign_up);

        betterSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerCat);
        ET_email = (EditText) findViewById(R.id.Pemail);
        ET_password = (EditText) findViewById(R.id.Ppassword);
        ET_PlaceName = (EditText) findViewById(R.id.PbrandName);
        imageUpload = (ImageView) findViewById(R.id.PickImagePlace);
        imageUploadedGane = (ImageView) findViewById(R.id.placeUploadedImage);
        btn_register = (Button) findViewById(R.id.button_signup);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        alertDialog = new AlertDialog.Builder(this).create();

        GetCategoryFromDB categoryFromDB = new GetCategoryFromDB(this);
        categoriesName = categoryFromDB.getCategoryFromDBs();

       /* for (int i = 0 ; i<Allcategories.size() ; i++)
        {

            String name = Allcategories.get(i).getName();
            categoriesName.add(name);
        }*/

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,categoriesName);
        betterSpinner.setAdapter(arrayAdapter);


        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_uploadImage = new Intent();
                intent_uploadImage.setType("image/*");
                intent_uploadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_uploadImage, 777);

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String pName = ET_PlaceName.getText().toString();
                    String pEmail = ET_email.getText().toString();
                    String ppassword = ET_password.getText().toString();
                    register_Place(pName, pEmail, ppassword);
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 777 && resultCode == RESULT_OK && data != null) {
            Uri uri_path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_path);
                imageUploadedGane.setImageBitmap(bitmap);
                imageUploadedGane.setVisibility(View.VISIBLE);
               // Toast.makeText(this, encoded_imageString(bitmap), Toast.LENGTH_SHORT).show();
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

    public void register_Place(final String pName, final String PEmail, final String Ppassword) {

        request = new StringRequest(Request.Method.POST, registerPlaceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("success"),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MerchantSignIn.class);
                        startActivity(intent);
                        alertDialog.setMessage("Now ^_^ You Can Login..");
                        alertDialog.show();

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("error"),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong",Toast.LENGTH_LONG).show();
                alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",PEmail);
                hashMap.put("password",Ppassword);
                hashMap.put("placeName",pName);
                hashMap.put("category_id", ""+spinner_position);
                hashMap.put("encoded_ImageString",encoded_imageString(bitmap));
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    public boolean validate() {

        boolean valid = true;

        if(ET_email.getText().equals("")||!android.util.Patterns.EMAIL_ADDRESS.matcher(ET_email.getText()).matches()){
            ET_email.setError("Enter Valid Email Address");
            valid=false;
        }
        if(ET_password.getText().toString().matches("")||ET_password.length()<8){
            ET_password.setError("Enter password At Least 8 CharS");
            valid=false;
        }
        if(ET_PlaceName.getText().toString().matches("")||ET_PlaceName.length()>32){
            ET_PlaceName.setError("please enter Valid Name");
            valid=false;
        }

        return valid;
    }
}
