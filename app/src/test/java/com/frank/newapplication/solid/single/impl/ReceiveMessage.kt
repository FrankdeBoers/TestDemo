package com.frank.newapplication.solid.single.impl

import com.frank.newapplication.solid.single.inter.IReceiveMessage

class ReceiveMessage : IReceiveMessage {
    override fun receiveMessage(message: String) {
        println("Phone# receiveMessage message:$message")
    }
}