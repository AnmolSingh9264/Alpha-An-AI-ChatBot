package com.anmol.alpha;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.anmol.alpha.model.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adapter extends FirebaseRecyclerAdapter<model,adapter.Viewholder> {
    public adapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @NonNull
    @Override
    public adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.Viewholder holder, int position,model model) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chatbot/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()
        +"/chats/"+model.getIndex());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("send").exists() && snapshot.child("recieve").exists()){
                    holder.recieve_box.setVisibility(View.VISIBLE);
                    holder.send_box.setVisibility(View.VISIBLE);
                    holder.recieve_msg.setText(model.getRecieve());
                    holder.send_msg.setText(model.getSend());
                }else {
                    if (snapshot.child("send").exists()) {
                        holder.recieve_box.setVisibility(View.GONE);
                        // holder.send_box.setVisibility(View.VISIBLE);
                        holder.send_msg.setText(model.getSend());
                    } else if (snapshot.child("recieve").exists()) {
                        holder.send_box.setVisibility(View.GONE);
                        // holder.recieve_box.setVisibility(View.VISIBLE);
                        holder.recieve_msg.setText(model.getRecieve());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        ConstraintLayout send_box,recieve_box;
        TextView send_msg,recieve_msg;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            send_box=itemView.findViewById(R.id.constraintLayout);
            recieve_box=itemView.findViewById(R.id.layout2);
            send_msg=itemView.findViewById(R.id.msg_sent);
            recieve_msg=itemView.findViewById(R.id.msgrecieve);
        }
    }
}
