package com.example.nikhil.os;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikhil on 2/5/16.
 */
public class HomePageFragment extends Fragment {
    TextView node,system, release, cpu, time;
    SharedPreferences sharedPreferences;
    RecyclerView clientList;
    Button analyse;
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        node = (TextView) view.findViewById(R.id.node_name);
        system = (TextView) view.findViewById(R.id.system_name);
        release = (TextView) view.findViewById(R.id.release);
        analyse = (Button) view.findViewById(R.id.analyse);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uname = sharedPreferences.getString("uname","first");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getString(R.string.ip)+"/clientInfoRead";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String NODE = jsonObject.getString("node");
                            String SYSTEM = jsonObject.getString("system");
                            String RELEASE = jsonObject.getString("release");

                            node.setText(NODE);
                            system.setText(SYSTEM);
                            release.setText(RELEASE);
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

        analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                CpuAnalysisFragment frag = new CpuAnalysisFragment();
                fragmentTransaction2.addToBackStack("Sign Up Fragment");
                fragmentTransaction2.hide(HomePageFragment.this);
                fragmentTransaction2.add(android.R.id.content, frag);
                fragmentTransaction2.commit();
                Fragment previousInstance = getFragmentManager().findFragmentByTag("First Screen");
                if (previousInstance != null)
                    fragmentTransaction2.remove(previousInstance);
            }
        });
        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new StatsFragment();
            switch (position){
                case 0: fragment = new StatsFragment();
                        break;
                case 1: fragment = new CpuAnalysisFragment();
            }
            return  fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
