package com.example.budgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgets.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class incomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference getRef;
    private RecyclerView recyclerView;

    private TextView incomeTotalSum;

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    private Button btnUpdate;
    private Button btnDelete;

    //dtaa item value
    private String type;
    private String note;
    private int amount;

    private String post_key;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_income, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id__income);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalvalue=0;
                DataSnapshot datasnapshot = null;
                for (DataSnapshot mysnapshot:datasnapshot.getChildren()){

                    Data data=mysnapshot.getValue(Data.class);

                    totalvalue+=data.getAmount();

                    String stTotalVAlue=String.valueOf(totalvalue);

                    incomeTotalSum.setText(stTotalVAlue+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;

    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.income_recycler_data,
                MyViewHolder.class,
                mIncomeDatabase) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder,final Data model,final int position) {
                // Bind the model data to the ViewHolder views
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setdate(model.getDate());
                viewHolder.setAmmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key= getRef.getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();
                        updateDataItem();
                    }
                });
             }
        };
                 recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);

        }

        void setNote(String note){

            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }

        void setdate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        private void setAmmount(int amount){

            TextView mAmmount=mView.findViewById(R.id.ammount_txt_income);
            String stammount=String.valueOf(amount);
            mAmmount.setText(stammount);
        }

        public void setAmount(int amount) {
        }


    }

    private void updateDataItem(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());

        View myView=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myView);

        edtAmount=myView.findViewById(R.id.amount_edit);
        edtType=myView.findViewById(R.id.type_edit);
        edtNote=myView.findViewById(R.id.note_edit);

        //set data to edit text...
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtType.setText(note);
        edtType.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myView.findViewById(R.id.btnupdate);
        btnUpdate=myView.findViewById(R.id.btndelete);

       final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String mdamount=String.valueOf(amount);

                int myAmount=Integer.parseInt(mdamount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(myAmount,type,note,post_key,mDate);

                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIncomeDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }


}