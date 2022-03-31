package es.usj.mastersa.nfcgallery.view.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.presenter.SplashAcitivtyPresenter

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() , ContractInterface.ViewSplashScreen
{
    //Objetc Presenter
    private var presenter: SplashAcitivtyPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Init Presenter
        presenter = SplashAcitivtyPresenter(this)
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