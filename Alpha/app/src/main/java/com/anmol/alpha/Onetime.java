package com.anmol.alpha;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Onetime extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Onetime() {

    }
    public static Onetime newInstance(String param1, String param2) {
        Onetime fragment = new Onetime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       getActivity().getWindow().setStatusBarColor(Color.parseColor("#9fa8da"));
       View view=inflater.inflate(R.layout.fragment_onetime, container, false);
        AppCompatButton login=view.findViewById(R.id.login);
        AppCompatButton signup=view.findViewById(R.id.signup);
        login.setOnClickListener(new login(requireActivity().getSupportFragmentManager()));
        signup.setOnClickListener(new signup(requireActivity().getSupportFragmentManager()));
        return view;
    }
    static class login implements View.OnClickListener{
        FragmentManager manager;
        Account account;
        public login(FragmentManager manager){
            this.manager=manager;
            account=new Account();
        }
        @Override
        public void onClick(View view) {
            Bundle bundle=new Bundle();
            bundle.putString("data","login");
            account.setArguments(bundle);
            manager.beginTransaction().replace(R.id.main,account).commit();
        }
    }
    static class signup implements View.OnClickListener{
        FragmentManager manager;
        Account account;
        public signup(FragmentManager manager){
            this.manager=manager;
            account=new Account();
        }
        @Override
        public void onClick(View view) {
            Bundle bundle=new Bundle();
            bundle.putString("data","signup");
            account.setArguments(bundle);
            manager.beginTransaction().replace(R.id.main,account).commit();
        }
    }
}