package es.usj.mastersa.nfcgallery.presenter

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import android.widget.Toast
import es.usj.mastersa.nfcgallery.R
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import es.usj.mastersa.nfcgallery.view.home.HomeActivity
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*


/**
 ****Creado por: Edison Martinez,
 ****Fecha:14,Thursday,2022,
 ****Hora: 6:11 PM.
 **/
class NFCWriteManagerPresenter(_viewHomeActivity: ContractInterface.ViewGalleryActivity,
                               context: Context): ContractInterface.PresenterNFCWriteManager,Activity()
{
    //Views
    private var viewHomeActivity: ContractInterface.ViewGalleryActivity = _viewHomeActivity
    //Objects
    private var nfcAdapter: NfcAdapter
    //Dialog
    private  lateinit var  alertDialog: AlertDialog


    init {
        viewHomeActivity.initView()
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        checkNfcEnabled(context)
    }

    override fun showDialogPutTagNFC(context: Context, activity: Activity)
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_put_nfc_tag,null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun checkNfcEnabled(context: Context)
    {
      val nfcEnabled = nfcAdapter.isEnabled
        if(!nfcEnabled)
        {
            Toast.makeText(context,"The NFC is not available",Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun handleIntent(intent: Intent, activity: Activity, context: Context,
                                      url:String)
    {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                writeTag(buildNdefMessage(url), tag, context,activity)
            }
        }
    }

    override fun setupForegroundDiskpatch(activity: Activity)
    {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext,
            0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("Check your mime type.")
        }
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun stopForegroundDiskPatch(activity: Activity)
    {
        nfcAdapter.disableForegroundDispatch(activity)
    }

    override fun showHomeActivity(context: Context, activity: Activity)
    {
        val myIntent = Intent(context, HomeActivity::class.java)
        activity.startActivity(myIntent)
    }


    override fun writeTag(message: NdefMessage, tag: Tag, context: Context, activity: Activity): Boolean
    {
       val size: Int = message.toByteArray().size

        try {
            val ndef: Ndef = Ndef.get(tag)
            if(ndef != null)
            {
                ndef.connect()

                if(!ndef.isWritable)
                {
                    Toast.makeText(context,
                        "Cannot write to this tag. This tag is read-only.", Toast.LENGTH_LONG)
                        .show()
                    return false
                }

                if(ndef.maxSize < size)
                {
                    Toast.makeText(
                        context, "Cannot write to this tag. Message size (" + size
                                + " bytes) exceeds this tag's capacity of "
                                + ndef.maxSize + " bytes.", Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                ndef.writeNdefMessage(message)
                Toast.makeText(
                    context, "Tag was successfully written", Toast.LENGTH_LONG
                ).show()
                alertDialog.dismiss()
                //Show Main Acitivity
                activity.finish()
                //showHomeActivity(context,activity)

            }
        }catch (e:Exception)
        {
            Toast.makeText(context, "Cannot write to this tag due to an Exception.",
                Toast.LENGTH_LONG)
                .show()
        }
        return false
    }

    override fun buildNdefMessage(url: String): NdefMessage
    {
        var lang = ByteArray(0)
        try {
            lang = Locale.getDefault().language.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        //Cut URL
        val urlShort = url.trim().substring(72)
        Log.d("URL_SHORT",urlShort)


        val dataBytes: ByteArray = urlShort.trim { it <= ' ' }
            .toByteArray(Charset.forName("UTF-8"))
        Log.d(this.javaClass.name,"Data To Write{$url}")

        val langsize = lang.size
        val textLenght = dataBytes.size

        val payload = ByteArrayOutputStream(1 + langsize + textLenght)
        payload.write((langsize and 0x1F))//OJO
        //payload.write((langsize and 0x1F) as Byte.toInt())
        payload.write(lang, 0, langsize)
        payload.write(dataBytes, 0, textLenght)

        val record = NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT,
            ByteArray(0),
            payload.toByteArray())

        val message = NdefMessage(arrayOf(record))

        return message
    }

}