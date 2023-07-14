package com.anmol.alpha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Account extends Fragment {
    TextView title,bottm_txt,txt,bottom_txt2;
    Button button;
    EditText email,password;
    ProgressBar bar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Account() {
    }

    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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
        View view=inflater.inflate(R.layout.fragment_account, container, false);
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#FFFFFFFF"));
        title=view.findViewById(R.id.textView);
        bottm_txt=view.findViewById(R.id.register);
        txt=view.findViewById(R.id.forgetpass);
        button=view.findViewById(R.id.button);
        bottom_txt2=view.findViewById(R.id.login);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        bar=view.findViewById(R.id.progress_bar);
        bar.setVisibility(View.GONE);
        if(TextUtils.equals(getArguments().getString("data"),"signup")){
            setData(getResources().getString(R.string.createaccount),getResources().getString(R.string.signup),
                    " ");
            bottm_txt.setVisibility(View.GONE);
            bottom_txt2.setVisibility(View.VISIBLE);
        }
        bottm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(getResources().getString(R.string.createaccount),getResources().getString(R.string.signup),
                        " ");
                bottm_txt.setVisibility(View.GONE);
                bottom_txt2.setVisibility(View.VISIBLE);
                email.setText("");
                password.setText("");
            }
        });
        bottom_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(getResources().getString(R.string.Welcome),getResources().getString(R.string.login),
                        getResources().getString(R.string.forget));
                bottm_txt.setVisibility(View.VISIBLE);
                bottom_txt2.setVisibility(View.GONE);
                email.setText("");
                password.setText("");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText())){
                    showSnack("Email is empty",view,"#d50000");
                }else if(TextUtils.isEmpty(password.getText())){
                    showSnack("Password is empty",view,"#d50000");
                }else {
                    if (TextUtils.equals(button.getText(), getResources().getString(R.string.login))) {
                          login(view);
                    } else {
                          Register(view);
                    }
                }
            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText())){
                    showSnack("Email is empty",view,"#d50000");
                }else{
                    bar.setVisibility(View.VISIBLE);
                    removeListner(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    bar.setVisibility(View.GONE);
                                    removeListner(false);
                                    showSnack(e.getMessage(), view,"#d50000");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    bar.setVisibility(View.GONE);
                                    removeListner(false);
                                    showSnack("Password reset link sent to email",view,"#43a047");
                                }
                            });
                        }
                    },2500);
                }
            }
        });
        return view;
    }
    public void setData(String title,String btn_txt,String txt){
        this.title.setText(title);
        this.txt.setText(txt);
        this.button.setText(btn_txt);
    }
    public void Register(View view){
        bar.setVisibility(View.VISIBLE);
        removeListner(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        bar.setVisibility(View.GONE);
                        removeListner(false);
                        showSnack("Registered successfully",view,"#43a047");
                        txt.setEnabled(false);
                        button.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(requireActivity(),FirstActivity.class));
                                requireActivity().finish();
                            }
                        },2000);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(View.GONE);
                        removeListner(false);
                        showSnack(e.getMessage(),view,"#d50000");
                    }
                });
            }
        },2500);
    }
    public  void login(View view){
        bar.setVisibility(View.VISIBLE);
        removeListner(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(View.GONE);
                        removeListner(false);
                        showSnack(e.getMessage(),view,"#d50000");
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        removeListner(false);
                        bar.setVisibility(View.GONE);
                        showSnack("Successfully logged in",view,"#43a047");
                        txt.setEnabled(false);
                        button.setEnabled(false);
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               startActivity(new Intent(requireActivity(),FirstActivity.class));
                               requireActivity().finish();
                           }
                       },2000);

                    }
                });
            }
        },2500);
    }
    public void showSnack(String msg,View view,String color){
        Snackbar.make(view,msg, BaseTransientBottomBar.LENGTH_SHORT).setBackgroundTint(Color.parseColor(color)).show();
    }
    public void removeListner(Boolean o){
        if (o){
            button.setEnabled(false);
            txt.setEnabled(false);
            bottom_txt2.setEnabled(false);
            bottm_txt.setEnabled(false);
            email.setEnabled(false);
            password.setEnabled(false);
        }else{
            button.setEnabled(true);
            txt.setEnabled(true);
            bottom_txt2.setEnabled(true);
            bottm_txt.setEnabled(true);
            email.setEnabled(true);
            password.setEnabled(true);
        }
    }
}