package com.example.android.schoolfinder.DialogFragments;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.android.schoolfinder.Adapters.ClassesCourseAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.DialogFragmentShowTeacherDetailBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ShowUserDetailFragment extends DialogFragment implements AuthenticationCallbacks, MediaStorageCallback {
    private static final int SELECT_PHOTO = 45;
    private DialogFragmentShowTeacherDetailBinding binding;
    private ClassesCourseAdapter adapter;
    private School school;
    private int pos;
    private MediaStorage mediaStorage;
    private Authentication authentication;
    private Uri imageUri;
    private boolean isClass = false;
    private boolean isEditButton = false;
    private boolean isNormalUser = false;
    private Users head = new Users();

    public static ShowUserDetailFragment newInstance(Bundle bundle) {
        ShowUserDetailFragment fragment = new ShowUserDetailFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mediaStorage = new MediaStorage(this);
        this.authentication = new Authentication(this);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_show_teacher_detail, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog_background_7dp);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
        }
        if (isNormalUser) binding.editDoneButton.setVisibility(View.GONE);
        binding.teacherDetailUsername.setEnabled(false);
        binding.teacherDetailBiography.setEnabled(false);
        binding.editDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditButton = !isEditButton;
                if (isEditButton) {
                    editButtonClicked();
                    binding.editDoneButton.setText("Done");
                } else {
                    doneButtonClicked();
                    binding.editDoneButton.setText("Edit");
                }
            }
        });

        binding.teacherDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditButton) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else
                    Toast.makeText(getActivity(), "Click edit button to change profile pic", Toast.LENGTH_SHORT).show();
            }
        });
        setUpViewsWithData();
        return binding.getRoot();
    }

    /**
     * This method is used to initialize the school object
     *
     * @param school the instance of the school
     * @param pos    the position oft the item in the adapter
     */
    public void initSchool(ClassesCourseAdapter adapter, School school, int pos) {
        this.adapter = adapter;
        this.school = school;
        this.pos = pos;

    }

    /**
     * Action to take when editbutton is clicked
     */
    private void editButtonClicked() {
        binding.teacherDetailUsername.setEnabled(true);
        binding.teacherDetailBiography.setEnabled(true);
    }

    /**
     * Action to take when done button is clicked
     */
    private void doneButtonClicked() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.teacherDetailUsername.setEnabled(false);
        binding.teacherDetailBiography.setEnabled(false);
        String formerHeadName = "";
        if (school != null) {
            if (isClass) {
                if (school.getClasses() != null) {
                    head = school.getClasses().get(pos).getHeadOfClass();
                    if (head == null) {
                        head = new Users();
                        head.setId(getUniqueId());
                    }
                    if (head.getId() == null) head.setId(getUniqueId());
                    formerHeadName = head.getName();
                    head.setName(binding.teacherDetailUsername.getText().toString());
                    head.setBiography(binding.teacherDetailBiography.getText().toString());
                    school.getClasses().get(pos).setHeadOfClass(head);
                    authentication.putNewUserInDb(school);
                    if (imageUri != null) {
//                        if (!formerHeadName.isEmpty()) {
//                            mediaStorage.addSchoolImages(imageUri, "Teacher-" + head.getId(), "Teacher-" + head.getName());
//                        } else
                        mediaStorage.addSchoolImages(imageUri, "Teacher-" + head.getId(), null);
                    }
                }
            } else {
                if (school.getCourses() != null) {
                    head = school.getCourses().get(pos).getHeadTeacherOfCourse();
                    if (head == null) {
                        head = new Users();
                        head.setId(getUniqueId());
                    }
                    if (head.getId() == null) head.setId(getUniqueId());
                    formerHeadName = head.getName();
                    head.setName(binding.teacherDetailUsername.getText().toString());
                    head.setBiography(binding.teacherDetailBiography.getText().toString());
                    school.getCourses().get(pos).setHeadTeacherOfCourse(head);

                    if (imageUri != null) {
                        Log.e("ShowUserDetailFragment", "imageUri != null");
//                        if (!formerHeadName.isEmpty()) {
//                            mediaStorage.addSchoolImages(imageUri, "Teacher-Course" + head.getId(), "Teacher-Course" + head.getName() + head.getId());
//                        } else
                        mediaStorage.addSchoolImages(imageUri, "Teacher-Course" + head.getId(), null);
                    } else {
                        Log.e("ShowUserDetailFragment", "imageUri == null");
                        authentication.putNewUserInDb(school);
                    }
                }
            }
        }
    }

    /**
     * This method returns a unique id from firebase
     *
     * @return a string of the unique id
     */
    private String getUniqueId() {
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    /**
     * This method sets up views with data
     */
    private void setUpViewsWithData() {

        if (school != null) {
            if (isClass) {
                if (school.getClasses() != null) {
                    Users head = school.getClasses().get(pos).getHeadOfClass();
                    if (head != null) {
                        binding.teacherDetailBiography.setText(head.getBiography() != null ? head.getBiography() : "");
                        binding.teacherDetailUsername.setText(head.getName() != null ? head.getName() : "");
                        if (head.getProfileImageUrl() != null && !head.getProfileImageUrl().isEmpty())
                            new PicassoImageLoader(getActivity(), head.getProfileImageUrl(), R.drawable.place_holder,
                                    R.drawable.place_holder, binding.teacherDetailImageView);
                    }
                }
            } else {
                if (school.getCourses() != null) {
                    Users head = school.getCourses().get(pos).getHeadTeacherOfCourse();
                    if (head != null) {
                        binding.teacherDetailBiography.setText(head.getBiography() != null ? head.getBiography() : "");
                        binding.teacherDetailUsername.setText(head.getName() != null ? head.getName() : "");
                        if (head.getProfileImageUrl() != null && !head.getProfileImageUrl().isEmpty())
                            new PicassoImageLoader(getActivity(), head.getProfileImageUrl(), R.drawable.place_holder,
                                    R.drawable.place_holder, binding.teacherDetailImageView);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (data == null) return;
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();

                    if (imageUri != null) {
                        new PicassoImageLoader(getActivity(), imageUri.toString(), R.color.colorLightGrey,
                                R.color.colorLightGrey, binding.teacherDetailImageView);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {

    }

    @Override
    public void userInsertedToDatabase(Users users) {

    }

    @Override
    public void userInsertedToDatabase(School school) {
        if (school != null) {
            if (getContext() != null)
                Toast.makeText(getContext(), "Data stored successfully", Toast.LENGTH_SHORT).show();
            this.school = school;
            setUpViewsWithData();
        } else {
            if (getContext() != null)
                Toast.makeText(getContext(), "Error in saving data, try again", Toast.LENGTH_SHORT).show();
        }
        binding.progressbar.setVisibility(View.GONE);
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {

    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void accountUpdated(boolean isEmail, boolean isSuccessful) {

    }

    @Override
    public void profileImageStored(String imageUrl, boolean isSuccesful) {

    }

    @Override
    public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {

    }

    @Override
    public void profileImageDeleted(boolean isSuccessful) {

    }

    @Override
    public void logoStored(String imageUrl) {

    }

    @Override
    public void schoolImageAdded(String imageUrl, String tag) {
        if (isClass) {
            if (tag.equals("Teacher-" + head.getId())) {
                head.setProfileImageUrl(imageUrl);
                school.getClasses().get(pos).setHeadOfClass(head);
                adapter.itemChanged(school.getClasses().get(pos), pos);
                authentication.putNewUserInDb(school);
            }
        } else {
            if (tag.equals("Teacher-Course" + head.getId())) {
                head.setProfileImageUrl(imageUrl);
                school.getCourses().get(pos).setHeadTeacherOfCourse(head);
                adapter.itemChanged(school.getCourses().get(pos), pos);
                authentication.putNewUserInDb(school);
            }
        }
    }

    @Override
    public void schoolImageAdded(List<Image> images, boolean isSuccessful) {

    }

    @Override
    public void postImageAdded(Post post, boolean isSuccessful) {

    }

    public void setNormalUser(boolean normalUser) {
        isNormalUser = normalUser;
    }
}
