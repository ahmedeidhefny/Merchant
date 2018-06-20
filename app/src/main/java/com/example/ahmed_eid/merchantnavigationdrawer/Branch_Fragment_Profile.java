package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

public class Branch_Fragment_Profile extends Fragment {

    View myView ;
    TextView TV_password,TV_adress,TV_phone;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    ImageButton btn_edit ;
    ImageView img;

    RequestQueue requestQueue ;
    StringRequest request ;

    private  final String getBranchURL = "http://gp.sendiancrm.com/offerall/getBranchData.php";

   /* private  String pName ;
    private  int categoryId;
    private String email;
    private String photo,phone;*/
    int branchId;

    SharedPreferences sharedPreferences ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.branch_fragment_profile,container,false);

        collapsingToolbarLayout = myView.findViewById(R.id.collapsing_toolbar);
        img = myView.findViewById(R.id.pImg2);
        TV_password = myView.findViewById(R.id.passw);
        TV_adress = myView.findViewById(R.id.addrs);
        TV_phone = myView.findViewById(R.id.phone);
        btn_edit = myView.findViewById(R.id.editProfile2);

        requestQueue = Volley.newRequestQueue(getActivity()) ;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("loginBranch",false)){

            branchId = sharedPreferences.getInt("BId",0);
            //Toast.makeText(getActivity(), ""+branchId, Toast.LENGTH_SHORT).show();
        }

        getbranchDB(branchId);
        return myView;
    }


    public  void getbranchDB(final int branchid){

        request =new StringRequest(Request.Method.POST,getBranchURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONArray("branchData");
                    JSONObject category = categories.getJSONObject(0);
                    final String name = (String)category.getString("Branch_name");
                    final String pass = (String)category.getString("Branch_Password");
                    final String add = (String)category.getString("Address");
                    final String phone = (String)category.getString("Branch_phone");
                    final String imge = (String)category.getString("Branch_image");
                    TV_password.setText(pass);
                    TV_adress.setText(add);
                    TV_phone.setText(phone);
                    collapsingToolbarLayout.setTitle(name);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(imge)
                            .into(img);
                    btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(getActivity(),EditBranchProfile.class);
                            in.putExtra("bn",name);
                            in.putExtra("adds",add);
                            in.putExtra("pas",pass);
                            in.putExtra("phon",phone);
                            in.putExtra("im",imge);
                            in.putExtra("bid",branchId);
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("branchID",""+branchid);
                return  hashMap ;
            }
        };
        requestQueue.add(request);
    }
}
