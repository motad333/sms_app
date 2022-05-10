package com.shop.navac.smspannel2

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import com.shop.navac.smspannel2.retrofit_models.SMS
import com.shop.navac.smspannel2.services.smsPanelService
import com.shop.navac.smspannel2.tools.RetrofitMaker
import com.shop.navac.smspannel2.tools.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
internal const val ACTION_FOO = "com.shop.navac.smspannel2.action.FOO"
private const val ACTION_BAZ = "com.shop.navac.smspannel2.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.shop.navac.smspannel2.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.shop.navac.smspannel2.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.

 */
class SmsSenderService : IntentService("SmsSenderService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {

            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
            ACTION_BAZ -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String?, param2: String?) {
        println("hellow from action foo")
        while (!Settings.stop_sending) {

            println("try to get messages")
            val get_sms =
                RetrofitMaker.getInstance().create(smsPanelService::class.java).getLpopSMS()
            get_sms.enqueue(object : Callback<SMS?> {
                override fun onResponse(call: Call<SMS?>, response: Response<SMS?>) {
                    if (response.body() != null) {
                        println(response.body()!!.receiver)
                        println(response.body()!!.sender)
                        println("want to send sms to ")
                        sendSmsFun(response.body()!!.receiver, response.body()!!.text)
                    }
                    println(response.body())
                }

                override fun onFailure(call: Call<SMS?>, t: Throwable) {
                    println(t)
                    println("get sms failed")
//                    todo : send error to mainactivity errorTV
                }
            })
            Thread.sleep(5000)
        }
    }
    fun sendSmsFun(receiver: String, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applicationContext.getSystemService(SmsManager::class.java)
                .sendTextMessage(
                    receiver,
                    null,
                    text,
                    null, null
                )
        } else {
            SmsManager.getDefault().sendTextMessage(
                receiver,
                null,
                text,
                null, null
            )
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String?, param2: String?) {
        TODO("Handle action Baz")
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, SmsSenderService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, SmsSenderService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}