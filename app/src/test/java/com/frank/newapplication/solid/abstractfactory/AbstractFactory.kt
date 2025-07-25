package com.frank.newapplication.solid.abstractfactory

import org.junit.Test

class AbstractFactoryTest {
    val factory by lazy {
        DeviceFactory()
    }

    @Test
    fun testFactory() {
        val device = factory.getDevice(DeviceBrand.Dell)
        val mouse = device.getMouse()
        mouse.showMouseBrand()
    }
}


// 抽象工厂，组合，获取鼠标、键盘
interface AbstractDeviceFactory {
    fun getMouse(): IMouse
    fun getKeyboard(): IKeyboard
}

enum class DeviceBrand {
    Dell,
    HP,
    Lenovo
}

class DeviceFactory {
    private val hashMap = hashMapOf<DeviceBrand, AbstractDeviceFactory>()

    fun getDevice(brand: DeviceBrand): AbstractDeviceFactory {
        val cacheFactory = hashMap[brand] ?: run {
            val new = createFactory(brand)
            hashMap[brand] = new
            new
        }
        return cacheFactory
    }

    private fun createFactory(brand: DeviceBrand): AbstractDeviceFactory {
        return when (brand) {
            DeviceBrand.HP -> HPFactory()
            DeviceBrand.Lenovo -> LenovoFactory()
            DeviceBrand.Dell -> DellFactory()
        }
    }
}

class DellFactory : AbstractDeviceFactory {
    override fun getMouse(): IMouse {
        return DellMouse()
    }

    override fun getKeyboard(): IKeyboard {
        return DellKeyboard()
    }
}

class HPFactory : AbstractDeviceFactory {
    override fun getMouse(): IMouse {
        return HPMouse()
    }

    override fun getKeyboard(): IKeyboard {
        return HPKeyboard()
    }
}


class LenovoFactory : AbstractDeviceFactory {
    override fun getMouse(): IMouse {
        return LenovoMouse()
    }

    override fun getKeyboard(): IKeyboard {
        return LenovoKeyboard()
    }

}




// 鼠标
interface IMouse {
    fun showMouseBrand()
}

// 键盘
interface IKeyboard {
    fun showKeyboardBrand()
}

class DellMouse : IMouse {
    override fun showMouseBrand() {
        println("AbstractFactory# Dell mouse")
    }
}

class DellKeyboard : IKeyboard {
    override fun showKeyboardBrand() {
        println("AbstractFactory# Dell keyboard")
    }
}

class HPMouse : IMouse {
    override fun showMouseBrand() {
        println("AbstractFactory# HP mouse")
    }
}

class HPKeyboard : IKeyboard {
    override fun showKeyboardBrand() {
        println("AbstractFactory# HP keyboard")
    }
}

class LenovoMouse : IMouse {
    override fun showMouseBrand() {
        println("AbstractFactory# Lenovo mouse")
    }
}

class LenovoKeyboard : IKeyboard {
    override fun showKeyboardBrand() {
        println("AbstractFactory# Lenovo keyboard")
    }
}