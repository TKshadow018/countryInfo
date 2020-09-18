package tk.a3labgo.countryinfo.activities
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import tk.a3labgo.countryinfo.R
import tk.a3labgo.countryinfo.databinding.ActivityLauncherBinding


class LauncherActivity : AppCompatActivity() {
    private val splashTimeOut = 3500
    private lateinit var  topAnimantion: Animation
    private lateinit var  topAnimantion1: Animation
    private lateinit var  topAnimantion2: Animation
    private lateinit var  topAnimantion3: Animation
    private lateinit var  middleAnimation: Animation
    private lateinit var  bottomAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Animation Calls
        topAnimantion = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.top_animation)
        topAnimantion1 = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.top_animation1)
        topAnimantion2 = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.top_animation2)
        topAnimantion3 = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.top_animation3)
        bottomAnimation = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.bottom_animation)
        middleAnimation = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.middle_animation)

        //-----------Setting Animations to the elements of Splash
        binding.firstLine.animation = topAnimantion1
        binding.secondLine.animation = topAnimantion2
        binding.thirdLine.animation = topAnimantion3
        binding.fourthLine.animation = topAnimantion
        binding.fifthLine.animation = topAnimantion1
        binding.sixthLine.animation = topAnimantion2
        binding.a.animation = middleAnimation
        binding.tagLine.animation = bottomAnimation

        //Splash Screen Code to call new Activity after some time
        Handler(Looper.getMainLooper()).postDelayed(Runnable { // This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@LauncherActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        }, splashTimeOut.toLong())
    }
}

