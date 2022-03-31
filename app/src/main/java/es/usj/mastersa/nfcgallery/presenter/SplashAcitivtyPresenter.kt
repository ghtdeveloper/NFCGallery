package es.usj.mastersa.nfcgallery.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.view.login.LoginActivity


/**
 ****Creado por: Edison Martinez,
 ****Fecha:30,Wednesday,2022,
 ****Hora: 8:02 PM.
 **/
class SplashAcitivtyPresenter (_viewSplashScreen: ContractInterface.ViewSplashScreen) :
    ContractInterface.PresenterSplashActivity
{

    private var viewSplashScreen: ContractInterface.ViewSplashScreen = _viewSplashScreen

   init
     {
        viewSplashScreen.initView()
    }//Endf initView

    override fun showSplashScreen(context: Context, activity: Activity)
    {
        android.os.Handler().postDelayed(
            {
                val myIntent = Intent(context,LoginActivity:: class.java)
                activity.startActivity(myIntent)
                activity.finish()
            }, 1300)
    }//End showSplashScreen


}//End SplashAcitivtyPresenter
