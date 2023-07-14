package com.anmol.alpha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.anmol.alpha.model.model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppInitProvider;
import com.startapp.sdk.adsbase.StartAppSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirstActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    adapter adapter;
    EditText msg;
    int c=0;
    DatabaseReference reference,index;
    Connect connect;
    LottieAnimationView animationView,typing,error;
    StartAppAd ad;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFFFF"));
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        recyclerView=findViewById(R.id.recyclerview);
        msg=findViewById(R.id.msg);
        error=findViewById(R.id.nonetwork);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(null);
        StartAppSDK.init(FirstActivity.this,"211206792",false);
        recyclerView.setNestedScrollingEnabled(true);
        AppCompatButton button=findViewById(R.id.send);
        animationView=findViewById(R.id.botanim);
        typing=findViewById(R.id.typing);
        typing.setVisibility(View.GONE);
        ad=new StartAppAd(FirstActivity.this);
        ad.loadAd();
        reference=FirebaseDatabase.getInstance().getReference("chatbot/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/chats");
        index=FirebaseDatabase.getInstance().getReference("chatbot/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        index.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("index").exists()) {
                    setC(Integer.parseInt(snapshot.child("index").getValue().toString()));
                }else{
                   index.child("index").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ad.showAd();
        FirebaseRecyclerOptions<model> options=new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(reference,model.class).build();
        adapter=new adapter(options);
        recyclerView.setAdapter(adapter);
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             //  button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
       // button.setEnabled(false);
        msg.addTextChangedListener(watcher);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
               if(!TextUtils.isEmpty(msg.getText())) {
                   if (isOnline(FirstActivity.this)) {
                       error.setVisibility(View.GONE);
                       typing.setVisibility(View.VISIBLE);
                       animationView.setVisibility(View.GONE);
                       recyclerView.setVisibility(View.VISIBLE);
                       connect = new Connect(msg.getText().toString(), FirstActivity.this);
                       reference.child(String.valueOf(c)).child("send").setValue(msg.getText().toString());
                       reference.child(String.valueOf(c)).child("index").setValue(String.valueOf(c));
                       msg.setText("");
                       msg.setEnabled(false);
                       recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                       if (connect.isAlive()) {
                           connect = null;
                           connect = new Connect(msg.getText().toString(), FirstActivity.this);
                           connect.start();
                       } else {
                           connect.start();
                       }
                   }else{
                       error.setVisibility(View.VISIBLE);
                       recyclerView.setVisibility(View.GONE);
                       if(animationView.getVisibility()==View.VISIBLE){
                           animationView.setVisibility(View.GONE);
                       }
                   }
               }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        msg.setEnabled(true);
        typing.setVisibility(View.GONE);
        reference.child(String.valueOf(c)).child("recieve").setValue(intent.getStringExtra("data"));
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
        c++;
        index.child("index").setValue(String.valueOf(c));
    }

    static class Connect extends Thread{
        String msg;
        Context context;
        public Connect(String msg,Context context){
            this.msg=msg;
            this.context=context;
        }
        @Override
        public void run() {
            super.run();
            OkHttpClient client=new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("in", msg)
                    .add("op", "in")
                    .add("cbot", "1")
                    .add("SessionID", FirebaseAuth.getInstance()
                    .getCurrentUser().getUid())
                    .add("cbid", "1")
                    .add("key", "RHMN5hnQ4wTYZBGCF3dfxzypt68rVP")
                   // .add("ChatSource", "RapidAPI")
                    //.add("duration", "1")
                    .build();

            Request request = new Request.Builder()
                    .url("https://robomatic-ai.p.rapidapi.com/api")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("X-RapidAPI-Key", "b526edf38fmshbe9d36060be2783p1f6858jsnb9320c67c200")
                    .addHeader("X-RapidAPI-Host", "robomatic-ai.p.rapidapi.com")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                  send_data(e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        JSONObject object=new JSONObject(response.body().string());
                       send_data(object.getString("out"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        public void send_data(String msg){
            Intent i = new Intent(context, FirstActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("data", msg);
            context.startActivity(i);
        }
    }
    public void setC(int c){
        this.c=c;
    }
    public int getC(){
        return c;
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        StartAppAd.onBackPressed(this);
        Process.killProcess(Process.myPid());
        finish();
    }
}