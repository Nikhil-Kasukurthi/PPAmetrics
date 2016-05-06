package com.example.nikhil.os;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikhil on 5/5/16.
 */
public class StatsFragment extends Fragment {
    RecyclerView clientList;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        clientList = (RecyclerView) view.findViewById(R.id.clients);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uname = sharedPreferences.getString("uname","first");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url2 = getString(R.string.ip)+"/clientReadServerStats";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int count = 0;
                        System.out.println(response);
                        try {
                            JSONObject jsonObject2 = new JSONObject(response);
                            JSONObject jsonObject = jsonObject2.getJSONObject("Most Recent Data");
                            int CPU = jsonObject.getInt("CPU Percent");
                            String TIME = jsonObject.getString("Time");
                            JSONArray jsonArray = jsonObject.getJSONArray("System Stats");


                            ClientInfo[] clientInfo = new ClientInfo[jsonArray.length()];
                            while (count < jsonArray.length()) {
                                JSONObject jo = jsonArray.getJSONObject(count);
                                String DEVICE = jo.getString("Device");
                                double TOTAL = jo.getDouble("Total");
                                double FREE = jo.getDouble("Free");
                                double USED = jo.getDouble("Used");
                                double PERCENT = jo.getDouble("Percent");
                                clientInfo[count] = new ClientInfo(DEVICE, TOTAL, FREE, USED, PERCENT);
                                System.out.println(clientInfo[count].getDevice());
                                count++;
                            }
                            Log.e("Client info list/array", Arrays.toString(clientInfo));
                            ClientInfoAdapter clientInfoAdapter = new ClientInfoAdapter(clientInfo);
                            clientList.setAdapter(clientInfoAdapter);
                            clientList.setLayoutManager(new LinearLayoutManager(getContext()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("error",error.toString());
                    }
                }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params;
                params = new HashMap<String, String>();
                params.put("uname",uname);
                return params;
            }
        };
        queue.add(stringRequest2);
        return view;
    }
}
