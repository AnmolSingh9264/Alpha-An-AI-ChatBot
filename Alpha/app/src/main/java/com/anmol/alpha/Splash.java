package com.anmol.alpha;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Splash() {
        // Required empty public constructor
    }
    public static Splash newInstance(String param1, String param2) {
        Splash fragment = new Splash();
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
        View view=inflater.inflate(R.layout.fragment_splash, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    clear();
                     startActivity(new Intent(requireActivity(),FirstActivity.class));
                     requireActivity().finish();
                }else{
                    FragmentManager manager=requireActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.main,new Onetime()).commit();
                }
            }
        },5000);
        return view;
    }
    public void clear(){
        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference("chatbot/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.removeValue();
    }
}