package tk.a3labgo.countryinfo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tk.a3labgo.countryinfo.api.getAPI
import tk.a3labgo.countryinfo.adapters.SpecificAdapter
import tk.a3labgo.countryinfo.models.Country
import tk.a3labgo.countryinfo.models.KeyValue
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.callback.PropertyInterface
import tk.a3labgo.countryinfo.databinding.ActivitySpecificCountryBinding

class SpecificCountryActivity : AppCompatActivity(),PropertyInterface {
    private lateinit var viewAdapter: SpecificAdapter
    private lateinit var border: String
    private lateinit var languages: String
    private lateinit var memberOf: String
    lateinit var mInterstitialAd:InterstitialAd
    var title:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySpecificCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val callingCode = intent.getStringExtra("callingCode")
        val countryName = intent.getStringExtra("countryName")
        binding.myRecyclerViewSpecific.layoutManager = LinearLayoutManager(this)
        this.loadAd()
        this.loadBannerAd()
        viewAdapter = SpecificAdapter(this@SpecificCountryActivity)
        binding.myRecyclerViewSpecific.adapter = viewAdapter
        //new
        val retrofit = Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api: getAPI = retrofit.create(getAPI::class.java)
        val call: Call<List<Country>> = api.getSpecificModel(callingCode)
        call.enqueue(object :Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (!response.isSuccessful) {
                    binding.textView.text = countryName
                    val intent = Intent(this@SpecificCountryActivity, WebViewActivity::class.java)
                    intent.putExtra("title", countryName)
                    intent.putExtra("link", "https://en.wikipedia.org/wiki/$countryName")
                    startActivity(intent)
                } else{
                    val country: List<Country> = response.body()!!
                    var defaultPosition = 0
                    for (i in country.indices){
                        if (country[i].nativeName == countryName){
                            defaultPosition = i
                        }
                    }
                    val imageLoader = ImageLoader.Builder(this@SpecificCountryActivity).componentRegistry {
                        add(SvgDecoder(this@SpecificCountryActivity))
                    }.build()
                    val request = ImageRequest.Builder(this@SpecificCountryActivity).data(country[defaultPosition].flag).target(binding.imageView).build()
                    imageLoader.enqueue(request)
                    binding.textView.text = country[defaultPosition].name
                    val list: ArrayList<KeyValue> = ArrayList()
                    list.add(KeyValue(getString(R.string.fullname),country[defaultPosition].name))
                    list.add(KeyValue(getString(R.string.shortName),country[defaultPosition].alpha3Code))
                    list.add(KeyValue(getString(R.string.localName),country[defaultPosition].nativeName))
                    list.add(KeyValue(getString(R.string.region),country[defaultPosition].subregion+", "+country[defaultPosition].region))
                    list.add(KeyValue(getString(R.string.countryCode),country[defaultPosition].callingCodes[0]))
                    list.add(KeyValue(getString(R.string.capital),country[defaultPosition].capital))
                    list.add(KeyValue(getString(R.string.population),country[defaultPosition].population.toString()+" Person"))
                    list.add(KeyValue(getString(R.string.area),country[defaultPosition].area.toString()+" Km"))
                    list.add(KeyValue(getString(R.string.map),country[defaultPosition].latlng[0].toString()+","+country[defaultPosition].latlng[1].toString()))
                    list.add(KeyValue(getString(R.string.currencyUsed),country[defaultPosition].currencies[0].name))
                    if (country[defaultPosition].timezones.size == 1){
                        list.add(KeyValue(getString(R.string.timezone),country[defaultPosition].timezones[0].toString()))
                    }else{
                        list.add(KeyValue(getString(R.string.timezone),"From "+ country[defaultPosition].timezones[0].toString()+" To "+country[defaultPosition].timezones[country[0].timezones.size-1].toString()))
                    }

                    for (i in country[defaultPosition].borders){
                        border = if (this@SpecificCountryActivity::border.isInitialized){
                            border +"\n" +i
                        }else{
                            i
                        }
                    }
                    for (i in country[defaultPosition].languages){
                        languages = if (this@SpecificCountryActivity::languages.isInitialized){
                            languages +"\n" +i.name
                        }else{
                            i.name
                        }
                    }
                    for (i in country[defaultPosition].regionalBlocs){
                        memberOf = if (this@SpecificCountryActivity::memberOf.isInitialized){
                            memberOf +"\n" +i.acronym
                        }else{
                            i.acronym
                        }
                    }
                    if (this@SpecificCountryActivity::border.isInitialized){
                        list.add(KeyValue(getString(R.string.borderWith),border))
                    }
                    if (this@SpecificCountryActivity::languages.isInitialized){
                        list.add(KeyValue(getString(R.string.spokenLang),languages))
                    }
                    if (this@SpecificCountryActivity::memberOf.isInitialized){
                        list.add(KeyValue(getString(R.string.memberOf),memberOf))
                    }
                    list.add(KeyValue(getString(R.string.gini),country[defaultPosition].gini.toString()))

                    viewAdapter.setSpecificList(list)
                }

            }
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun loadAd() {
        //Ad
        MobileAds.initialize(this@SpecificCountryActivity) {}
        mInterstitialAd = InterstitialAd(this@SpecificCountryActivity)
//        mInterstitialAd.adUnitId = "ca-app-pub-9447458149465385/5343481009"
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener  = object : AdListener() {
            override fun onAdClosed() {
                val intent = Intent(this@SpecificCountryActivity, SpecificCountryActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("link", "https://en.wikipedia.org/wiki/$title")
                startActivity(intent)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Toast.makeText(applicationContext, "Ad finished Loading", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadBannerAd(){
        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun clickedProperty(title: String?) {
        this.title = title.toString()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        } else{
            Toast.makeText(applicationContext, "Ad not Loaded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SpecificCountryActivity, WebViewActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("link", "https://en.wikipedia.org/wiki/$title")
            startActivity(intent)
        }
    }
}