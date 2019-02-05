package com.example.android.schoolfinder.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.Course;

import java.util.List;

public class ClassesCourseAdapter extends RecyclerView.Adapter<ClassesCourseAdapter.ClassCourseViewHolder> {
    private List<Course> mCourseList;
    private List<Class> mClassList;
    private boolean isCourseViewType;

    public ClassesCourseAdapter(boolean isCourseViewType) {
        super();
        this.isCourseViewType = isCourseViewType;
    }

    @NonNull
    @Override
    public ClassCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassCourseViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return isCourseViewType ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        if(isCourseViewType)
            return mCourseList == null ? 0: mCourseList.size();
        else return mClassList == null ? 0: mClassList.size();
    }

    public class ClassCourseViewHolder extends RecyclerView.ViewHolder{
        public ClassCourseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
