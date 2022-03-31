package es.usj.mastersa.nfcgallery.presenter

import es.usj.mastersa.nfcgallery.contract.ContractInterface


/**
 ****Creado por: Edison Martinez,
 ****Fecha:30,Wednesday,2022,
 ****Hora: 10:14 PM.
 **/
class HomeActivityPresenter(_viewHomeActivity: ContractInterface.ViewHomeActivity):
    ContractInterface.PresenterHomeActivity
{
    private var viewHomeActivity: ContractInterface.ViewHomeActivity = _viewHomeActivity


    init {
        viewHomeActivity.initView()
    }


}//End HomeActivityPresenter