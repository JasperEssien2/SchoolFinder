package com.example.android.schoolfinder.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {
    private List<Certificate> certificateList;
    private List<Certificate> certificates;
    private Activity activity;

    public CertificateAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    public CertificateAdapter(Activity activity, List<Certificate> certificates) {
        this.activity = activity;

        this.certificates = certificates;
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_cert, parent, false);

        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        Certificate certificate = certificateList.get(holder.getAdapterPosition());
        Picasso
                .get()
                .load(certificate.getImageOfCert().getImageUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Create a dialog showing the details of the certificate
            }
        });
    }

    public void setCertificateList(List<Certificate> certificates) {
        certificateList = certificates;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return certificateList == null ? 0 : certificateList.size();
    }

    public class CertificateViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CertificateViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cert_image);
        }
    }
}
