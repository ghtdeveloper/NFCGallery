package es.usj.mastersa.nfcgallery.presenter

import android.content.Context
import android.nfc.NfcAdapter
import android.view.View
import com.google.android.material.snackbar.Snackbar
import es.usj.mastersa.nfcgallery.contract.ContractInterface


/**
 ****Creado por: Edison Martinez,
 ****Fecha:30,Wednesday,2022,
 ****Hora: 10:14 PM.
 **/
class HomeActivityPresenter(_viewHomeActivity: ContractInterface.ViewHomeActivity,context: Context):
    ContractInterface.PresenterHomeActivity
{
    //Views
    private var viewHomeActivity: ContractInterface.ViewHomeActivity = _viewHomeActivity
    //Objects
    private var nfcAdapter: NfcAdapter

    init {
        viewHomeActivity.initView()
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    }



    override fun showSnackBar(view: View, username: String)
    {
        Snackbar.make(view, "Welcome $username",
            Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }


}