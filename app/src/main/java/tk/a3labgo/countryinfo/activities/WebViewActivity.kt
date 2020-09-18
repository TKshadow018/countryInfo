package tk.a3labgo.countryinfo.activities

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.loadBannerAd()

        /////////////////////////////Receive Intent////////////////////////////////
        val intent = intent
        val link = intent.getStringExtra("link")
        val titleText = intent.getStringExtra("title")

        /////////////////////////////Resource Linking////////////////////////////////
        binding.progressPercentage.visibility = View.GONE
        /////////////////////////////Setting Default Value////////////////////////////////
        binding.onDemandButton.text = "×"
        binding.toolbarTitle.maxLines = 1
        binding.toolbarTitle.ellipsize = TextUtils.TruncateAt.END
        binding.toolbarTitle.text = titleText
        binding.progressBar.progress = 0
        binding.webView.webViewClient = WebViewClient()
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true //added to allow non secure(https) site (ex-> http)
        webSettings.setSupportZoom(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.useWideViewPort = true
        binding.webView.setLayerType(WebView.LAYER_TYPE_NONE, null)
        if (link != null) {
            binding.webView.loadUrl(link)
        }


        /////////////////////////////Button Action////////////////////////////////
        binding.onDemandButton.setOnClickListener { finish() }
        binding.backButton.setOnClickListener { _: View? ->
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
        binding.webView.setOnTouchListener(OnTouchListener { _: View?, event: MotionEvent ->
            event.action == MotionEvent.ACTION_MOVE
        })
        binding.webView.isScrollContainer = false
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    binding.progressBar.visibility = View.GONE
                    binding.webView.visibility = View.VISIBLE
                    binding.progressPercentage.visibility = View.GONE
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE)
                    binding.progressPercentage.visibility = View.VISIBLE
                    binding.progressPercentage.text = "$progress%"
                   // binding.progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(this@WebViewActivity, android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN)
                    binding.webView.visibility = View.GONE
                    binding.progressBar.progress = progress
                }
            }
        }
        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.webView.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.webView.visibility = View.VISIBLE
            }
        })
    }

    /////////////////////////////Button Action(Hardware)////////////////////////////////
    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.web_view)
        if (webView!!.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadBannerAd(){
        MobileAds.initialize(this) { }
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}