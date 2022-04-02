package es.usj.mastersa.nfcgallery.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import es.usj.mastersa.nfcgallery.R
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
    //View
    private var viewLoginActivity : ContractInterface.ViewLoginActivity = _viewLoginActivity

    init {
        viewLoginActivity.initView()
    }
    override fun showHomeActivity(context: Context, activity: Activity)
    {
        val myIntent = Intent(context,HomeActivity::class.java)
        activity.startActivity(myIntent)
    }//End showHomeActivity

}