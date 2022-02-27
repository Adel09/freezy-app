package com.crumlabs.freezyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.crumlabs.freezyapp.models.CityModelItem
import com.crumlabs.freezyapp.models.CountryModelItem
import com.crumlabs.freezyapp.models.StateModelItem
import com.crumlabs.freezyapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val countries = mutableListOf("Loading")
    val countriesISO = mutableListOf("none")
    val states = mutableListOf("Loading")
    val statesISO = mutableListOf("none")
    val cities = mutableListOf("Loading")
    var ciso = 0

    lateinit var country: String
    lateinit var state: String
    lateinit var city: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        user = auth.currentUser!!
        nameInput.setText(user.displayName)
        emailInput.setText(user.email)




        val apiInterface = ApiInteface.create().getCountries()
        apiInterface.enqueue(object : Callback<List<CountryModelItem>> {
            override fun onResponse(
                    call: Call<List<CountryModelItem>>?,
                    response: Response<List<CountryModelItem>>?
            ) {
                if (response?.body() != null) {
                    for (response in response.body()!!) {
                        countries.add(response.name)
                        countriesISO.add(response.iso2)
                        println(response.name)
                    }
                }

            }

            override fun onFailure(call: Call<List<CountryModelItem>>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                if (t != null) {
                    println(t.message)
                }

            }
        })

        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, countries)
        country_autcompid.setAdapter(adapter)

        country_autcompid.setOnItemClickListener { adapterView, view, i, l ->
            menu1.visibility = View.VISIBLE
            println("Selected ${countries.get(i)}")
            ciso = i

            country = countries.get(i)

            populateStates(i)

        }

        state_autocomp.setOnItemClickListener { adapterView, view, i, l ->
            menu2.visibility = View.VISIBLE
            println("Selected ${states.get(i)}")

            state = states.get(i)
            populateCities(ciso, i)

        }

        city_autocomp.setOnItemClickListener { adapterView, view, i, l ->
            println("Selected ${cities.get(i)}")
            city = cities.get(i)

        }



    }

    fun register(view: View){
        database = Firebase.database.reference
        val newUser = User(user.displayName ,user.email, phoneInput.text.toString(), country, state, city)
        database.child("users").child(user.uid).setValue(newUser)
        val intent = Intent(applicationContext, BaseActivity::class.java)
        startActivity(intent)

    }

    fun populateStates(i: Int){
        val apiInt2 = ApiInteface.create().getStates(countriesISO.get(i))
        apiInt2.enqueue(object : Callback<List<StateModelItem>> {
            override fun onResponse(
                    call: Call<List<StateModelItem>>?,
                    response: Response<List<StateModelItem>>?
            ) {
                if (response?.body() != null) {
                    for (response in response.body()!!) {
                        states.add(response.name)
                        statesISO.add(response.iso2)
                        println("State: $response.name")
                    }
                }

            }

            override fun onFailure(call: Call<List<StateModelItem>>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                if (t != null) {
                    println(t.message)
                }

            }
        })

        val stateAdapter = ArrayAdapter(applicationContext, R.layout.list_item, states)
        state_autocomp.setAdapter(stateAdapter)

    }


    fun populateCities(ciso: Int, siso: Int){
        val apiInt2 = ApiInteface.create().getCities(countriesISO.get(ciso), statesISO.get(siso))
        apiInt2.enqueue(object : Callback<List<CityModelItem>> {
            override fun onResponse(
                    call: Call<List<CityModelItem>>?,
                    response: Response<List<CityModelItem>>?
            ) {
                if (response?.body() != null) {
                    for (response in response.body()!!) {
                        cities.add(response.name)
                        println("City: $response.name")
                    }
                }

            }

            override fun onFailure(call: Call<List<CityModelItem>>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                if (t != null) {
                    println(t.message)
                }

            }
        })

        val cityAdapter = ArrayAdapter(applicationContext, R.layout.list_item, cities)
        city_autocomp.setAdapter(cityAdapter)

    }


}