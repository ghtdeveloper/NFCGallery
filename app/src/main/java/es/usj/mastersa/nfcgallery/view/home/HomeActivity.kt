package es.usj.mastersa.nfcgallery.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.FirebaseApp
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityHomeBinding
import es.usj.mastersa.nfcgallery.presenter.HomeActivityPresenter

class HomeActivity : AppCompatActivity(),ContractInterface.ViewHomeActivity
{
    //Presenter
    private var presenter: HomeActivityPresenter?= null
    private lateinit var rootView:View
    //Binding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        rootView= binding.root
        setContentView(rootView)
        //Init Presenter
        presenter = HomeActivityPresenter(this)
        //Init Views
        initView()
    }//End onCreate

    override fun onStart() {
        super.onStart()
        displayWelcomeSnack()
    }

    override fun onResume() {
        super.onResume()
        displayAnimation()
    }

    override fun initView()
    {


    }//End initView


    override fun displayWelcomeSnack()
    {
        val intent = intent
        presenter!!.showSnackBar(rootView,intent.getStringExtra("username").toString())
    }

    override fun displayAnimation()
    {
       val animation:Animation =AnimationUtils.loadAnimation(this,R.anim.moveanimation)
        binding.imgIconPhone.startAnimation(animation)
    }


}