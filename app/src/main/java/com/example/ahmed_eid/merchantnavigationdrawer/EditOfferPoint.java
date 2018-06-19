package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditOfferPoint extends AppCompatActivity {

    EditText ET_title,ET_type,ET_price,ET_points,ET_starD,ET_endD;
    Button btn_editOfferpoints ;
    ImageView itemImgV,itemImgG,calImgStar,calImgEnd ;
    private int mYear;
    private int mMonth;
    private int mDay;

    ImageButton pickerImage ;
    Bitmap bitmap ;
    private SimpleDateFormat dateFormat ;
    private String encoded_image ="" ;

    RequestQueue requestQueue ;
    StringRequest request ;
    AlertDialog alertDialog;

    int offerId ;
    SharedPreferences sharedPreferences ;
    private  String editOfferPointsURL = "http://gp.sendiancrm.com/offerall/editOfferPoints.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer_point);

        ET_title = findViewById(R.id.EofferItemName);
        ET_type = findViewById(R.id.EOfferItemType);
        ET_price = findViewById(R.id.EitemNewPrice);
        ET_points = findViewById(R.id.EitemPoints);
        ET_starD = findViewById(R.id.EofferstarDate2);
        ET_endD = findViewById(R.id.EofferendDate2);
        itemImgV = findViewById(R.id.EshowofferG);
        itemImgG =findViewById(R.id.EshowofferV);
        pickerImage = findViewById(R.id.EPickImageOffer);
        calImgStar = findViewById(R.id.Ecal_star);
        calImgEnd = findViewById(R.id.Ecal_end);
        btn_editOfferpoints =findViewById(R.id.btn_editOfferPoint);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        requestQueue = Volley.newRequestQueue(this) ;
        alertDialog = new android.app.AlertDialog.Builder(this).create();

        Bundle b = getIntent().getExtras();

        final String starDate = b.getString("str2");
        final String endDate = b.getString("end2");
        final String title = b.getString("ti2");
        final String type = b.getString("ty2");
        final Float price = b.getFloat("pr2");
        final int points = b.getInt("poin");
        offerId = b.getInt("id2");


        ET_starD.setText(starDate);
        ET_endD.setText(endDate);
        ET_title.setText(title);
        ET_type.setText(type);
        ET_price.setText(""+price);
        ET_points.setText(points+"");

        Toast.makeText(this, ""+offerId, Toast.LENGTH_SHORT).show();

        btn_editOfferpoints.setOnClickListener(new View.OnClickListener() {
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
                        editOfferItemPointsDB(title, type, price,pos,star,end);
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
                startActivityForResult(intent_uploadImage, 235);
            }
        });

        calImgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog =new DatePickerDialog(EditOfferPoint.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog =new DatePickerDialog(EditOfferPoint.this, new DatePickerDialog.OnDateSetListener() {
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
        if (requestCode == 235 && resultCode == RESULT_OK && data != null) {
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

    public void editOfferItemPointsDB(final String title, final String type, final String price, final String pos,final String star, final String end)
    {
        request = new StringRequest(Request.Method.POST,editOfferPointsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(EditOfferPoint.this, ""+jsonObject.get("success"),
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(EditOfferPoint.this,MerchantBranches_SideMenu.class);
                        startActivity(in);

                    }else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(EditOfferPoint.this, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditOfferPoint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                alertDialog.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("offerIds",""+offerId);
                hashMap.put("ETitle2",title);
                hashMap.put("EType2",type);
                hashMap.put("Eprice2",price);
                hashMap.put("Epoints",pos);
                hashMap.put("EstarDate2",star);
                hashMap.put("EendDate2",end);
                hashMap.put("Eencoded_Image",encoded_image);
                return  hashMap ;
            }
        } ;
        requestQueue.add(request);

    }


}
