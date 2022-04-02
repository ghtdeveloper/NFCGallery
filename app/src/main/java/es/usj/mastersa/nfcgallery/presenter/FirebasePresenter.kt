package es.usj.mastersa.nfcgallery.presenter

import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.domain.User
import es.usj.mastersa.nfcgallery.interactors.firebaseinteractor.FirebaseInteractor


/**
 ****Creado por: Edison Martinez,
 ****Fecha:02,Saturday,2022,
 ****Hora: 5:23 PM.
 **/
class FirebasePresenter(_viewLoginActivity:ContractInterface.ViewLoginActivity )
    :ContractInterface.IFirebasePresenter
{
    //Interactor
    private var interactor: FirebaseInteractor? = null
    //View
    private var viewLoginActivity : ContractInterface.ViewLoginActivity = _viewLoginActivity

    init {
        interactor = FirebaseInteractor()
        viewLoginActivity.initView()
    }

    override fun addUser(user: User, uuid: String)
    {
        interactor!!.addUser(user, uuid)
    }


}