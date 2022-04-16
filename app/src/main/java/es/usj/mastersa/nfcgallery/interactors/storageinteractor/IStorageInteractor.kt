package es.usj.mastersa.nfcgallery.interactors.storageinteractor

import android.net.Uri
import com.google.firebase.storage.StorageReference


/**
 ****Creado por: Edison Martinez,
 ****Fecha:04,Monday,2022,
 ****Hora: 7:45 PM.
 **/
interface IStorageInteractor
{
    fun depositImage(uuid:String,filename:String) : StorageReference

}