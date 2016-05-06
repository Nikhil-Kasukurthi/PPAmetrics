package com.example.nikhil.os;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.YAxisRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by nikhil on 4/5/16.
 */
public class CpuAnalysisFragment extends Fragment {
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cpu_analysis, container, false);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //

        // creating list of entry
        final ArrayList<Entry> entries = new ArrayList<>();
        final LineDataSet dataset = new LineDataSet(entries, "CPU Usage");
        final LineChart chart = (LineChart) view.findViewById(R.id.chart);
        chart.setScaleEnabled(true);
        chart.animateXY(1000,1000);
        chart.setDescriptionColor(getResources().getColor(R.color.white));
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightAxis = chart.getAxisRight();
        xAxis.setTextColor(R.color.white);
        rightAxis.setEnabled(false);
        final ArrayList<String> labels = new ArrayList<String>();
        dataset.setFillColor(R.color.colorAccent);
        chart.getXAxis().setDrawGridLines(false);
        chart.setGridBackgroundColor(R.color.white);
        dataset.setValueTextColor(R.color.white);
        dataset.setHighLightColor(R.color.colorAccent);
        dataset.setDrawFilled(true);
         // set the data and list of lables into chart

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uname = sharedPreferences.getString("uname","first");
        String url = getString(R.string.ip)+"/clientAnalyseServerStats";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int count= 0;
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Stats array");
                            while(count<jsonArray.length()){
                                JSONObject jo = jsonArray.getJSONObject(count);
                                double cpu = jo.getDouble("CPU Percent");
                                String time = jo.getString("Time");
                                System.out.println(cpu);
                                entries.add(new Entry((float) cpu,count));
                                labels.add(time);// refresh
                                LineData data = new LineData(labels, dataset);
                                chart.setDescription("CPU usage over time");  // set the description
                                chart.setData(data);
                                chart.notifyDataSetChanged(); // let the chart know it's data changed
                                chart.animateXY(1000,1000);
                                chart.invalidate();
                                chart.setGridBackgroundColor(R.color.colorAccent);
                                count++;

                            }
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
        queue.add(stringRequest);

//        toolbar.setTitle("ppametrics.io");
        return view;
    }

}
