package es.usj.mastersa.nfcgallery.view.home

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityHomeBinding
import es.usj.mastersa.nfcgallery.presenter.HomeActivityPresenter
import es.usj.mastersa.nfcgallery.presenter.NFCReadManagerPresenter
import es.usj.mastersa.nfcgallery.view.imagegallery.GalleryActivity
import es.usj.mastersa.nfcgallery.view.login.LoginActivity

class HomeActivity : AppCompatActivity(),ContractInterface.ViewHomeActivity
{
    //Presenter
    private var presenter: HomeActivityPresenter?= null
    private var presenterNfcReadManager : NFCReadManagerPresenter? = null
    private lateinit var rootView:View
    //Binding
    private lateinit var binding: ActivityHomeBinding
    //Dialog
    private lateinit var dialog: AlertDialog
    //Objetcts
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        rootView= binding.root
        setContentView(rootView)
        //Init Presenter
        presenter = HomeActivityPresenter(this,this)
        auth = FirebaseAuth.getInstance()
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        presenterNfcReadManager = NFCReadManagerPresenter(this,this)
        //Init Views
        initView()
    }//End onCreate

    override fun onStart() {
        super.onStart()
        displayWelcomeSnack()
    }

    override fun onResume() {
        super.onResume()
        displayAnimation()
        presenterNfcReadManager!!.setupForegroundDiskpatch(this)
    }

    override fun onPause() {
        super.onPause()
        presenterNfcReadManager!!.stopForegroundDiskPatch(this)
    }


    override  fun onNewIntent(intent: Intent?)
    {
        super.onNewIntent(intent)
        lifecycleScope.launchWhenStarted {
            if(intent!= null)
            {
                presenterNfcReadManager!!.handleIntent(intent,this@HomeActivity)
                //binding.tvMessageReading.text = presenterNfcManager!!.getTextReader()
                if(presenterNfcReadManager!!.getTextReader().startsWith("/o/root") ||
                    presenterNfcReadManager!!.getTextReader().startsWith("/o/root"))
                {
                    Log.d(this.javaClass.name,"The tag data is correct")
                    //Log.d("SHOW PICTURE","SHOW DIALOG IMAGE")
                    showDialogoImage(presenterNfcReadManager!!.getTextReader())
                }else
                {
                    Log.d(this.javaClass.name,"the tag data is not correct")
                    displayDialogNoDataTag()
                }
            }
        }
    }

    override fun initView() {
        binding.textSignOut.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle(R.string.text_title_sign_out)
            dialogBuilder.setPositiveButton(R.string.text_yes, DialogInterface.OnClickListener
            { _, _ ->
              //FirebaseAuth()
               auth.signOut()
                dialog.dismiss()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }).setNegativeButton(R.string.text_not, DialogInterface.OnClickListener {
                    _, _ ->
            })
            dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun showDialogoImage(url: String)
    {
        val dialoBuilder = AlertDialog.Builder(this,R.style.dialogoPersonalizado)
        val inflarte = this.layoutInflater
        val dialoView = inflarte.inflate(R.layout.dialog_show_image,null)
        dialoBuilder.setView(dialoView)
        dialoBuilder.setCancelable(false)
        //Cast Views
        val imageView:ImageView = dialoView.findViewById(R.id.imagenAdjuntoFullScreen)
        val imgBtnClose: ImageButton = dialoView.findViewById(R.id.btnCloseDialogo)
        val imgBtnEdit: ImageButton = dialoView.findViewById(R.id.btnEditImage)
        val imgBtnShare: ImageButton = dialoView.findViewById(R.id.btnShare)

        val urlBase ="https://firebasestorage.googleapis.com/v0/b/nfcgallery-1b10c.appspot.com"
        val urlFinal = urlBase.trim() + url.trim()
        Log.d("URL COMPLETE",urlFinal)

        //Listener
        imgBtnClose.setOnClickListener {
            dialog.dismiss()
        }
        imgBtnEdit.setOnClickListener {
            dialog.dismiss()
            val intentGallery = Intent(applicationContext,GalleryActivity::class.java)
            intentGallery.putExtra("uriPict",urlFinal)
            startActivity(intentGallery)
        }
        imgBtnShare.setOnClickListener {
            dialog.dismiss()
            val intentShare: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,urlFinal)
                type ="text/plain"
            }
            startActivity(Intent.createChooser(intentShare,"Send To"))
        }
        //Dowload from storageReference
        Glide.with(this).load(Uri.parse(urlFinal)).into(imageView)
        dialog = dialoBuilder.create()
        dialog.show()
    }

    override fun displayWelcomeSnack()
    {
        val intent = intent
        presenter!!.showSnackBar(rootView,intent.getStringExtra("username").toString())
    }

    override fun displayAnimation()
    {
       val animation:Animation =AnimationUtils.loadAnimation(this,R.anim.moveanimation)
        binding.imgIconPhone.startAnimation(animation)
    }

    override fun displayDialogNoDataTag()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_no_data_tag,null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        //Cast a las vistas
        dialogView.findViewById<TextView?>(R.id.textViewNot).
        setOnClickListener { dialog.dismiss()}
        dialogView.findViewById<TextView?>(R.id.textViewYes).setOnClickListener {
            //Toast.makeText(this,"Show Add Image", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext,GalleryActivity::class.java)
            intent.putExtra("action","write_new")
            startActivity(intent)
            dialog.dismiss()
        }
        dialog = dialogBuilder.create()
        dialog.show()
    }

}