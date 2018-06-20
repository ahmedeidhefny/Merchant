package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Merchant_Fragment_Profile extends Fragment {

    View myView ;
    TextView TV_email,TV_pass,TV_cat;

    CollapsingToolbarLayout collapsingToolbarLayout ;
    ImageView img ;
    ImageButton btn_edit ;

    private String pName ;
    private int categoryId;
    private String categoryName;
    private String categorDes;
    private String emails;
    private String password;
    private String photo;

    RequestQueue requestQueue ;
    StringRequest request ;
    private  final String GetOneCategoryURL = "http://gp.sendiancrm.com/offerall/getCategoryById.php";
    private  final String GetMerchantDataURL = "http://gp.sendiancrm.com/offerall/getMerchantData.php";
    int placeId ;

    SharedPreferences sharedPreferences ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.merchant_fragment_profile,container,false);

        collapsingToolbarLayout = myView.findViewById(R.id.collapsing_toolbar);
        img = myView.findViewById(R.id.pImg);
        TV_cat = myView.findViewById(R.id.ctg2);
        TV_pass = myView.findViewById(R.id.pass);
        TV_email = myView.findViewById(R.id.ctg5);
        btn_edit = myView.findViewById(R.id.editProfile);

        requestQueue = Volley.newRequestQueue(getActivity()) ;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",0);
        }

        getDataProfileDB(placeId);


        return myView;
    }

    public  void getOneCategory(final int catId){

        request =new StringRequest(Request.Method.POST,GetOneCategoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONArray("categoryDB");
                    JSONObject category = categories.getJSONObject(0);
                  String categoryName1 = (String)category.getString("Name");
                  categorDes = (String)category.getString("Discription");
                    TV_cat.setText(categoryName1);
                    categoryName = categoryName1 ;
                    //TV_Descript.setText(categorDes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("catId",""+catId);
                return  hashMap ;
            }
        };
        requestQueue.add(request);
    }

    public void getDataProfileDB(final int place_Id)
    {
        StringRequest request2 =new StringRequest(Request.Method.POST,GetMerchantDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray places = jsonObject.getJSONArray("placesData");
                    JSONObject place = places.getJSONObject(0);
                    pName = (String)place.getString("PLaceName");
                    categoryId = Integer.parseInt(place.getString("Category_id"));
                    emails = (String)place.getString("PlaceEmail");
                    photo = (String)place.getString("Place_LogoPhoto");
                    password = (String)place.getString("PlacePassword");
                    TV_pass.setText(password);
                    TV_email.setText(emails);
                    collapsingToolbarLayout.setTitle(pName);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(photo)
                            .into(img);
                    getOneCategory(categoryId);
                    btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(getActivity(),EditMershantProfile.class);
                            in.putExtra("n",pName);
                            in.putExtra("cn",categoryName);
                            in.putExtra("em",emails);
                            in.putExtra("pa",password);
                            in.putExtra("pid",place_Id);
                            startActivity(in);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Place_Id",""+place_Id);
                return  hashMap ;
            }
        };
        requestQueue.add(request2);

    }



}
