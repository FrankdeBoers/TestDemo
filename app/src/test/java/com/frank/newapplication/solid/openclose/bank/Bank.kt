package com.frank.newapplication.solid.openclose.bank

import org.junit.Test

// 面向接口抽象
class Bank {
    @Test
    fun testInterface() {
        BankStaff().handleProcess(bankClient = BankClient().apply {
            bankType = "存款"
        })
    }
}

class BankClient() {
    // 业务类型
    public var bankType: String = ""
}

class BankStaff() {
    private val bankProcess = BankProcess(store = StoreMoney(), take = TakeMoneyClass())

    fun handleProcess(bankClient: BankClient) {
        // 调研银行业务系统处理业务请求

        when (bankClient.bankType) {
            "存款" -> {
                bankProcess.storeMoney()
            }

            "取款" -> {
                bankProcess.takeMoney()
            }

        }

    }
}

class BankProcess(val store: IStore, val take: ITake) {

    fun storeMoney() {
        store.storeMoney()
    }

    fun takeMoney() {
        take.takeMoney()
    }
}

interface IStore {
    fun storeMoney()
}

interface ITake {
    fun takeMoney()
}

class StoreMoney : IStore {
    override fun storeMoney() {
        println("Bank# storeMoney")
    }
}

class TakeMoneyClass : ITake {
    override fun takeMoney() {
        println("Bank# takeMoney")
    }
}