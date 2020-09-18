package tk.a3labgo.countryinfo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.adapters.HomeAdapter
import tk.a3labgo.countryinfo.api.getAPI
import tk.a3labgo.countryinfo.callback.HomeInterface
import tk.a3labgo.countryinfo.databinding.ActivityHomeBinding
import tk.a3labgo.countryinfo.models.Country

class HomeActivity : AppCompatActivity(), HomeInterface {
    private lateinit var viewAdapter: HomeAdapter
    lateinit var mInterstitialAd:InterstitialAd
    var callingCode:String = ""
    var nativeName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
        this.loadAd()
        this.loadBannerAd()
        viewAdapter = HomeAdapter(this@HomeActivity)
        binding.myRecyclerView.adapter = viewAdapter

        //new
        val retrofit = Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api: getAPI = retrofit.create(getAPI::class.java)
        val call: Call<List<Country>> = api.model
        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (!response.isSuccessful) {
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

    private fun loadAd() {
        //Ad
        MobileAds.initialize(this@HomeActivity) {}
        mInterstitialAd = InterstitialAd(this@HomeActivity)
//        mInterstitialAd.adUnitId = "ca-app-pub-9447458149465385/5343481009"
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener  = object : AdListener() {
            override fun onAdClosed() {
                val intent = Intent(this@HomeActivity, SpecificCountryActivity::class.java)
                intent.putExtra("callingCode", callingCode)
                intent.putExtra("countryName", nativeName)
                startActivity(intent)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun loadBannerAd(){
        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun clickedCountry(callingCode: String?, nativeName: String?) {
        this.callingCode = callingCode.toString()
        this.nativeName = nativeName.toString()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        } else{
            val intent = Intent(this@HomeActivity, SpecificCountryActivity::class.java)
            intent.putExtra("callingCode", callingCode)
            intent.putExtra("countryName", nativeName)
            startActivity(intent)
//            Toast.makeText(applicationContext, "Ad not Loaded", Toast.LENGTH_SHORT).show()
        }
    }
}
