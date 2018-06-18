package com.example.ahmed_eid.merchantnavigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchesSignIn extends AppCompatActivity {
    //TextView TV_branchName ;
    EditText ET_password ;
    placebranch classBranch ;
    Button btn_BranchSignIN;

    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;
    StringRequest request ;
    RequestQueue requestQueue ;
    int branchId ;
    private static  final String loginBranchesURL ="http://gp.sendiancrm.com/offerall/loginBranchEdit.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches_sign_in);

        alertDialog = new AlertDialog.Builder(this).create();
        requestQueue = Volley.newRequestQueue(this);

       // TV_branchName =(TextView) findViewById(R.id.bName) ;
        btn_BranchSignIN = (Button) findViewById(R.id.branchSignIn) ;
        ET_password = (EditText) findViewById(R.id.branchpassword);

        Bundle b = getIntent().getExtras();
        branchId = b.getInt("OBId");
        Toast.makeText(this, ""+branchId, Toast.LENGTH_SHORT).show();
        btn_BranchSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                {
                    String password = ET_password.getText().toString();
                    loginBranch(password);
                }
            }
        });

    }
    public  Boolean validate(){

        boolean valid = true ;

        if (ET_password.getText().toString().matches("")){ // || ET_password.length()<8
            ET_password.setError("Enter Valid Password!");
            valid = false ;
        }

        return valid ;
    }

    public  void loginBranch(final String Bpassword) {

        request = new StringRequest(Request.Method.POST, loginBranchesURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.names().get(0).equals("empty"))
                    {
                        Toast.makeText(getApplicationContext(), ""+jsonObject.get("empty"), Toast.LENGTH_SHORT).show();
                    }

                    JSONArray branches = jsonObject.getJSONArray("branchEs");
                    JSONObject branch = branches.getJSONObject(0);

                    classBranch = new placebranch();
                    classBranch.setBranchName((String)branch.getString("Branch_name"));
                    classBranch.setID(Integer.parseInt((String) branch.getString("Branch_id")));
                    classBranch.setPlace_id(Integer.parseInt((String) branch.getString("Place_id")));
                    classBranch.setRewordSystem(Integer.parseInt((String) branch.getString("RewardSystemAvailabilty")));
                    classBranch.setPassword((String)branch.getString("Branch_Password"));
                    classBranch.setPhonee((String)branch.getString("Branch_phone"));
                    classBranch.setPhoto((String)branch.getString("Branch_image"));
                    //classBranch.setLatitude(Float.parseFloat((String) branch.getString("latitude")));
                    //classBranch.setLongitude(Float.parseFloat((String) branch.getString("longitude")));

                    BranchSesionStart(classBranch);

                    if (sharedPreferences != null)
                    {
                        Intent intent = new Intent(getApplicationContext(), MerchantBranches_SideMenu.class);
                        startActivity(intent);

                        alertDialog.setMessage("Welcome: "+sharedPreferences.getString("BName",null)+" "+"Branch");
                        alertDialog.show();
                    }else {
                        Toast.makeText(getApplicationContext(), "sharedPreferences=null", Toast.LENGTH_SHORT).show();
                        alertDialog.setMessage("Wrong UserName Or Password");
                        alertDialog.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "some thing wrong",Toast.LENGTH_LONG).show();
                alertDialog.setMessage("حدث خطأ بالاتصال بالشبكه؟" +"\n"+"يجب عليك فتح النت؟");
                alertDialog.show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap =new HashMap<>();
                hashMap.put("password",Bpassword);
                hashMap.put("branchid",""+branchId);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    public void BranchSesionStart(placebranch branch){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("BId",branch.getID());
        editor.putString("BName",branch.getBranchName());
        editor.putInt("BplaceId",branch.getPlace_id());
        editor.putString("BPassword",branch.getPassword());
        editor.putInt("rewordSystem",branch.getRewordSystem());
        editor.putString("BPhone",branch.getPhonee());
        editor.putString("BPhoto",branch.getPhoto());
        editor.putBoolean("loginBranch",true);
        //editor.putFloat("Blatitude",branch.getLatitude());
        //editor.putFloat("BLongitude",branch.getLongitude());
        editor.commit();
    }

}
