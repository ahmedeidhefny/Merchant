package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditMershantProfile extends AppCompatActivity {

    private ImageView imageUpload, imageUploadedGane;
    private Bitmap bitmap;
    AlertDialog alertDialog;

    private EditText ET_email, ET_password, ET_PlaceName;
    TextView ts ;
    private String encoded_image ="" ;
    private RequestQueue requestQueue;
    private StringRequest request;
    private final static String editPlaceURL = "http://gp.sendiancrm.com/offerall/editPlaceProfile.php";
    private  final String GetCategoryURL = "http://gp.sendiancrm.com/offerall/getCategory.php";
    Button btn_EditProfile;

    private int spinner_position = 1;
    private Spinner spinner;

    ArrayList<String> categoriesName =new ArrayList<>();
    ArrayList<Integer> categoriesId= new ArrayList<>();

    int placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mershant_profile);
        spinner = (Spinner) findViewById(R.id.Espinner);
        ET_email = (EditText) findViewById(R.id.EPemail);
        ET_password = (EditText) findViewById(R.id.EPpassword);
        ET_PlaceName = (EditText) findViewById(R.id.EPbrandName);
        imageUpload = (ImageView) findViewById(R.id.EPickImagePlace);
        imageUploadedGane = (ImageView) findViewById(R.id.EplaceUploadedImage);
        btn_EditProfile = (Button) findViewById(R.id.Ebutton_signup);
        ts = findViewById(R.id.tvsp);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        alertDialog = new AlertDialog.Builder(this).create();

        Bundle b = getIntent().getExtras();
        final String name = b.getString("n");
        final String catname = b.getString("cn");
        final String email = b.getString("em");
        final String password = b.getString("pa");
        placeId = b.getInt("pid");

        ET_email.setText(email);
        ET_password.setText(password);
        ET_PlaceName.setText(name);
        ts.setText(catname);

        getCategoriesDB();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,categoriesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_uploadImage = new Intent();
                intent_uploadImage.setType("image/*");
                intent_uploadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_uploadImage, 837);

            }
        });


        btn_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String pName = ET_PlaceName.getText().toString();
                    String pEmail = ET_email.getText().toString();
                    String ppassword = ET_password.getText().toString();
                    edit_Place(pName, pEmail, ppassword);
                }


            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ts.setText(""+parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 837 && resultCode == RESULT_OK && data != null) {
            Uri uri_path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_path);
                imageUploadedGane.setImageBitmap(bitmap);
                imageUploadedGane.setVisibility(View.VISIBLE);
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

    public void edit_Place(final String pName, final String PEmail, final String Ppassword) {

        request = new StringRequest(Request.Method.POST, editPlaceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("success"),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MerchantND.class);
                        startActivity(intent);
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
                hashMap.put("place_id",""+placeId);
                hashMap.put("Eemail",PEmail);
                hashMap.put("Epassword",Ppassword);
                hashMap.put("EplaceName",pName);
                hashMap.put("Ecategory_id", ""+spinner_position);
                hashMap.put("Eencoded_ImageString",encoded_image);
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

    public  void getCategoriesDB()
    {
        StringRequest request2 = new StringRequest(Request.Method.POST, GetCategoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                    JSONArray categories = jsonObject.getJSONArray("categoriesDB");

                    for (int i=0 ; i< categories.length() ; i++)
                    {
                        JSONObject category = categories.getJSONObject(i);

                        String categoryName = (String)category.getString("Name");
                        categoriesName.add(categoryName);

                        int categoryid = Integer.parseInt(category.getString("Category_ID"));
                        categoriesId.add(categoryid);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request2);

    }

}
