package es.usj.mastersa.nfcgallery.interactors.firebaseinteractor

import es.usj.mastersa.nfcgallery.domain.User


/**
 ****Creado por: Edison Martinez,
 ****Fecha:02,Saturday,2022,
 ****Hora: 5:20 PM.
 **/
interface IFirebaseInteractor
{
    fun addUser(user:User,uuid:String)

}