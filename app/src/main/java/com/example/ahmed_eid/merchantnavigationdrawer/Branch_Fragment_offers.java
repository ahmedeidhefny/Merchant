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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Branch_Fragment_offers extends Fragment {

    View myView ;
    RequestQueue requestQueue ;
    StringRequest request ;
    SharedPreferences sharedPreferences ;
    android.app.AlertDialog alertDialog;
    int branchId ;
    private  final String GetOfferPointsURL = "http://gp.sendiancrm.com/offerall/getOfferPoints.php";
    private  final String GetOfferDiscountURL = "http://gp.sendiancrm.com/offerall/getOfferDiscount.php";

    ArrayList<String> imagesList_point = new ArrayList<>();
    ArrayList<String> titleList_point = new ArrayList<>();
    ArrayList<String> markList_point = new ArrayList<>();
    ArrayList<Float> PriceList_point = new ArrayList<>();
    ArrayList<Integer> pointsList_point = new ArrayList<>();
    ArrayList<Integer> offerIdList_point = new ArrayList<>();
    ArrayList<String> starDateList_point = new ArrayList<>();
    ArrayList<String> endDateList_point = new ArrayList<>();


    ArrayList<String> imagesList_Dis = new ArrayList<>();
    ArrayList<String> titleList_Dis = new ArrayList<>();
    ArrayList<String> markList_Dis = new ArrayList<>();
    ArrayList<Float> PriceList_Dis = new ArrayList<>();
    ArrayList<Float> newPrice_Dis = new ArrayList<>();
    ArrayList<Integer> DiscountList_Dis = new ArrayList<>();
    ArrayList<Integer> offerIdList_Dis = new ArrayList<>();
    ArrayList<String> starDateList_Dis = new ArrayList<>();
    ArrayList<String> endDateList_Dis = new ArrayList<>();





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.branch_fragment_offers,container,false);

        ImageView btn_addOfferItem = myView.findViewById(R.id.addOfferItem);
        ImageView btn_addOfferPoint = myView.findViewById(R.id.addofferpoint);

        requestQueue = Volley.newRequestQueue(getActivity()) ;
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("loginBranch",false)){

           branchId = sharedPreferences.getInt("BId",0);
            //Toast.makeText(getActivity(), ""+branchId, Toast.LENGTH_SHORT).show();
        }

        btn_addOfferItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BranchAddOfferDiscount.class);
                startActivity(intent);
            }
        });

        btn_addOfferPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BranchAddOfferPoints.class);
                startActivity(intent);

            }
        });



        getOfferDiscount();
        getOfferPoints();

        intialAdptorItemsOfferDis();
        intialAdptorItemsOfferPoint();

        // NotificationEndDateOffer notifi = new NotificationEndDateOffer(getActivity());
        // notifi.notifi_EndDateOffer(endDateList_point);
        return myView;

    }

    public  void intialAdptorItemsOfferDis()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.offerRecycleHor);
        recyclerView.setLayoutManager(layoutManager);
        OfferRecycleHorAdaptor adaptor = new OfferRecycleHorAdaptor(getActivity(),newPrice_Dis,imagesList_Dis,titleList_Dis,markList_Dis,DiscountList_Dis,PriceList_Dis,
                                                                    offerIdList_Dis,starDateList_Dis,endDateList_Dis);
        recyclerView.setAdapter(adaptor);

    }
    public  void intialAdptorItemsOfferPoint()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.offerRecycleHor2);
        recyclerView.setLayoutManager(layoutManager);
        OfferRecycleHorAdaptor2 adaptor = new OfferRecycleHorAdaptor2(getActivity(),imagesList_point,titleList_point,markList_point,
                        pointsList_point,PriceList_point,starDateList_point,endDateList_point,offerIdList_point);
        recyclerView.setAdapter(adaptor);

    }
    public void getOfferDiscount(){
        StringRequest request2 = new StringRequest(Request.Method.POST, GetOfferDiscountURL, new Response.Listener<String>() {
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

                    JSONArray offersDis = jsonObject.getJSONArray("offerDis");

                    for (int i=0 ; i< offersDis.length() ; i++)
                    {
                        JSONObject offer = offersDis.getJSONObject(i);

                        String offerImage = offer.getString("Offer_image");
                        imagesList_Dis.add(offerImage);
                        String offerTitle = offer.getString("Title");
                        titleList_Dis.add(offerTitle);
                        String offerType = offer.getString("itemType");
                        markList_Dis.add(offerType);

                        float offerPrice = Float.parseFloat(offer.getString("price"));
                        PriceList_Dis.add(offerPrice);
                        int offerDiscount = Integer.parseInt(offer.getString("Discount"));
                        DiscountList_Dis.add(offerDiscount);

                        Float dis = (float)offerDiscount/100 ;
                        Float newPrice = offerPrice * dis ;
                        newPrice_Dis.add(newPrice);

                        int offerID = Integer.parseInt(offer.getString("Offer_id"));
                        offerIdList_Dis.add(offerID);
                        String offerStarDate =offer.getString("StartDate");
                        starDateList_Dis.add(offerStarDate);
                        String offerEndDate =offer.getString("EndDate");
                        endDateList_Dis.add(offerEndDate);
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
                hashMap.put("BranchId",""+branchId);
                return  hashMap ;
            }
        };
        requestQueue.add(request2);
    }

    public void getOfferPoints(){
        request = new StringRequest(Request.Method.POST, GetOfferPointsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.get("error"),
                                Toast.LENGTH_LONG).show();
                    }

                    JSONArray offers = jsonObject.getJSONArray("offerDis");

                    for (int i=0 ; i< offers.length() ; i++)
                    {
                        JSONObject offer = offers.getJSONObject(i);

                        String offerImage = offer.getString("Offer_image");
                        imagesList_point.add(offerImage);
                        String offerTitle = offer.getString("Title");
                        titleList_point.add(offerTitle);
                        String offerType = offer.getString("itemType");
                        markList_point.add(offerType);
                        float offerPrice = Float.parseFloat(offer.getString("Price"));
                        PriceList_point.add(offerPrice);
                        int pointss = Integer.parseInt(offer.getString("points"));
                        pointsList_point.add(pointss);
                        int offerID = Integer.parseInt(offer.getString("Offer_id"));
                        offerIdList_point.add(offerID);
                        String offerStarDate =offer.getString("StartDate");
                        starDateList_point.add(offerStarDate);
                        String offerEndDate =offer.getString("EndDate");
                        endDateList_point.add(offerEndDate);

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
                hashMap.put("BranchId",""+branchId);
                return  hashMap ;
            }
        };
        requestQueue.add(request);
    }

}
