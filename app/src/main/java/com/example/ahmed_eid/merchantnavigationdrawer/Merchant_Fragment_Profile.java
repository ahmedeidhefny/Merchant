package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView TV_email,TV_Descript,TV_cat;
    CollapsingToolbarLayout collapsingToolbarLayout ;

    RequestQueue requestQueue ;
    StringRequest request ;
    private  final String GetOneCategoryURL = "http://gp.sendiancrm.com/offerall/getCategoryById.php";

    String pName ;
    int categoryId;
    String email;
    String photo;

    SharedPreferences sharedPreferences ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.merchant_fragment_profile,container,false);
        collapsingToolbarLayout = myView.findViewById(R.id.collapsing_toolbar);
        ImageView img = myView.findViewById(R.id.pImg);
        TV_cat = myView.findViewById(R.id.ctg2);
        TV_Descript = myView.findViewById(R.id.desc2);
        TV_email = myView.findViewById(R.id.ctg5);


        requestQueue = Volley.newRequestQueue(getActivity()) ;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sharedPreferences.getBoolean("logged in",false)){

            pName = sharedPreferences.getString("PName","place Name");
            categoryId = sharedPreferences.getInt("PcategoryId",1);
            email = sharedPreferences.getString("PEmail","Email");
            photo = sharedPreferences.getString("Pphoto",null);
        }

        getOneCategory(categoryId);
        TV_email.setText(email);
        collapsingToolbarLayout.setTitle(pName);
        Glide.with(getActivity())
                .asBitmap()
                .load(photo)
                .into(img);

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
                    String categoryName = (String)category.getString("Name");
                    String categorDes = (String)category.getString("Discription");
                    TV_cat.setText(categoryName);
                    TV_Descript.setText(categorDes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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



}
