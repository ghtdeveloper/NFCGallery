package es.usj.mastersa.nfcgallery.contract

import android.app.Activity
import android.content.Context


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
    }

    interface ViewHomeActivity
    {
        fun initView()
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
        //Do Something
    }


}//End ContraactInterface