package com.frank.newapplication.solid.single

import com.frank.newapplication.solid.single.impl.Dial
import com.frank.newapplication.solid.single.impl.Hangup
import com.frank.newapplication.solid.single.impl.ReceiveMessage
import com.frank.newapplication.solid.single.impl.SendMessage

/**
 * Phone工厂类 - 简化Phone对象的创建
 * 提供默认实现
 * 体现了工厂模式和依赖注入的组合使用
 * */
object PhoneFactory {

    fun createTestPhone() : Phone {
        return Phone(
            dialService = Dial(),
            hangUpService = Hangup(),
            sendMessageService = SendMessage(),
            receiveMessageService = ReceiveMessage()
        )
    }
}