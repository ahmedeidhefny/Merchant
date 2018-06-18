package com.example.ahmed_eid.merchantnavigationdrawer;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCategoryFromDB {

    RequestQueue requestQueue ;
    StringRequest request ;
    ArrayList<String> categoryList;
    Context context ;
    private  final String GetCategoryURL = "http://gp.sendiancrm.com/offerall/getCategory.php";

    public GetCategoryFromDB(Context context) {
        this.context = context;
    }

    public ArrayList<String> getCategoryFromDBs ()
    {
        requestQueue = Volley.newRequestQueue(context) ;
        categoryList = new ArrayList<String>();

        request = new StringRequest(Request.Method.POST, GetCategoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(context, ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                    JSONArray categories = jsonObject.getJSONArray("categoriesDB");

                    for (int i=0 ; i< categories.length() ; i++)
                    {
                        JSONObject category = categories.getJSONObject(i);

                        String categoryName = (String)category.getString("Name");
                        categoryList.add(categoryName);

                        /*int Cat_id = Integer.parseInt((String)category.getString("Category_ID"));
                        String categoryName = (String)category.getString("Name");
                        String categoryDES =(String)category.getString("Discription") ;
                        categoryList.add(new Category(Cat_id,categoryName,categoryDES));*/
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
        requestQueue.add(request);

        return categoryList;

    }


}
