package com.frank.newapplication.solid.single.impl

import com.frank.newapplication.solid.single.inter.ISendMessage

class SendMessage : ISendMessage {
    override fun sendMessage(message: String) {
        println("Phone# sendMessage message:$message")
    }
}