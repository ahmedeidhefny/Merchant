package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MerchantAddBranch extends AppCompatActivity {

    EditText ET_Bname,ET_Bpassword,ET_Bphone;
    ImageButton btn_placePicker ;
    Button btn_addBranch ;
    private final int PLACE_PICKER_REQUEST=1;
    TextView TV_addressGane ;

    double latitude,longitude;
    String address;
    int checked=0 ;
    CheckBox checkBox  ;

    RequestQueue requestQueue ;
    StringRequest request ;
    AlertDialog alertDialog;
    int placeId ;
    SharedPreferences sharedPreferences ;
    private  String addBranchURL = "http://gp.sendiancrm.com/offerall/addBranch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_branch);
        ET_Bname = findViewById(R.id.branchName);
        ET_Bpassword = findViewById(R.id.branchPass);
        ET_Bphone = findViewById(R.id.branchPhone);
        btn_placePicker = (ImageButton) findViewById(R.id.PickLocation);
        TV_addressGane = findViewById(R.id.location);
        btn_addBranch = findViewById(R.id.btn_addBranch);
        checkBox = findViewById(R.id.checkbox);

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",55);
        }

        btn_placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOGoogleMap();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = 1;
            }
        });

        btn_addBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String name = ET_Bname.getText().toString();
                    String pass = ET_Bpassword.getText().toString();
                    String phone = ET_Bphone.getText().toString();
                    addBranchDB(name, pass, phone);
                }
            }
        });

    }


    public  void  goTOGoogleMap(){

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
       try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK )
        {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }
    public  void  displaySelectedPlaceFromPlacePicker(Intent data)
    {
        Place placeSelected = PlacePicker.getPlace(data, this);
        address = placeSelected.getAddress().toString();
        latitude = placeSelected.getLatLng().latitude;
        longitude = placeSelected.getLatLng().longitude;
        TV_addressGane.setText(address);
        TV_addressGane.setVisibility(View.VISIBLE);
        btn_placePicker.setImageResource(R.drawable.editl2);
    }

    public  void addBranchDB(final String name, final String pass, final String  phone)
    {
        request = new StringRequest(Request.Method.POST, addBranchURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(MerchantAddBranch.this, response, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject= new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(MerchantAddBranch.this, ""+jsonObject.get("success"),
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(MerchantAddBranch.this,MerchantND.class);
                        startActivity(in);

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(MerchantAddBranch.this, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MerchantAddBranch.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                alertDialog.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("PlaceIDs",""+placeId);
                hashMap.put("address",address);
                hashMap.put("latitude",""+latitude);
                hashMap.put("longitude",""+longitude);
                hashMap.put("name",name);
                hashMap.put("pass",pass);
                hashMap.put("phone",phone);
                hashMap.put("check",""+checked);
                return  hashMap ;
            }
        } ;
        requestQueue.add(request);
    }

    public boolean validate() {

        boolean valid = true;
        if(ET_Bname.getText().toString().matches("")||ET_Bname.length()>32){
            ET_Bname.setError("Enter Valid Branch Name");
            valid=false;
        }
        if(ET_Bpassword.getText().toString().matches("")||ET_Bpassword.length()<8){
            ET_Bpassword.setError("Enter Valid Branch Password");
            valid=false;
        }
        if(ET_Bphone.getText().toString().matches("")||ET_Bphone.length()>20){
            ET_Bphone.setError("Enter Valid item Price");
            valid=false;
        }

        return valid;
    }

}
