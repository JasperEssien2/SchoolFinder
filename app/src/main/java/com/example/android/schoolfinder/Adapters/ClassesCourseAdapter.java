package com.example.android.schoolfinder.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.DialogFragments.ShowUserDetailFragment;
import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ItemClassBinding;
import com.example.android.schoolfinder.databinding.ItemCourseClassBinding;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;
import com.example.android.schoolfinder.normalUsers.Fragments.ClassCoursesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassesCourseAdapter extends RecyclerView.Adapter<ClassesCourseAdapter.ClassCourseViewHolder> {
    private List<Course> mCourseList = new ArrayList<>();
    private List<Class> mClassList = new ArrayList<>();
    private boolean isCourseViewType;
    private Activity mActivity;
    private ItemCourseClassBinding itemCourseClassBinding;
    private ItemClassBinding itemClassBinding;
    private School school;
    private boolean isNormalUser = false;

    public ClassesCourseAdapter(boolean isCourseViewType, Activity activity, School school) {
        super();
        this.isCourseViewType = isCourseViewType;
        mActivity = activity;
        this.school = school;
    }

    /**
     * Sets which flavour its calling this adapter
     *
     * @param isNormalUser
     */
    public void isNormalUser(boolean isNormalUser) {

        this.isNormalUser = isNormalUser;
    }

    @NonNull
    @Override
    public ClassCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCourseViewType) {
            itemCourseClassBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(mActivity), R.layout.item_course_class, parent, false);
            return new ClassCourseViewHolder(itemCourseClassBinding.getRoot());
        } else {
            itemClassBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(mActivity), R.layout.item_class, parent, false);
            return new ClassCourseViewHolder(itemClassBinding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassCourseViewHolder holder, int position) {
        if (isCourseViewType) {
            itemCourseClassBinding.itemCourseClassTeacherProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if (!isCourseViewType) {
                    if (school != null) {
                        Bundle b = new Bundle();
                        b.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
                        ShowUserDetailFragment detailFragment = ShowUserDetailFragment.newInstance(b);
                        detailFragment.initSchool(ClassesCourseAdapter.this, school, holder.getAdapterPosition());
                        if (isNormalUser) detailFragment.setNormalUser(true);
                        detailFragment.show(((AppCompatActivity) mActivity).getSupportFragmentManager(), null);
//                }
                    }
                }
            });
        } else {
            itemClassBinding.itemCourseClassTeacherProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if (!isCourseViewType) {
                    if (school != null) {
                        Bundle b = new Bundle();
                        b.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                        ShowUserDetailFragment detailFragment = ShowUserDetailFragment.newInstance(b);
                        detailFragment.initSchool(ClassesCourseAdapter.this, school, holder.getAdapterPosition());
                        if (isNormalUser) detailFragment.setNormalUser(true);
                        detailFragment.show(((AppCompatActivity) mActivity).getSupportFragmentManager(), null);
//                }
                    }
                }
            });
        }
        if (isCourseViewType)
            bindViewsForCourse(holder);
        else bindViewsForClass(holder);
    }

    /**
     * This method binds view if the fragment is for class
     *
     * @param holder
     */
    private void bindViewsForClass(ClassCourseViewHolder holder) {
        final Class clas = mClassList.get(holder.getAdapterPosition());
        holder.courseClassText.setText(clas.getNameOfClass());
        if (clas.getHeadOfClass() != null && clas.getHeadOfClass().getProfileImageUrl() != null)
            new PicassoImageLoader(mActivity, clas.getHeadOfClass().getProfileImageUrl(),
                    R.color.colorLightGrey, R.color.colorLightGrey, holder.headImage);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNormalUser) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Select")
                            .setItems(new String[]{"View Courses", "Apply"}, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    if (which == 0) {
                                        openCoursesOfferedFragment((ArrayList<Course>) clas.getCoursesOffered());
                                    } else {
//                                        TODO: Action for applying to school
                                    }
                                }
                            });
                    builder.create().show();
                }
            }
        });
