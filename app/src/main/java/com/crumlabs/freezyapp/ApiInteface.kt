package com.crumlabs.freezyapp


import com.crumlabs.freezyapp.models.CityModelItem
import com.crumlabs.freezyapp.models.CountryModelItem
import com.crumlabs.freezyapp.models.StateModelItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInteface {
    
    //Get API Key from countrystatecity.in

    @Headers("X-CSCAPI-KEY: c0wyZ.......")
    @GET("v1/countries")
    fun getCountries() : Call<List<CountryModelItem>>

    @Headers("X-CSCAPI-KEY: c0wy.......")
    @GET("v1/countries/{iso2}/states")
    fun getStates(@Path("iso2") iso: String) : Call<List<StateModelItem>>

    @Headers("X-CSCAPI-KEY: c0wy.....")
    @GET("v1/countries/{ciso}/states/{siso}/cities")
    fun getCities(@Path("ciso") ciso: String, @Path("siso") siso: String) : Call<List<CityModelItem>>

    companion object {

        var BASE_URL = "https://api.countrystatecity.in/"

        fun create() : ApiInteface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInteface::class.java)

        }
    }


}
