package com.example.android.countryregioncitypicker.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.countryregioncitypicker.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoNamesViewModels {

    public static class CountriesViewModel extends ViewModel {
        private static final String TAG = CountriesViewModel.class.getSimpleName();
        private MutableLiveData<List<Country>> countryList;
        private MutableLiveData<Country> country;


        public LiveData<List<Country>> getCountryList() {
            if (countryList == null) {
                countryList = new MutableLiveData<List<Country>>();
                loadCountries();
            }

            //finally we will return the list
            return countryList;
        }

        public LiveData<Country> getCountry(double latitude, double longitude) {
            if (country == null) {
                country = new MutableLiveData<>();
                Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl(ApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<Country> call = apiService.getCountryWithLatLong(latitude, longitude, "jasperessien");
                call.enqueue(new Callback<Country>() {
                    @Override
                    public void onResponse(Call<Country> call, Response<Country> response) {
                        Log.e(TAG, "OnResponse ---- " +response.body().getCountryName());
                        country.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Country> call, Throwable t) {
                        Log.e(TAG, "OnFailure oooh ---- ");
                    }
                });
            }
            return country;
        }

        private void loadCountries() {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<CountriesResponse> call = apiService.getCountries("jasperessien");
            call.enqueue(new Callback<CountriesResponse>() {
                @Override
                public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                    Log.e(TAG, "Country List -- " + response.body().getGeoNames().toString());
                    if (response.body() != null)
                        countryList.setValue(response.body().getGeoNames());
                }

                @Override
                public void onFailure(Call<CountriesResponse> call, Throwable t) {

                }
            });
        }
    }

    public static class StatesRegionViewModel extends ViewModel {
        private static final String TAG = StatesRegionViewModel.class.getSimpleName();
        private MutableLiveData<List<StateRegion>> stateRegionList;


        public LiveData<List<StateRegion>> getStateRegionList(int countryId) {
            if (stateRegionList == null) {
                stateRegionList = new MutableLiveData<List<StateRegion>>();
                loadStatesRegion(countryId);
            }

            //finally we will return the list
            return stateRegionList;
        }

        private void loadStatesRegion(int countryId) {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<StatesRegionsResponse> call = apiService.getStatesRegions(countryId, "jasperessien");
            call.enqueue(new Callback<StatesRegionsResponse>() {
                @Override
                public void onResponse(Call<StatesRegionsResponse> call, Response<StatesRegionsResponse> response) {
                    Log.e(TAG, response.raw().toString());
                    if (response.body() != null)
                        stateRegionList.setValue(response.body().getGeonames());
                }

                @Override
                public void onFailure(Call<StatesRegionsResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure() ---- " + t.getMessage());
                }
            });
        }
    }
}
