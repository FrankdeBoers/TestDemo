package com.frank.newapplication

import org.junit.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.io.FileInputStream
import java.io.FileOutputStream

class AsmUnitTest {
    @Test
    fun testAsm() {
        // 原class文件，必须是java编译后的，kotlin编译后的不行
        val fis = FileInputStream("src/test/java/com/frank/newapplication/Test.class")
        val fos = FileOutputStream("src/test/java/com/frank/newapplication/TestResult.class")

        val classReader = ClassReader(fis)

        // 自动计算 栈帧、局部变量表大小
        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        classReader.accept(CustomVisitor(Opcodes.ASM7, classWriter), ClassReader.EXPAND_FRAMES)
        val bytes = classWriter.toByteArray()

        fos.write(bytes)
        fos.close()
        fis.close()
    }

}


class CustomVisitor : ClassVisitor {
    constructor(api: Int) : super(api)

    constructor(api: Int, classVisitor: ClassVisitor) : super(api, classVisitor)

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        println("Frank# visitMethod name:$name")
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        // 跳过构造方法和静态块
        if (mv != null && "<init>" != name && "<clinit>" != name) {
            return object : AdviceAdapter(ASM9, mv, access, name, descriptor) {
                override fun onMethodEnter() {
                    println("Frank# onMethodEnter name:$name")
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    mv.visitLdcInsn("ASM enter method:$name")
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
                }
            }
        }
        return mv
    }
}