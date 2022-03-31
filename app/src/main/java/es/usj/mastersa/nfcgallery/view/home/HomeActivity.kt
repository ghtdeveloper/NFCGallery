package es.usj.mastersa.nfcgallery.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityHomeBinding
import es.usj.mastersa.nfcgallery.presenter.HomeActivityPresenter

class HomeActivity : AppCompatActivity(),ContractInterface.ViewHomeActivity
{
    //Presenter
    private var presenter: HomeActivityPresenter?= null

    //Binding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Init Presenter

    }//End onCreate

    override fun initView()
    {
        //Do Something

    }//End initView

}//End HomeActivity