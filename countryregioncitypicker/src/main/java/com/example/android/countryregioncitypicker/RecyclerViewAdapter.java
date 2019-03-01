package com.example.android.countryregioncitypicker;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.Models.StateRegion;
import com.example.android.countryregioncitypicker.databinding.ItemRecycleBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> implements Filterable {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();
    private List<Country> countryList = new ArrayList<>();
    private List<StateRegion> stateRegionList = new ArrayList<>();
    private Activity activity;
    private OnCountrySelected onCountrySelected;
    private boolean isCountry;
    private ItemRecycleBinding binding;

    public RecyclerViewAdapter(Activity activity, OnCountrySelected onCountrySelected, boolean isCountry) {
        super();
        this.activity = activity;
        this.onCountrySelected = onCountrySelected;
        this.isCountry = isCountry;
    }

//    public RecyclerViewAdapter(Activity activity, OnCountrySelected onCountrySelected, boolean isCountry) {
//        super();
//        this.activity = activity;
//        this.isCountry = isCountry;
//    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_recycle, viewGroup, false);
        return new RecyclerViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
        if (isCountry) {
            final Country country = countryList.get(viewHolder.getAdapterPosition());
//            viewHolder.countryFlag.
            viewHolder.name.setText(country.getCountryName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCountrySelected.countrySelected(country);
                }
            });
        } else {
            StateRegion stateRegion = stateRegionList.get(viewHolder.getAdapterPosition());
            viewHolder.name.setText(stateRegion.getToponymName());
        }
    }

    @Override
    public int getItemCount() {
        if (isCountry)
            return countryList.size();
        else
            return stateRegionList.size();
    }

    public void setCountryList(List<Country> countries) {
//        countryList.clear();
//        countryList = countries;
//        notifyDataSetChanged();
        int i = 0;
        for (Country country : countries) {
            countryList.add(country);
            notifyItemInserted(i);
            i++;
        }
    }

    public void setStateRegionList(List<StateRegion> stateRegions) {
        stateRegionList.clear();
        int i = 0;
        for (StateRegion stateRegion : stateRegions) {
            stateRegionList.add(stateRegion);
            notifyItemInserted(i);
            i++;
        }
    }

    private List<Country> filteredCountryList;
    private List<StateRegion> filteredStateRegionList;

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    if (isCountry)
                        filteredCountryList = countryList;
                    else filteredStateRegionList = stateRegionList;
                } else {
                    if (isCountry) {
                        List<Country> filteredList = new ArrayList<>();
                        for (Country row : countryList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getCountryName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        filteredCountryList = filteredList;
                        Log.e(TAG, "filteredCountryList ------------- " + filteredCountryList.toString());
                    } else {
                        List<StateRegion> filteredList = new ArrayList<>();
                        for (StateRegion row : stateRegionList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getToponymName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        filteredStateRegionList = filteredList;
                        Log.e(TAG, "filteredStateRegionList ------------- " + filteredStateRegionList.toString());
                    }
                }

                FilterResults filterResults = new FilterResults();
                if (isCountry)
                    filterResults.values = filteredCountryList;
                else filterResults.values = filteredStateRegionList;
                return filterResults;
//                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (isCountry)
                    filteredCountryList = (ArrayList<Country>) filterResults.values;
                else filteredStateRegionList = (ArrayList<StateRegion>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView countryFlag;
        private TextView name;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            countryFlag = binding.countryFlag;
            name = binding.name;
        }
    }
}
