package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Branch_Fragment_Points extends Fragment {

    View myView ;

    EditText ET_email,ET_points;
    Button btn_addUserPoint ;
    RequestQueue requestQueue ;
    StringRequest request ;
    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;
    int placeId ;
    private final static String addPointsURL = "http://gp.sendiancrm.com/offerall/addUserPoints.php";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.branch_fragment_points,container,false);

        ET_points = myView.findViewById(R.id.numOfPoint);
        ET_email = myView.findViewById(R.id.email);
        btn_addUserPoint =myView.findViewById(R.id.btn_addUserPoint);

        requestQueue = Volley.newRequestQueue(getActivity()) ;
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",0);
        }

        btn_addUserPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String pon = ET_points.getText().toString();
                    String em = ET_email.getText().toString();
                    addUserPointsDB(pon, em);
                }
            }
        });

        return myView;
    }

    public  Boolean validate(){

        boolean valid = true ;

       /* if (ET_email.getText().equals("")||!android.util.Patterns.EMAIL_ADDRESS.matcher(ET_email.getText()).matches()){
            ET_email.setError("Enter Valid Email Address!");
            valid = false ;
        }*/
        if (ET_email.getText().toString().matches("")){
            ET_email.setError("Enter User id");
            valid = false ;
        }

        if (ET_points.getText().toString().matches("")){
            ET_points.setError("Enter points To User");
            valid = false ;
        }

        return valid ;
    }
   public void addUserPointsDB(final String point , final String email){

       request = new StringRequest(Request.Method.POST,addPointsURL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONObject jsonObject= new JSONObject(response);
                   if (jsonObject.names().get(0).equals("success"))
                   {
                       Toast.makeText(getActivity(), ""+jsonObject.get("success"),
                               Toast.LENGTH_LONG).show();
                       Intent in = new Intent(getActivity(),MerchantBranches_SideMenu.class);
                       startActivity(in);

                   }else if (jsonObject.names().get(0).equals("error"))
                   {
                       Toast.makeText(getActivity(), ""+jsonObject.get("error"),
                               Toast.LENGTH_LONG).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
               alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
               alertDialog.show();

           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap hashMap = new HashMap();
               hashMap.put("Place_id",""+placeId);
               hashMap.put("point",point);
               hashMap.put("email",email);
               return  hashMap ;
           }
       } ;
       requestQueue.add(request);

    }
}
