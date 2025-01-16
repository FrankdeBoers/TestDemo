package com.frank.newapplication.weakreference


class Apple(var name: String) {
    /**
     * 覆盖finalize，在回收的时候会执行。
     * @throws Throwable
     */
    @Throws(Throwable::class)
    protected fun finalize() {
        println("Apple： $name finalize。")
    }

    override fun toString(): String {
        return "Apple{" +
                "name='" + name + '\'' +
                '}' + ", hashCode:" + this.hashCode()
    }
}