package com.example.chatbat


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val human = ChatUser(
            1,
            "Batman",
            BitmapFactory.decodeResource(resources,
                R.drawable.bats)
        )


        val agent = ChatUser(
            2,
            "Joker",
            BitmapFactory.decodeResource(resources,
                R.drawable.joker)
        )

        FuelManager.instance.baseHeaders = mapOf( "Authorization" to "Bearer $ACCESS_TOKEN" )
        FuelManager.instance.basePath =  "https://api.dialogflow.com/v1/"

        FuelManager.instance.baseParams = listOf(
            "v" to "20170712",
            "sessionId" to UUID.randomUUID(),
            "lang" to "en"
        )

        my_chat_view.setOnClickSendButtonListener(
            View.OnClickListener {
                my_chat_view.send(
                    Message.Builder()
                        .setUser(human)
                        .setText(my_chat_view.inputText)
                        .build()
                )

                Fuel.get("/query",
                    listOf("query" to my_chat_view.inputText))
                    .responseJson { _, _, result ->
                        val reply = result.get().obj().getJSONObject("result").getJSONObject("fulfillment").get("speech").toString()
                        Log.i("reply", reply)

                        my_chat_view.send(Message.Builder()
                            .setRight(true)
                            .setUser(agent)
                            .setText(reply)
                            .build()
                        )
                    }

            })
    }

    companion object {
        private const val ACCESS_TOKEN = "1c381462283e4af99c944e69ae043816"

    }

}

