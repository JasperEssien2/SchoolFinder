package com.example.android.countryregioncitypicker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.Models.GeoNamesViewModels;
import com.example.android.countryregioncitypicker.databinding.DialogFragmentCountryPickerBinding;

import java.util.List;

public class CountryPickerDialogFragment extends DialogFragment {

    private DialogFragmentCountryPickerBinding binding;
    private OnCountrySelected onCountrySelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_country_picker, container, false);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), onCountrySelected,true);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GeoNamesViewModels.CountriesViewModel viewModel = new ViewModelProvider.NewInstanceFactory().create(GeoNamesViewModels.CountriesViewModel.class);
        viewModel.getCountryList().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable List<Country> countries) {
//                if(countries != null)
                    adapter.setCountryList(countries);
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return binding.getRoot();
    }

    public void initCountrySelectedListener(OnCountrySelected onCountrySelected){

        this.onCountrySelected = onCountrySelected;
    }
}
