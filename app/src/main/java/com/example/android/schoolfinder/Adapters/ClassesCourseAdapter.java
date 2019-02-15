package com.example.android.schoolfinder.Adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ItemCourseClassBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassesCourseAdapter extends RecyclerView.Adapter<ClassesCourseAdapter.ClassCourseViewHolder> {
    private List<Course> mCourseList;
    private List<Class> mClassList;
    private boolean isCourseViewType;
    private Activity mActivity;
    private ItemCourseClassBinding itemCourseClassBinding;

    public ClassesCourseAdapter(boolean isCourseViewType, Activity activity) {
        super();
        this.isCourseViewType = isCourseViewType;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ClassCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemCourseClassBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mActivity), R.layout.item_course_class, parent, false);

        return new ClassCourseViewHolder(itemCourseClassBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ClassCourseViewHolder holder, int position) {
        if (isCourseViewType)
            bindViewsForCourse(holder);
        else bindViewsForClass(holder);
    }

    private void bindViewsForClass(ClassCourseViewHolder holder) {
        Class clas = mClassList.get(holder.getAdapterPosition());
        holder.courseClassText.setText(clas.getNameOfClass());
        if (clas.getHeadOfClass() != null && clas.getHeadOfClass().getProfileImageUrl() != null)
            Picasso
                    .get()
                    .load(clas.getHeadOfClass().getProfileImageUrl())
                    .centerCrop()
                    .into(holder.headImage);
//        else holder.headImage.setSo
    }

    private void bindViewsForCourse(ClassCourseViewHolder holder) {
        Course course = mCourseList.get(holder.getAdapterPosition());
        holder.courseClassText.setText(course.getCourseName());
        if (course.getHeadTeacherOfCourse() != null &&
                course.getHeadTeacherOfCourse().getProfileImageUrl() != null) {
            Picasso
                    .get()
                    .load(course.getHeadTeacherOfCourse().getProfileImageUrl())
                    .centerCrop()
                    .into(holder.headImage);
        }
    }

    public void setCourseList(List<Course> courses) {
        this.mCourseList = courses;
        notifyDataSetChanged();
    }

    public void setClassList(List<Class> classes) {
        this.mClassList = classes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isCourseViewType ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        if (isCourseViewType)
            return mCourseList == null ? 0 : mCourseList.size();
        else return mClassList == null ? 0 : mClassList.size();
    }

    public class ClassCourseViewHolder extends RecyclerView.ViewHolder {
        private ImageView backgroundImage;
        private CircleImageView headImage;
        private TextView courseClassText;

        public ClassCourseViewHolder(View itemView) {
            super(itemView);
            backgroundImage = itemCourseClassBinding.itemBackgroundImage;
            headImage = itemCourseClassBinding.itemCourseClassTeacherProfile;
            courseClassText = itemCourseClassBinding.itemCourseClassName;
        }
    }
}
