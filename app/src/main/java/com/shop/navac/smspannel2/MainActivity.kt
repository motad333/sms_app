package com.shop.navac.smspannel2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.shop.navac.smspannel2.tools.Settings
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext
//import io.sentry.Sentry

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start_send_button: Button = findViewById(R.id.startSendSmsButton)
        val stop_send_button: Button = findViewById(R.id.stopSendingButton)
        val get_phone_et: EditText = findViewById(R.id.getPhoneEditText)
        val sending_status_tv: TextView = findViewById(R.id.sendingStatusTV)
        val send_error_tv:TextView= findViewById(R.id.errorTV)


        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.SEND_SMS
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.SEND_SMS
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS), 1
                )
            }
        }

        sending_status_tv.text = (!Settings.stop_sending).toString()

        start_send_button.setOnClickListener {
            // Handler code here.
            Settings.stop_sending = false
//            todo : get phone text and use it in intent service
//            Settings.sender_address = get_phone_et.text
            sending_status_tv.text = (!Settings.stop_sending).toString()
            val send_sms_intent = Intent(this@MainActivity, SmsSenderService::class.java).apply {

            }
            send_sms_intent.action = ACTION_FOO
            startService(send_sms_intent);


        }
        stop_send_button.setOnClickListener {
            Settings.stop_sending = true
            sending_status_tv.text = (!Settings.stop_sending).toString()
        }
    }


}