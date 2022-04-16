package es.usj.mastersa.nfcgallery.presenter

import android.util.Log
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.interactors.storageinteractor.StorageInteractor


/**
 ****Creado por: Edison Martinez,
 ****Fecha:04,Monday,2022,
 ****Hora: 7:59 PM.
 **/
class StorageCloudPresenter (_viewGalleryActivity: ContractInterface.ViewGalleryActivity)
    : ContractInterface.IStoragePresenter
{
    //iteractor
    private var interactor : StorageInteractor? = null
    //view
    private var viewGalleryActivity: ContractInterface.ViewGalleryActivity = _viewGalleryActivity
    //Variable
    private var urlPict:String = ""

    init {
        interactor = StorageInteractor()
        viewGalleryActivity.initView()
    }

    override fun uploadImage(uuid: String, filename: String, byte: ByteArray)
    {
       val uploadTask = interactor!!.depositImage(uuid, filename).putBytes(byte)
            uploadTask.addOnSuccessListener {
                Log.d(this.javaClass.name,"Succcesfull upload")
            }.addOnFailureListener {
                Log.e(this.javaClass.name,"Unsucccesfull upload")
            }

        val urlTak = uploadTask.continueWithTask { task ->
            if(!task.isSuccessful)
            {
                task.exception?.let {
                    throw  it
                }
            }
            interactor!!.depositImage(uuid, filename).downloadUrl
        }.addOnCompleteListener {
            if(it.isSuccessful)
            {
                val dowloadUri = it.result
                urlPict = dowloadUri.toString()
                //Log.d(this.javaClass.name, "URL GET $dowloadUri")
            }else
            {
                Log.d(this.javaClass.name,"The URL is not available yet")
            }
        }
    }

    override fun getUrlPict(): String {
        return  urlPict
    }

}