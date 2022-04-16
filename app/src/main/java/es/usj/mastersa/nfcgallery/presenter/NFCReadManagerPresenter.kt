package es.usj.mastersa.nfcgallery.presenter

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import es.usj.mastersa.nfcgallery.contract.ContractInterface
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.experimental.and


/**
 ****Creado por: Edison Martinez,
 ****Fecha:03,Sunday,2022,
 ****Hora: 11:28 AM.
 **/
class NFCReadManagerPresenter(_viewHomeActivity: ContractInterface.ViewHomeActivity, context: Context) :
    ContractInterface.PresenterNFCManager,Activity()
{

    //Views
    private var viewHomeActivity: ContractInterface.ViewHomeActivity = _viewHomeActivity
    //Objects
    private var nfcAdapter: NfcAdapter? = null
    private val MIME_TEXT_PLAIN = "text/plain"
    private var DATA_GET: String = ""


    //Init
    init {
        viewHomeActivity.initView()
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        isAdaptaerAvailable(context)
        isAdapterEnable(context)
    }

    override fun isAdaptaerAvailable(context: Context): Boolean
    {
        return !nfcAdapter!!.equals(null)
    }

    override fun isAdapterEnable(context: Context): Boolean
    {
        return nfcAdapter!!.isEnabled
    }


    override suspend fun handleIntent(intent: Intent, activity: Activity)
    {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val type = intent.type
            if (MIME_TEXT_PLAIN == type)
            {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    doInBackground(tag)
                }
            } else {
                Log.d(this.javaClass.name, "Wrong mime type: $type")
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val techList = tag!!.techList
            val searchedTech = Ndef::class.java.name
            for (tech in techList) {
                if (searchedTech == tech) {
                    doInBackground(tag)
                    break
                }
            }
        }
    }

    override suspend fun doInBackground(tag: Tag): String
    {
       Log.d(this.javaClass.name,"Reading NFC TAGS")
        val ndef : Ndef = Ndef.get(tag)
        val ndefMessage: NdefMessage = ndef.cachedNdefMessage
        val records: Array<out NdefRecord>? = ndefMessage.records
        if (records != null) {
            for(ndfRecord in records)
            {
                if(ndfRecord.tnf == NdefRecord.TNF_WELL_KNOWN &&
                    Arrays.equals(ndfRecord.type, NdefRecord.RTD_TEXT))
                {
                    try {
                        //Log.d("DATA READER",readText(record = ndfRecord))//Check
                        DATA_GET = readText(record = ndfRecord)
                        //getTextReader(readText(record = ndfRecord))
                        return readText(ndfRecord)
                    }catch (e: UnsupportedEncodingException)
                    {
                        Log.e(this.javaClass.name,"Unsupported Encoding")
                    }
                }
            }
        }
        return "data"
    }

    override fun readText(record: NdefRecord): String
    {
        val payload: ByteArray? = record.payload

        // Get the Text Encoding
        val textEncoding = Charset.forName("utf-8")

        // Get the Language Code
        val languageCodeLength = (payload!![0] and 51).toInt()

        // Get the Text
        return String(
            payload, languageCodeLength + 1, payload.size - languageCodeLength - 1,
            textEncoding)
    }

    override fun getTextReader(): String
    {
       return DATA_GET
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
            filters[0]!!.addDataType(MIME_TEXT_PLAIN)
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("Check your mime type.")
        }
        nfcAdapter!!.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun stopForegroundDiskPatch(activity: Activity)
    {
        nfcAdapter!!.disableForegroundDispatch(activity)
    }


}