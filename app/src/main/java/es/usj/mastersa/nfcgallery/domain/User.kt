package es.usj.mastersa.nfcgallery.domain

import java.util.*


/**
 ****Creado por: Edison Martinez,
 ****Fecha:02,Saturday,2022,
 ****Hora: 5:26 PM.
 **/
data class User(
    val uuid:String,
    val displayName:String,
    val email:String,
    val lastAccesDate:Date
)
