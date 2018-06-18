package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

public class DeleteBranch {

        RequestQueue requestQueue ;
        StringRequest request ;
        android.app.AlertDialog alertDialog;
        SharedPreferences sharedPreferences ;
        private  String DeleteBranchURL = "http://gp.sendiancrm.com/offerall/deleteBranch.php";

        Context context;

        public DeleteBranch(Context context) {
            this.context = context;
        }

        public  void deleteBranchDB(final int branchId){
            requestQueue = Volley.newRequestQueue(context) ;
            alertDialog = new android.app.AlertDialog.Builder(context).create();

            request =new StringRequest(Request.Method.POST,DeleteBranchURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success"))
                        {
                            Toast.makeText(context, ""+jsonObject.get("success"),
                                    Toast.LENGTH_LONG).show();

                        }else if (jsonObject.names().get(0).equals("error"))
                        {
                            Toast.makeText(context, ""+jsonObject.get("error"),
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("حدث خطأ لا يوجد Rsponse؟" +"\n"+"قد يكون خطأ فى اتصال بالشبكه؟");
                    alertDialog.show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap hashMap = new HashMap();
                    hashMap.put("branch_id",""+branchId);
                    return  hashMap ;
                }
            };
            requestQueue.add(request);
        }

}
