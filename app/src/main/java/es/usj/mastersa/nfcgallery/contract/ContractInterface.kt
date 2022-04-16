package es.usj.mastersa.nfcgallery.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.view.View
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
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
        fun showDialogoImage(url:String)
        fun displayWelcomeSnack()
        fun displayAnimation()
        fun displayDialogNoDataTag()
    }

    interface ViewGalleryActivity
    {
        fun initView()
        fun dispatchTakePicture()
        fun pickGalleryImage()
        fun prepareImageToUpload()
        fun uploadImage()
        fun showDialogProgress()
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

    interface PresenterNFCManager
    {
        suspend fun handleIntent(intent: Intent, activity: Activity)
        fun setupForegroundDiskpatch(activity: Activity)
        fun stopForegroundDiskPatch(activity: Activity)
        fun isAdaptaerAvailable(context: Context):Boolean
        fun isAdapterEnable(context: Context):Boolean
        suspend fun doInBackground(tag:Tag) : String
        fun readText(record: NdefRecord) : String
        fun getTextReader() : String
    }

    interface PresenterNFCWriteManager
    {
        fun writeTag(message: NdefMessage, tag: Tag, context: Context,activity: Activity): Boolean
        fun buildNdefMessage(url:String): NdefMessage
        fun showDialogPutTagNFC(context: Context,activity: Activity)
        fun checkNfcEnabled(context: Context)
        suspend fun handleIntent(intent: Intent, activity: Activity,context: Context,url:String)
        fun setupForegroundDiskpatch(activity: Activity)
        fun stopForegroundDiskPatch(activity: Activity)
        fun showHomeActivity(context: Context,activity: Activity)
    }

    interface IFirebasePresenter
    {
        fun addUser(user:User,uuid:String)
    }

    interface IStoragePresenter
    {
        //fun depositImage(uuid: String): StorageReference
        fun uploadImage(uuid:String, filename:String,byte: ByteArray)
        fun getUrlPict():String
    }


}//End ContraactInterface