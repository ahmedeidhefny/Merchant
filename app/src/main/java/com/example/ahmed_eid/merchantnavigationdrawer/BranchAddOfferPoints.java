package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BranchAddOfferPoints extends AppCompatActivity {

    EditText ET_title,ET_type,ET_price,ET_points,ET_starD,ET_endD;
    Button btn_addOfferpoints ;
    ImageView itemImgV,itemImgG,calImgStar,calImgEnd ;
    private int mYear;
    private int mMonth;
    private int mDay;

    ImageButton pickerImage ;
    Bitmap bitmap ;
    private String encoded_image ="" ;
    private SimpleDateFormat dateFormat ;

    RequestQueue requestQueue ;
    StringRequest request ;
    AlertDialog alertDialog;

    int branchId ;
    SharedPreferences sharedPreferences ;
    private  String addOfferPointsURL = "http://gp.sendiancrm.com/offerall/addOfferPoints.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_add_offer_points);

        ET_title = findViewById(R.id.offerItemName);
        ET_type = findViewById(R.id.OfferItemType);
        ET_price = findViewById(R.id.itemNewPrice);
        ET_points = findViewById(R.id.itemPoints);
        ET_starD = findViewById(R.id.offerstarDate2);
        ET_endD = findViewById(R.id.offerendDate2);
        itemImgV = findViewById(R.id.showofferG);
        itemImgG =findViewById(R.id.showofferV);
        pickerImage = findViewById(R.id.PickImageOffer);
        calImgStar = findViewById(R.id.cal_star);
        calImgEnd = findViewById(R.id.cal_end);
        btn_addOfferpoints =findViewById(R.id.btn_addOfferPoint);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("loginBranch",false)){

            branchId = sharedPreferences.getInt("BId",0);
            //Toast.makeText(getActivity(), ""+branchId, Toast.LENGTH_SHORT).show();
        }

        btn_addOfferpoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validate()) {
                        String title = ET_title.getText().toString();
                        String type = ET_type.getText().toString();
                        String price = ET_price.getText().toString();
                        String pos = ET_points.getText().toString();
                        String star = ET_starD.getText().toString();
                        String end = ET_endD.getText().toString();
                        addOfferItemPointsDB(title, type, price,pos,star,end);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        pickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_uploadImage = new Intent();
                intent_uploadImage.setType("image/*");
                intent_uploadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_uploadImage, 999);
            }
        });

     calImgStar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             Calendar calendar = Calendar.getInstance();
             mYear = calendar.get(Calendar.YEAR);
             mMonth = calendar.get(Calendar.MONTH);
             mDay = calendar.get(Calendar.DAY_OF_MONTH);
             DatePickerDialog datePickerDialog =new DatePickerDialog(BranchAddOfferPoints.this, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     month+=1;
                  ET_starD.setText(year+"-"+month+"-"+dayOfMonth);
                 }
             }, mYear, mMonth, mDay);
             datePickerDialog.show();

         }
     });

     calImgEnd.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Calendar calendar = Calendar.getInstance();
             mYear = calendar.get(Calendar.YEAR);
             mMonth = calendar.get(Calendar.MONTH);
             mDay = calendar.get(Calendar.DAY_OF_MONTH);
             DatePickerDialog datePickerDialog =new DatePickerDialog(BranchAddOfferPoints.this, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     month+=1;
                     ET_endD.setText(year+"-"+month+"-"+dayOfMonth);
                 }
             }, mYear, mMonth, mDay);
             datePickerDialog.show();

         }
     });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
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


    public boolean validate() throws ParseException {

        boolean valid = true;
        if(ET_title.getText().toString().matches("")||ET_title.length()>32){
            ET_title.setError("Enter Valid offer Name");
            valid=false;
        }
        if(ET_type.getText().toString().matches("")||ET_type.length()>32){
            ET_type.setError("Enter Valid offer Type");
            valid=false;
        }
        if(ET_price.getText().toString().matches("")||ET_price.length()>8){
            ET_price.setError("Enter Valid offer Price");
            valid=false;
        }
        if(ET_points.getText().toString().matches("")||ET_points.length()>8){
            ET_points.setError("Enter Valid offer discount");
            valid=false;
        }
        if(ET_starD.getText().toString().matches("")){
            ET_starD.setError("Enter Valid star Date");
            valid=false;
        }
        if(ET_endD.getText().toString().matches("")){
            ET_endD.setError("Enter Valid end Date");
            valid=false;
        }
        String stardate= ET_starD.getText().toString();
        String enddate= ET_endD.getText().toString();

        if (dateFormat.parse(stardate).after(dateFormat.parse(enddate)))
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Star Date After End Date Not Valid !"+"\n"+"Please..You Must Edit ^_^");
            dlgAlert.setTitle("Attention! ");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            valid=false;
        }


        return valid;
    }

    public void addOfferItemPointsDB(final String title, final String type, final String price, final String pos,final String star, final String end)
    {
        request = new StringRequest(Request.Method.POST,addOfferPointsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(BranchAddOfferPoints.this, ""+jsonObject.get("success"),
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(BranchAddOfferPoints.this,MerchantBranches_SideMenu.class);
                        startActivity(in);

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(BranchAddOfferPoints.this, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BranchAddOfferPoints.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                alertDialog.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("branch_id",""+branchId);
                hashMap.put("Title2",title);
                hashMap.put("Type2",type);
                hashMap.put("price2",price);
                hashMap.put("points",pos);
                hashMap.put("starDate2",star);
                hashMap.put("endDate2",end);
                hashMap.put("encoded_ImageString",encoded_image);
                return  hashMap ;
            }
        } ;
        requestQueue.add(request);

    }

}
