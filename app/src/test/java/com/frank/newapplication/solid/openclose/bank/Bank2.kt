package com.frank.newapplication.solid.openclose.bank

import org.junit.Test

// 面向接口抽象
class Bank2 {
    @Test
    fun testClass() {
        val bankClient = StoreClient()
        val bankBusiness = bankClient.getBankBusiness()
        bankBusiness.process()
    }
}

interface IBankClient {
    fun getBankBusiness(): IBankBusiness
}

class StoreClient: IBankClient {
    override fun getBankBusiness(): IBankBusiness {
        return StoreBusiness()
    }
}

class TakeClient: IBankClient {
    override fun getBankBusiness(): IBankBusiness {
        return TakeBusiness()
    }
}


interface IBankBusiness {
    fun process()
}

class StoreBusiness() : IBankBusiness {
    override fun process() {
        println("Bank# storeMoney")
    }
}

class TakeBusiness() : IBankBusiness {
    override fun process() {
        println("Bank# takeMoney")
    }
}