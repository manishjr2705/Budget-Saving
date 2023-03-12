package com.example.budgets;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgets.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.text.DateFormat;
import java.util.Date;

public class dashFragment extends Fragment {

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private Boolean isopen = false;

    //animation
    private Animation FadOpen, FadeClose;

    //dashboard income and expense result..
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    //firebase...
    private DatabaseReference mIncomeDataBase;
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private android.app.AlertDialog.Builder mRecyclerIncome;
    private android.app.AlertDialog.Builder mRecyclerExpense;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dash, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDataBase = FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        mIncomeDataBase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        //connect floating button to layout
        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_ft_btn);
        //connect floating text
        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);
        //total income and expense result set..

        totalExpenseResult = myview.findViewById(R.id.incoome_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);


        //animation connect..
        FadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if (isopen) {
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isopen = false;
                } else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isopen = true;
                }

            }
        });
        //calculate total income..

        mIncomeDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalsum = 0;

                DataSnapshot datasnapshot = null;
                for (DataSnapshot mysnapshot : datasnapshot.getChildren()) {

                    Data data = mysnapshot.getValue(Data.class);

                    totalsum += data.getAmount();

                    String stResult = String.valueOf(totalsum);

                    totalIncomeResult.setText(stResult + ".00");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Calculate total expense..

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum = 0;

                DataSnapshot datasnapshot = null;
                for (DataSnapshot mysnapshot : datasnapshot.getChildren()) {

                    Data data = mysnapshot.getValue(Data.class);

                    totalsum += data.getAmount();

                    String strTotal = String.valueOf(totalsum);

                    totalExpenseResult.setText(strTotal + ".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;
    }

    private void ftAnimation() {

        if (isopen) {
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_txt.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isopen = false;
        } else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_txt.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isopen = true;
        }

    }


    private void addData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeDataInsetr();

            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseDataInsert();

            }
        });

    }

    public void incomeDataInsetr() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final EditText edtAmmount = myview.findViewById(R.id.amount_edit);
        final EditText edtType = myview.findViewById(R.id.type_edit);
        final EditText edtNote = myview.findViewById(R.id.note_edit);

        Button btnsave = myview.findViewById(R.id.btnsave);
        Button btncancle = myview.findViewById(R.id.btnCancle);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = edtType.getText().toString().trim();
                String ammount = edtAmmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    edtType.setError("required Field");
                    return;
                }

                if (TextUtils.isEmpty(type)) {
                    edtAmmount.setError("required Field");
                    return;
                }

                int ouramountint = Integer.parseInt(ammount);
                if (TextUtils.isEmpty(type)) {
                    edtNote.setError("required Field");
                    return;
                }

                String id = mIncomeDataBase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint, type, note, id, mDate);

                mIncomeDataBase.child(id).setValue(data);

                Toast.makeText(getActivity(), "added Data", Toast.LENGTH_SHORT);

                ftAnimation();

                dialog.dismiss();


            }


        });

        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void expenseDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);

        final EditText amount = myview.findViewById(R.id.amount_edit);
        final EditText type = myview.findViewById(R.id.type_edit);
        final EditText note = myview.findViewById(R.id.note_edit);

        Button btnsave = myview.findViewById(R.id.btnsave);
        Button btncancle = myview.findViewById(R.id.btnCancle);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmmount = amount.getText().toString().trim();
                String tmtyoe = type.getText().toString().trim();
                String tmnote = note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmmount)) {
                    amount.setError("Required field");
                    return;
                }

                int inamount = Integer.parseInt(tmAmmount);

                if (TextUtils.isEmpty(tmtyoe)) {
                    type.setError("required field");
                    return;
                }
                if (TextUtils.isEmpty(tmnote)) {
                    note.setError("required field");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(inamount, tmtyoe, tmnote, id, mDate);
                mExpenseDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income,
                        dashFragment.IncomeViewHolder.class,
                        mIncomeDataBase

                ) {
            @Override
            protected void populateViewHolder(incomeFragment.MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setdate(model.getDate());

            }
        };



        FirebaseRecyclerAdapter<Data, ExpenseViewHolder>expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_expense,
                        dashFragment.ExpenseViewHolder.class,
                        mExpenseDatabase

                ) {
            @Override
            protected void populateViewHolder(incomeFragment.MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setdate(model.getDate());

            }
        };


    }

    //for income data

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {

        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeView(String type) {

            TextView mtype = mIncomeView.findViewById(R.id.type_Income_ds);
            mtype.setText(type);
        }

        public void setmIncomeAmount(int amount) {

            TextView mAmount = mIncomeView.findViewById(R.id.amount_Income_ds);
            String strAmount = String.valueOf(amount);
            mAmount.setText(strAmount);

        }

        public void setIncomeDate(String date) {
            TextView mtype = mIncomeView.findViewById(R.id.date_income_ds);
            mtype.setText(date);
        }
    }

    //For Expense Data

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setIncomeView(String type) {

            TextView mtype = mExpenseView.findViewById(R.id.type_Expense_ds);
            mtype.setText(type);
        }

        public void setmIncomeAmount(int amount) {

            TextView mAmount = mExpenseView.findViewById(R.id.amount_Expense_ds);
            String strAmount = String.valueOf(amount);
            mAmount.setText(strAmount);

        }

        public void setExpenseDate(String date) {
            TextView mtype = mExpenseView.findViewById(R.id.date_expense_ds);
            mtype.setText(date);
        }
    }
}