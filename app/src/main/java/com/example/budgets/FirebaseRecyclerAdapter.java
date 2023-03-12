package com.example.budgets;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgets.model.Data;
import com.google.firebase.database.DatabaseReference;

public abstract class FirebaseRecyclerAdapter<T, T1> extends RecyclerView.Adapter {
    public FirebaseRecyclerAdapter(Class<T> dataClass, int income_recycler_data, Class<T1> myViewHolderClass, DatabaseReference mIncomeDatabase) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected abstract void populateViewHolder(incomeFragment.MyViewHolder viewHolder, Data model, int position);
}
