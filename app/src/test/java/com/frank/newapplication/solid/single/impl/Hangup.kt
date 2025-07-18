package com.frank.newapplication.solid.single.impl

import com.frank.newapplication.solid.single.inter.IHangUp

class Hangup : IHangUp {
    override fun hangUp(phoneNumber: String) {
        println("Phone# hangUp phoneNumber:$phoneNumber")
    }
}