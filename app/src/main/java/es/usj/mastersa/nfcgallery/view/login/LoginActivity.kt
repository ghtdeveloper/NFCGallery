package es.usj.mastersa.nfcgallery.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityLoginBinding
import es.usj.mastersa.nfcgallery.domain.User
import es.usj.mastersa.nfcgallery.presenter.FirebasePresenter
import es.usj.mastersa.nfcgallery.presenter.LoginActivityPresenter
import es.usj.mastersa.nfcgallery.util.DateNow
import es.usj.mastersa.nfcgallery.view.home.HomeActivity

class LoginActivity : AppCompatActivity(),ContractInterface.ViewLoginActivity
{

    //Object Presenter
    private var presenter: LoginActivityPresenter? = null
    private var presenterFirebase: FirebasePresenter? = null
    //Constant
    private var RC_SIGN_IN: Int = 1
    //Binding
    private lateinit var binding: ActivityLoginBinding
    //Google Sign In
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    //Dialog
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Init Object
        presenter = LoginActivityPresenter(this)
        presenterFirebase = FirebasePresenter(this)
        auth = FirebaseAuth.getInstance()
        //Init Viws
        initView()
        //GSO
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.defaut_web_cliente_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
    }//End onCreate

    override fun onStart() {
        super.onStart()
        showDialogProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                 val account = task.getResult(ApiException::class.java)!!
                Log.d("Login","FirebaseAuthLogin"+ account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException)
            {
                Log.w("Login","Google Sign Failed")
            }
        }
    }

    override fun initView()
    {
        binding.btnSignIn.setOnClickListener { signInGoogle() }
    }//End initView

    override fun signInGoogle()
    {
        val signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent,RC_SIGN_IN)
    }

    override fun firebaseAuthWithGoogle(idToken: String)
    {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){task ->
            if(task.isSuccessful)
            {
                //Sign In Success, update UI
                val user = auth.currentUser
                if (user != null) {
                    updateUI(user)
                }
            }else
            {
                Log.w("Login", "Error with the credentials" +task.exception.toString())
                Toast.makeText(this,"The Sign Authentication is not available, " +
                        " please try later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updateUI(user: FirebaseUser)
    {
        val intent = Intent(this,HomeActivity::class.java)
        intent.putExtra("username",user.displayName)
        intent.putExtra("uuid",user.uid)
        val dateNow = DateNow()
        //Add user in the collection
       presenterFirebase!!.addUser(User(uuid = user.uid, displayName = user.displayName!!,
           email = user.email!!, lastAccesDate = dateNow.getCurrentDateTime()),user.uid)
        startActivity(intent)
    }

    override fun showDialogProgress()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_progress_bar,null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        Thread {
            try {
                Thread.sleep(2500)
                val currentUser: FirebaseUser? = auth.currentUser
                if (currentUser != null) {
                    Log.d("Login","There is a user connected")
                    updateUI(currentUser)
                } else {
                    Log.d("Login","There isn't a user connected")
                }
                dialog.dismiss()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
        dialog = dialogBuilder.create()
        dialog.show()
    }

}