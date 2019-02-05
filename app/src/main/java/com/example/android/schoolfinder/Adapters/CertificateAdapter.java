package com.example.android.schoolfinder.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.Certificate;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {
    private List<Certificate> certificateList;

    public CertificateAdapter() {
        super();
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return certificateList == null ? 0: certificateList.size();
    }

    public class CertificateViewHolder extends RecyclerView.ViewHolder{
        public CertificateViewHolder(View itemView) {
            super(itemView);
        }
    }
}
