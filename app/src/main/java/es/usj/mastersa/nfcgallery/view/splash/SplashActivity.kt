package es.usj.mastersa.nfcgallery.view.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.presenter.SplashAcitivtyPresenter

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() , ContractInterface.ViewSplashScreen
{
    //Objetc Presenter
    private var presenter: SplashAcitivtyPresenter? = null
   // private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Init Presenter
        presenter = SplashAcitivtyPresenter(this)
       // auth =  Firebase.auth
    }//End onCreate

    override fun onStart()
    {
        super.onStart()
        initView()
    }//End initView

    override fun initView()
    {
        presenter?.showSplashScreen(this,this)

    }//End initView

}//End SplashActivity