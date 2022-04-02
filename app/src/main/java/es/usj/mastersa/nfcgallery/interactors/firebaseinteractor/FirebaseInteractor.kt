package es.usj.mastersa.nfcgallery.interactors.firebaseinteractor

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.usj.mastersa.nfcgallery.domain.User


/**
 ****Creado por: Edison Martinez,
 ****Fecha:02,Saturday,2022,
 ****Hora: 5:20 PM.
 **/
class FirebaseInteractor : IFirebaseInteractor
{
    //TAG
    private var TAG: String = this.javaClass.name
    //DB
    private var db = Firebase.firestore

    //Add User in the collection
    override fun addUser(user: User,uuid:String)
    {
        db.collection("Collect_NFC_DB").document("Users")
            .collection("Collect_User_Auth").document(uuid).set(user).
            addOnSuccessListener {
                Log.d(TAG,"User Added With custom ID ")
            }.addOnFailureListener {
                Log.e(TAG,"Error adding user")
            }
    }



}