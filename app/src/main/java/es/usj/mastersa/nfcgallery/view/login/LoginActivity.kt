package es.usj.mastersa.nfcgallery.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityLoginBinding
import es.usj.mastersa.nfcgallery.presenter.LoginActivityPresenter

class LoginActivity : AppCompatActivity(),ContractInterface.ViewLoginActivity
{
    //Object Presenter
    private var presenter: LoginActivityPresenter? = null
    //Binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Init Object
        presenter = LoginActivityPresenter(this)
        //Init Viws
        initView()
    }//End onCreate


    override fun initView()
    {
        binding.btnSignIn.setOnClickListener {
            presenter?.showHomeActivity(this,this)
        }
    }//End initView

    override fun onBackPressed() {}


}//End LoginActivity