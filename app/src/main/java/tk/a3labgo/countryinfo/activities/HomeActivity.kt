package tk.a3labgo.countryinfo.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tk.a3labgo.countryinfo.api.getAPI
import tk.a3labgo.countryinfo.adapters.HomeAdapter
import tk.a3labgo.countryinfo.models.Country
import tk.a3labgo.countryinfo.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var viewAdapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
        viewAdapter = HomeAdapter(this@HomeActivity)
        binding.myRecyclerView.adapter = viewAdapter

        //new
        val retrofit = Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api: getAPI = retrofit.create(getAPI::class.java)
        val call: Call<List<Country>> = api.model
        call.enqueue(object :Callback<List<Country>>{
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (!response.isSuccessful){
                    Toast.makeText(applicationContext, response.code(), Toast.LENGTH_SHORT).show()
                }
                val countries: List<Country> = response.body()!!
                viewAdapter.setList(countries)
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
