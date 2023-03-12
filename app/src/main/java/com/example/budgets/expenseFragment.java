package com.example.budgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link expenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class expenseFragment extends Fragment {


    //firebase database
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private DatabaseReference getRef;
    //recyclerview..
    private RecyclerView recyclerView;

    private TextView expenseSumResult;

    //Edit data item;
    private EditText edtAmount;
    private EditText edttype;
    private EditText edtNote;

    private Button btnUpdate;
    private Button btnDelete;

    //Data Variable

    private String type;
    private String note;
    private int amount;

    private String post_key;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public expenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment expenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static expenseFragment newInstance(String param1, String param2) {
        expenseFragment fragment = new expenseFragment();
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
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        expenseSumResult=myview.findViewById(R.id.expense_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id__expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int expesnSum=0;
                DataSnapshot datasnapshot = null;
                for (DataSnapshot mysnapshot:datasnapshot.getChildren()){

                    Data data=mysnapshot.getValue(Data.class);

                    expesnSum+=data.getAmount();

                    String strExpensesum=String.valueOf(expesnSum);

                    expenseSumResult.setText(strExpensesum+".00");

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

        FirebaseRecyclerAdapter<Data,MyviewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyviewHolder>
                (
                        Data.class,
                        R.layout.expense_recycler_data,
                        MyviewHolder.class,
                        mExpenseDatabase

                ) {
            @Override
            protected void populateViewHolder(incomeFragment.MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setdate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key= getRef.getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updatedataItem();
                    }
                });

            }


        };

    }

    private static class MyviewHolder extends RecyclerView.ViewHolder{

       View mView;

        public MyviewHolder( View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }
        private void setType(String type) {
            TextView mType = mView.findViewById(R.id.date_txt_expense);
            mType.setText(type);

        }
        private void setNote(String note) {
            TextView mNotes = mView.findViewById(R.id.date_txt_expense);
            mNotes.setText(note);

        }

        private void setAmount(String amount) {
            TextView mAmount = mView.findViewById(R.id.date_txt_expense);

            String stramount=String.valueOf(amount);

            mAmount.setText(stramount);

        }

    }

    private void  updatedataItem(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmount=myview.findViewById(R.id.amount_edit);
        edtNote=myview.findViewById(R.id.note_edit);
        edttype=myview.findViewById(R.id.type_edit);

        edttype.setText(type);
        edttype.setSelection(type.length());

        edttype.setText(note);
        edttype.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(note.length());


        btnUpdate=myview.findViewById(R.id.btnupdate);
        btnDelete=myview.findViewById(R.id.btndelete);

       final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=edttype.getText().toString().trim();
                type=edtNote.getText().toString().trim();

                String stamount=String.valueOf(amount);

                stamount=edtAmount.getText().toString().trim();

                int intamount=Integer.parseInt(stamount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(intamount,type,note,post_key,mDate);

                mExpenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();


            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mExpenseDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }

}