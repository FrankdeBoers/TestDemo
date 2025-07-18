package com.frank.newapplication.solid.single.impl

import com.frank.newapplication.solid.single.inter.IDial

class Dial : IDial {
    override fun dialNumber(phoneNumber: String) {
        println("Phone# dialNumber phoneNumber:$phoneNumber")
    }
}