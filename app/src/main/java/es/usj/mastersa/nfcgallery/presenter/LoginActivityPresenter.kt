package es.usj.mastersa.nfcgallery.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.view.home.HomeActivity


/**
 ****Creado por: Edison Martinez,
 ****Fecha:30,Wednesday,2022,
 ****Hora: 9:45 PM.
 **/
class LoginActivityPresenter(_viewLoginActivity: ContractInterface.ViewLoginActivity) :
    ContractInterface.PresenterLoginActivity
{
   private var viewLoginActivity : ContractInterface.ViewLoginActivity = _viewLoginActivity

    init {
        viewLoginActivity.initView()
    }

    override fun showHomeActivity(context: Context, activity: Activity)
    {
        val myIntent = Intent(context,HomeActivity::class.java)
        activity.startActivity(myIntent)

    }//End showHomeActivity


} //End LoginActivityPresenter