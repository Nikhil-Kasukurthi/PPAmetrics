package com.example.nikhil.os;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by nikhil on 2/5/16.
 */
public class LandingFragment extends Fragment {
    Button login, signUP;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        login = (Button)view.findViewById(R.id.loginButton);
        signUP = (Button)view.findViewById(R.id.signUPButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                LoginAuthenticationFragment frag = new LoginAuthenticationFragment();
                fragmentTransaction2.addToBackStack("Landing Fragment");
                fragmentTransaction2.hide(LandingFragment.this);
                fragmentTransaction2.add(android.R.id.content, frag);
                fragmentTransaction2.commit();
                Fragment previousInstance = getFragmentManager().findFragmentByTag("First Screen");
                if (previousInstance != null)
                    fragmentTransaction2.remove(previousInstance);
            }
        });
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                SignUpFragment frag = new SignUpFragment();
                fragmentTransaction2.addToBackStack("Landing Fragment");
                fragmentTransaction2.hide(LandingFragment.this);
                fragmentTransaction2.add(android.R.id.content, frag);
                fragmentTransaction2.commit();
                Fragment previousInstance = getFragmentManager().findFragmentByTag("First Screen");
                if (previousInstance != null)
                    fragmentTransaction2.remove(previousInstance);
            }
        });
        return view;

    }
}
