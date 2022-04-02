package es.usj.mastersa.nfcgallery.contract

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.firebase.auth.FirebaseUser
import es.usj.mastersa.nfcgallery.domain.User


/**
 ****Creado por: Edison Martinez,
 ****Fecha:30,Wednesday,2022,
 ****Hora: 8:01 PM.
 **/
interface ContractInterface
{
    //****************************************Views*************************************************
    interface ViewSplashScreen
    {
        fun initView()
    }

    interface ViewLoginActivity
    {
        fun initView()
        fun signInGoogle()
        fun firebaseAuthWithGoogle(idToken:String)
        fun updateUI(user:FirebaseUser)
        fun showDialogProgress()
    }

    interface ViewHomeActivity
    {
        fun initView()
        fun displayWelcomeSnack()
        fun displayAnimation()
    }


    //***************************************Presenter**********************************************

    interface PresenterSplashActivity
    {
        fun showSplashScreen(context: Context, activity: Activity)
    }

    interface PresenterLoginActivity
    {
        fun showHomeActivity(context: Context,activity: Activity)

    }

    interface PresenterHomeActivity
    {
        fun showSnackBar(view: View,username:String)
    }

    interface IFirebasePresenter
    {
        fun addUser(user:User,uuid:String)
    }


}//End ContraactInterface