//        else holder.headImage.setSo
    }

    private void openCoursesOfferedFragment(ArrayList<Course> courses) {
        Bundle bundle = new Bundle();
        if (school != null) //TODO: passing in the school courses offered is for test purpose, pass class courses instead
            bundle.putParcelableArrayList(BundleConstants.COURSES_BUNDLE, (ArrayList<? extends Parcelable>) school.getCourses());
        ((SchoolDetailActivity) mActivity).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.school_detail_parent, ClassCoursesFragment.newInstance(bundle), "")
                .addToBackStack(null)
                .commit();
    }

    private void setCourseItemBackgroundImage(String name, CircleImageView background) {
        if (name.toLowerCase().startsWith("Computer".toLowerCase()) || name.toLowerCase().endsWith("Computer".toLowerCase()))
            Picasso.get()
                    .load(R.drawable.ic_computer_course)
                    .into(background);
//            new PicassoImageLoader(mActivity, R.drawable.ic_computer_course,
//                    R.color.colorLightGrey, R.color.colorLightGrey, background);

        else if (name.toLowerCase().startsWith("Mathematics".toLowerCase()) || name.toLowerCase().endsWith("Mathematics".toLowerCase()))
            new PicassoImageLoader(mActivity, R.drawable.ic_math,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);

        else if (name.toLowerCase().startsWith("Geography".toLowerCase()) || name.toLowerCase().endsWith("Geography".toLowerCase()))
            new PicassoImageLoader(mActivity, R.drawable.ic_geography_course,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);

        else if (name.toLowerCase().startsWith("Biology".toLowerCase()) || name.toLowerCase().endsWith("Biology".toLowerCase()))
            new PicassoImageLoader(mActivity, R.drawable.ic_biology_course,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);

        else if (name.toLowerCase().startsWith("physics".toLowerCase()) || name.toLowerCase().endsWith("physics".toLowerCase()))
            new PicassoImageLoader(mActivity, R.drawable.ic_physics_course,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);

        else if (name.toLowerCase().startsWith("chemistry".toLowerCase()) || name.toLowerCase().endsWith("chemistry".toLowerCase()))
            new PicassoImageLoader(mActivity, R.drawable.ic_chemistry_course,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);
        else {
            new PicassoImageLoader(mActivity, R.drawable.ic_course_place_holder,
                    R.color.colorLightGrey, R.color.colorLightGrey, background);
        }
    }

    /**
     * This method binds view if the fragment is for course
     *
     * @param holder
     */
    private void bindViewsForCourse(ClassCourseViewHolder holder) {
        Course course = mCourseList.get(holder.getAdapterPosition());
        holder.courseClassText.setText(course.getCourseName());
        setCourseItemBackgroundImage(course.getCourseName(), holder.backgroundImage);
        if (course.getHeadTeacherOfCourse() != null &&
                course.getHeadTeacherOfCourse().getProfileImageUrl() != null) {
            new PicassoImageLoader(mActivity, course.getHeadTeacherOfCourse().getProfileImageUrl(),
                    R.color.colorLightGrey, R.color.colorLightGrey, holder.headImage);
        }
    }

    public void setSchool(School school) {

        this.school = school;
    }

    /**
     * This method sets the course list
     *
     * @param courses
     */
    public void setCourseList(List<Course> courses) {
        if (courses != null)
            this.mCourseList = courses;

        notifyDataSetChanged();
    }

    /**
     * This method sets the class list
     *
     * @param classes
     */
    public void setClassList(List<Class> classes) {
        if (classes != null)
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

    public void itemChanged(Class aClass, int pos) {
        notifyItemChanged(pos);
    }

    public void itemChanged(Course aCourse, int pos) {
        notifyItemChanged(pos);
    }

    public class ClassCourseViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView backgroundImage;
        private CircleImageView headImage;
        private TextView courseClassText;
        private ImageButton more;

        public ClassCourseViewHolder(View itemView) {
            super(itemView);
            if (isCourseViewType) {
                backgroundImage = itemCourseClassBinding.itemBackgroundImage;
                headImage = itemCourseClassBinding.itemCourseClassTeacherProfile;
                courseClassText = itemCourseClassBinding.itemCourseClassName;
            } else {
                headImage = itemClassBinding.itemCourseClassTeacherProfile;
                courseClassText = itemClassBinding.itemCourseClassName;
                more = itemClassBinding.more;
            }
        }
    }
}
