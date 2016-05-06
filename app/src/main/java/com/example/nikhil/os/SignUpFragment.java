package com.example.nikhil.os;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by nikhil on 4/4/16.
 */
public class SignUpFragment extends Fragment {
    EditText emailET,pass,passRe;
    Button RequestAccess;
    public final String EMAIL = "email";
    SharedPreferences sharedpreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailET = (EditText)view.findViewById(R.id.email);
        pass = (EditText)view.findViewById(R.id.pass);
        passRe = (EditText)view.findViewById(R.id.passVerify);
        //FontsOverride.overrideFont(getContext(), view, "fonts/Lato-Regular.ttf");
        //FontsOverride.overrideFont(getContext(), view.findViewById(R.id.reqButton), "fonts/Lato-Bold.ttf");

        RequestAccess = (Button)view.findViewById(R.id.reqButton);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EMAIL,emailET.getText().toString().trim());
        editor.apply();
        RequestAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET == null || pass == null || passRe == null) {
                    Toast.makeText(getActivity(), "Please enter the credentials", Toast.LENGTH_SHORT).show();
                }else{
                    if(pass.getText().toString().trim().equals(passRe.getText().toString().trim())){
                        {
                            ConnectivityManager connMgr = (ConnectivityManager)
                                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                System.out.println(getString(R.string.ip));
                                String url = getString(R.string.ip)+"/signUp";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                System.out.println(response);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean serverResponse = jsonObject.getBoolean("Response");
                                                    if(serverResponse){
                                                        FragmentManager fragmentManager2 = getFragmentManager();
                                                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                                                        LoginAuthenticationFragment frag = new LoginAuthenticationFragment();
                                                        fragmentTransaction2.addToBackStack("Sign Up Fragment");
                                                        fragmentTransaction2.hide(SignUpFragment.this);
                                                        fragmentTransaction2.add(android.R.id.content, frag);
                                                        fragmentTransaction2.commit();
                                                        Fragment previousInstance = getFragmentManager().findFragmentByTag("First Screen");
                                                        if (previousInstance != null)
                                                            fragmentTransaction2.remove(previousInstance);
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
                                        params.put("username", emailET.getText().toString().trim());
                                        params.put("password", pass.getText().toString().trim());
                                        return params;
                                    }
                                };
                                queue.add(stringRequest);

                            } else
                                Toast.makeText(getActivity(), "Network unavailable at the moment", Toast.LENGTH_SHORT).show();
                        }
                    }else
                        Toast.makeText(getActivity(),"Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
