package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
//import models.placebranch;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Merchant_Fragment_Branches extends Fragment {

    View myView ;
    ListView LV_branchesList ;
    private ImageView toAddBranch ;
    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;
    StringRequest request ;
    RequestQueue requestQueue ;
    private static  final String listOfBranchesURL = "http://gp.sendiancrm.com/offerall/showListOfBranches.php";
    private int placeId  ;

    ArrayList<String> listBranchNames ;
    ArrayList<Integer> listBranchId ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.merchant_fragment_branches,container,false);

        LV_branchesList = (ListView) myView.findViewById(R.id.branchList);
        toAddBranch = (ImageView) myView.findViewById(R.id.image_addBranch);
        listBranchNames = new ArrayList<>();
        listBranchId = new ArrayList<>();
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();
        requestQueue = Volley.newRequestQueue(getActivity());


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("logged in", false)) {
            placeId = sharedPreferences.getInt("PID", Integer.parseInt("0"));
        }
        showListOfBranchesFromDB(placeId);

        toAddBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MerchantAddBranch.class);
                startActivity(intent);
            }
        });

        LV_branchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView BName = (TextView) view.findViewById(R.id.Branch_Name) ;
                //Toast.makeText(getActivity(), BName.getText(), Toast.LENGTH_SHORT).show();
                String branchName =""+parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),BranchesSignIn.class);
                intent.putExtra("BranchName",branchName);
                startActivity(intent);

            }
        });


        return myView;
    }

    public void showListOfBranchesFromDB(final int place_Id){
        request = new StringRequest(Request.Method.POST, listOfBranchesURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.names().get(0).equals("empty"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.get("empty"), Toast.LENGTH_SHORT).show();
                    }
                    JSONArray branches = jsonObject.getJSONArray("branchs");
                    for(int i=0 ; i<branches.length();i++)
                    {
                        JSONObject branch = branches.getJSONObject(i);
                        //Toast.makeText(Merchant_Branches_list.this,""+branch, Toast.LENGTH_SHORT).show();
                        listBranchNames.add(branch.getString("Branch_name"));
                        listBranchId.add(Integer.parseInt(branch.getString("Branch_id")));
                    }
                    saveBNamesSharedPerf(listBranchNames);
                    loadBNamesSharedPerf();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Some Things Wrong",Toast.LENGTH_LONG).show();
                alertDialog.setMessage("حدث خطأ بالاتصال بالشبكه؟" +"\n"+"يجب عليك فتح النت؟");
                alertDialog.show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("placeId",""+place_Id);
                return hashMap;
            }
        };
       requestQueue.add(request);
    }

    public  void saveBNamesSharedPerf(ArrayList<String> listBranch)
    {
        StringBuilder sb =new  StringBuilder();
        for (String s : listBranch)
        {
            sb.append(s);
            sb.append(",");
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Branches_Names",sb.toString());
        editor.putBoolean("BRnames",true);
        editor.commit();
    }

    public void loadBNamesSharedPerf(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("BRnames",false)){

        String x =sharedPreferences.getString("Branches_Names",null);
        String[] s =x.split(",");
        //ArrayList<String> BNameFromSharedP = new ArrayList<String>();
       // for (int i=0 ;i<s.length;i++)
        //{BNameFromSharedP.add(s[i]);}
        ArrayList<BranchListItemShap> listnames =new ArrayList<>();
        for (int i=0 ; i<s.length ; i++)
        {
            listnames.add(new BranchListItemShap(s[i],i+1));

        }
        arrayAdaptorHandle adaptor = new arrayAdaptorHandle(getActivity(),listnames,listBranchId);
        LV_branchesList.setAdapter(adaptor);

       // ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,BNameFromSharedP);
        //LV_branchesList.setAdapter(adapter);
    }

    }
}
