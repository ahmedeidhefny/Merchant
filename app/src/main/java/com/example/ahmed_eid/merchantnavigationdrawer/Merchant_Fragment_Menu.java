package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class Merchant_Fragment_Menu extends Fragment {

    View myView ;

    RequestQueue requestQueue ;
    StringRequest request ;
    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;
    int placeId ;
    private  final String GetMenuURL = "http://gp.sendiancrm.com/offerall/getMenu.php";
    private  final String GetMenuItemsURL = "http://gp.sendiancrm.com/offerall/getMenuItems.php";


    ArrayList<String> itemsImglist = new ArrayList<>();
    ArrayList<String> titleslist = new ArrayList<>();
    ArrayList<String> markslist = new ArrayList<>();
    ArrayList<Float> pricesList = new ArrayList<>();
    ArrayList<Integer> itemIdList = new ArrayList<>();

    ArrayList<String> menuImages = new ArrayList<>();
    ArrayList<Integer> menuIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.merchant_fragment_menu,container,false);
        ImageView btn_addMenuItem = myView.findViewById(R.id.addMenuItem);
        ImageView btn_addMenu = myView.findViewById(R.id.addMenu);

        requestQueue = Volley.newRequestQueue(getActivity()) ;
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sharedPreferences.getBoolean("logged in",false)){

            placeId = sharedPreferences.getInt("PID",0);
        }

        btn_addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MerchantAddMenuItems.class);
                startActivity(intent);
            }
        });

        btn_addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MerchantAddMenu.class);
                startActivity(intent);

            }
        });

        getMenusFromDB();
        getItemsMenuFromDB();

        intialAdptorItems();
        intialAdptorMenu();

        return myView;
    }

    public  void intialAdptorItems()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.menusRecycleHor);
        recyclerView.setLayoutManager(layoutManager);
        MenuRecycleHorAdaptor adaptor = new MenuRecycleHorAdaptor(getActivity(),itemsImglist,titleslist,markslist,pricesList,itemIdList);
        recyclerView.setAdapter(adaptor);

    }
    public  void intialAdptorMenu()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.menusRecycleHor2);
        recyclerView.setLayoutManager(layoutManager);
        MenuRecycleHorAdaptor2 adaptor = new MenuRecycleHorAdaptor2(getActivity(),menuIdList,menuImages);
        recyclerView.setAdapter(adaptor);
    }

    public  void getItemsMenuFromDB(){
        StringRequest request2 = new StringRequest(Request.Method.POST, GetMenuItemsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                    JSONArray items = jsonObject.getJSONArray("MenuItems");

                    for (int i=0 ; i< items.length() ; i++)
                    {
                        JSONObject item = items.getJSONObject(i);

                        String itemImage = item.getString("Menu_Image");
                        itemsImglist.add(itemImage);

                        String itemTitle = item.getString("Menu_Title");
                        titleslist.add(itemTitle);

                        String itemType = item.getString("itemType");
                        markslist.add(itemType);

                        float itemPrice = Float.parseFloat(item.getString("Menu_Price"));
                        pricesList.add(itemPrice);

                        int itemID = Integer.parseInt(item.getString("Menu_id"));
                        itemIdList.add(itemID);

                    }

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
                HashMap hashMap = new HashMap();
                hashMap.put("Place_Id",""+placeId);
                return  hashMap ;
            }
        };
        requestQueue.add(request2);
    }

    public  void getMenusFromDB(){
        request = new StringRequest(Request.Method.POST, GetMenuURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                    JSONArray menus = jsonObject.getJSONArray("menus");
                    for (int i=0 ; i< menus.length() ; i++) {
                        JSONObject menu = menus.getJSONObject(i);

                        String menuImage = menu.getString("image");
                        menuImages.add(menuImage);
                        int menuId = Integer.parseInt(menu.getString("menu_id"));
                        menuIdList.add(menuId);
                    }

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
                HashMap hashMap = new HashMap();
                hashMap.put("PlaceId",""+placeId);
                return  hashMap ;
            }
        };
        requestQueue.add(request);

    }

}
