package com.example.android.schoolfinder.normalUsers.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Activity activity;
    private final List<String> categories;
    private final int HIGH_SCHOOL = 0, MID_SCHOOL = 2, TERTIARY = 4, NURSERY_KINDERGARTEN = 3,
    OTHERS = 5;

    public CategoryAdapter(Activity activity, List<String> categories) {
        super();
        this.activity = activity;
        this.categories = categories;
    }

    @Override
    public int getItemViewType(int position) {
        String txt = categories.get(position);
        if(txt.equals(FirebaseConstants.HIGH_SCHOOLS_NODE))
            return HIGH_SCHOOL;
        else if(txt.equals(FirebaseConstants.PRIMARY_MID_NODE))
            return MID_SCHOOL;

        else if(txt.equals(FirebaseConstants.TERTIARY_NODE))
            return TERTIARY;
        else if(txt.equals(FirebaseConstants.KINDERGARTEN_NURSERIES_NODE))
            return NURSERY_KINDERGARTEN;
        else if(txt.equals(FirebaseConstants.OTHERS_NODE))
            return OTHERS;
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        TextView textView = new TextView(activity);
//        textView.setPadding(4, 4, 4, 4);
//        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textView1 = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_text, viewGroup, false);
        return new CategoryViewHolder(textView1);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder viewHolder, int i) {
        String txt = categories.get(viewHolder.getAdapterPosition());
        TextView t = ((TextView)viewHolder.itemView);
        t.setText(txt);
        switch (viewHolder.getItemViewType()){
            case HIGH_SCHOOL:
                t.setBackgroundResource(R.drawable.category_background_cyan_200);
                break;
            case MID_SCHOOL:
                t.setBackgroundResource(R.drawable.category_background_corn_flower);
                break;

            case TERTIARY:
                t.setBackgroundResource(R.drawable.category_background_hot_pink);
                break;
            case NURSERY_KINDERGARTEN:
                t.setBackgroundResource(R.drawable.category_background_orange);
                break;
            case OTHERS:
                t.setBackgroundResource(R.drawable.category_background_corn_flower);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
