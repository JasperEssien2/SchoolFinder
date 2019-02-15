package com.example.android.schoolfinder.schoolOwners.DialogFragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentAddClassesBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;

import java.util.ArrayList;
import java.util.List;

public class AddClassDialogFragment extends DialogFragment {

    private AuthenticationCallbacks callbacks;
    private School school;
    private DialogFragmentAddClassesBinding binding;
    private Authentication authentication;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_classes, container, false);
        binding.addClassesDialogProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callbacks != null && school != null) {
                    school.setClasses(getClasses());
                    authentication = new Authentication(callbacks);
                    authentication.putNewUserInDb(school);
                }

            }
        });
        binding.addClassesFragmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return binding.getRoot();
    }

    public void initCallback(AuthenticationCallbacks callbacks, School school){
        this.callbacks = callbacks;
        this.school = school;
    }

    private List<Class> getClasses(){
        List<Class> list = new ArrayList<>();

        //TODO: check for duplicate classes
        if(school.getClasses() != null)
            list = school.getClasses();

        String text = binding.addCourseEditText.getText().toString();
        if(text != null) {
            for(String s : text.split(",\n")){
                Class clas = new Class();
                clas.setNameOfClass(s);
                list.add(clas);
            }
        }
        return list;
    }
}
