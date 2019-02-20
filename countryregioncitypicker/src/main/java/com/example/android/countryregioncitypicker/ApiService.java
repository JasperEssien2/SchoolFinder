package com.example.android.countryregioncitypicker;

import com.example.android.countryregioncitypicker.Models.CountriesResponse;
import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.Models.StateRegion;
import com.example.android.countryregioncitypicker.Models.StatesRegionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://api.geonames.org/";
    String COUNTRY_END = "countryInfoJSON";
    String STATE_END = "childrenJSON";
    String REVERSE_GEOCODING = "countrySubdivisionJSON";

    @GET(COUNTRY_END)
    Call<CountriesResponse> getCountries(@Query("username") String username);

    @GET(STATE_END)
    Call<StatesRegionsResponse> getStatesRegions(@Query("geonameId") int geoNameId, @Query("username") String username);

    @GET(REVERSE_GEOCODING)
    Call<Country> getCountryWithLatLong(@Query("lat") double latitude, @Query("lng")double longitude,
                                        @Query("username") String username);


}
