package com.frank.newapplication.solid.single

import org.junit.Before
import org.junit.Test

/**
 * Phone类单元测试 - 验证单一职责原则实现
 * 测试依赖注入、职责分离和接口隔离
 */
class PhoneTest {
    
    private lateinit var phone: Phone

    @Before
    fun setUp() {
        // 创建Phone实例，注入Mock依赖
        phone = PhoneFactory.createTestPhone()
    }

    @Test
    fun `测试拨号功能的单一职责`() {
        // Given - 准备测试数据
        val phoneNumber = "13800138000"
        // When - 执行拨号操作
        phone.dialPhoneNumber(phoneNumber)
    }

    @Test
    fun `测试挂断功能的单一职责`() {
        // Given
        val phoneNumber = "13800138000"
        // When
        phone.hangUpPhoneNumber(phoneNumber)
    }

    @Test
    fun `测试发送消息功能的单一职责`() {
        // Given
        val message = "Hello, SOLID!"
        // When
        phone.sendMessage(message)
    }

    @Test
    fun `测试接收消息功能的单一职责`() {
        // Given
        val message = "Received message"
        // When
        phone.receiveMessage(message)
    }

} 