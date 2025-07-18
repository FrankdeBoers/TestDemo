package com.frank.newapplication.solid.single

import com.frank.newapplication.solid.single.inter.IDial
import com.frank.newapplication.solid.single.inter.IHangUp
import com.frank.newapplication.solid.single.inter.IReceiveMessage
import com.frank.newapplication.solid.single.inter.ISendMessage

/**
 * Phone类 - 单一职责原则演示
 * 通过依赖注入的方式组合四个独立的功能接口
 * 每个接口负责一个特定的职责，符合SRP原则
 */
class Phone(
    private val dialService: IDial,
    private val hangUpService: IHangUp,
    private val sendMessageService: ISendMessage,
    private val receiveMessageService: IReceiveMessage
) {

    /**
     * 拨打电话号码
     */
    fun dialPhoneNumber(phoneNumber: String) {
        dialService.dialNumber(phoneNumber)
    }

    /**
     * 挂断电话
     */
    fun hangUpPhoneNumber(phoneNumber: String) {
        hangUpService.hangUp(phoneNumber)
    }

    /**
     * 发送消息
     */
    fun sendMessage(message: String) {
        sendMessageService.sendMessage(message)
    }

    /**
     * 接收消息
     */
    fun receiveMessage(message: String) {
        receiveMessageService.receiveMessage(message)
    }
}