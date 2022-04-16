package es.usj.mastersa.nfcgallery.interactors.storageinteractor

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


/**
 ****Creado por: Edison Martinez,
 ****Fecha:04,Monday,2022,
 ****Hora: 7:45 PM.
 **/
class StorageInteractor : IStorageInteractor
{
    //TAG
    //private val TAG: String = javaClass.name
    //Reference Object
    //DB
    private var firebaseStorage: FirebaseStorage? = null

    init {
        firebaseStorage = FirebaseStorage.getInstance()
    }

    override fun depositImage(uuid: String,filename:String): StorageReference
    {
       return firebaseStorage!!.reference.child("root").child("users")
           .child("$uuid/$filename")
    }

}