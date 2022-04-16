package es.usj.mastersa.nfcgallery.view.imagegallery

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.databinding.ActivityGalleryBinding
import es.usj.mastersa.nfcgallery.presenter.NFCWriteManagerPresenter
import es.usj.mastersa.nfcgallery.presenter.StorageCloudPresenter
import es.usj.mastersa.nfcgallery.util.DateNow
import java.io.ByteArrayOutputStream

class GalleryActivity : AppCompatActivity(), ContractInterface.ViewGalleryActivity
{
    //Presenter
    private var storageCloudPresenter: StorageCloudPresenter? = null
    private var nfcWriteManagerPresenter : NFCWriteManagerPresenter? = null
    //Binding
    private lateinit var binding: ActivityGalleryBinding
    //Variable
    private var REQUEST_IMAGE_CAPTURE = 0
    private var REQUEST_GALLERY_IMAGE = 1
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    //Objetcts
    private var alertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Init Objets
        storageCloudPresenter = StorageCloudPresenter(this)
        nfcWriteManagerPresenter = NFCWriteManagerPresenter(this,this)
        auth = FirebaseAuth.getInstance()
        //Views
        initView()
    }

    override fun initView()
    {
        binding.cardViewCamara.setOnClickListener { dispatchTakePicture() }
        binding.cardViewGallery.setOnClickListener { pickGalleryImage() }
        binding.btnWriteTAG.setOnClickListener { uploadImage() }
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(intent.getStringExtra("uriPict") == null)
        {
            Log.d(this.javaClass.name,"no edit")
        }else
        {
            Log.d("Edit From Home Act",intent.getStringExtra("uriPict").toString())
            Glide.with(this).load(intent.getStringExtra("uriPict")).
            into(binding.imgPicture)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        nfcWriteManagerPresenter!!.setupForegroundDiskpatch(this)
    }

    override fun onResume() {
        super.onResume()
        nfcWriteManagerPresenter!!.setupForegroundDiskpatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        lifecycleScope.launchWhenStarted {
            if (intent != null) {
               nfcWriteManagerPresenter!!.handleIntent(intent, this@GalleryActivity,
                    this@GalleryActivity,storageCloudPresenter!!.getUrlPict())
                /*nfcWriteManagerPresenter!!.handleIntent(intent, this@GalleryActivity,
                this@GalleryActivity,"test6")*/
            }
        }
    }

    override fun dispatchTakePicture()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    override fun pickGalleryImage()
    {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent,"Add From"),REQUEST_GALLERY_IMAGE)
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    override fun prepareImageToUpload()
    {
        //Get the data from ImageView as bytes
        binding.imgPicture.isDrawingCacheEnabled = true
        binding.imgPicture.buildDrawingCache()
        val bitmap = (binding.imgPicture.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()
        //Pass data to the presenter
        //storageCloudPresenter!!.uploadImage()

        if(auth.uid != null)
        {
            val dateNow = DateNow()
            val fileName ="IMG_"+dateNow.getCurrentYear()+dateNow.getCurrentMonth()+
                    dateNow.getCurrentDay()+"_"+dateNow.getCurrentHour()+
                    dateNow.getCurrentMinute()+dateNow.getCurrentSecond()
            storageCloudPresenter!!.uploadImage(auth.uid!!,fileName,data)
            //Show DialogProgress
            showDialogProgress()
        }else
        {
            Toast.makeText(this,"Please check your user",Toast.LENGTH_SHORT).show()
        }
    }

    override fun uploadImage() { prepareImageToUpload() }


    override fun showDialogProgress()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_progress_bar,null)
        dialogBuilder.setView(dialogView)
        //Cast View
        dialogView.findViewById<TextView?>(R.id.textViewVerificando)
            .text = "Uploading Image...."
        dialogBuilder.setCancelable(false)
        Thread {
            try {
                Thread.sleep(4500)
                //Log.d(this.javaClass.name,"URL PICT "+storageCloudPresenter!!.getUrlPict())
                Log.d(this.javaClass.name,"Show Dialog Put NFC")
                alertDialog!!.dismiss()
                this.runOnUiThread {
                    nfcWriteManagerPresenter!!.showDialogPutTagNFC(this,this)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
        alertDialog = dialogBuilder.create()
        alertDialog!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            binding.imgPicture.setImageBitmap(imageBitmap)
        }else if(requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && data!= null)
        {
            //val imageBitmap = data.extras!!.get("data") as Bitmap
            imageUri = data.data
            binding.imgPicture.setImageURI(imageUri)
        }
    }

}