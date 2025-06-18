package com.frank.newapplication

import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.R)
class KeyStoreDemoActivity : ComponentActivity() {
    private val alias = "demo_key_alias"
    private val transformation = "AES/CBC/PKCS7Padding"
    private val rsaAlias = "demo_rsa_alias"
    private val rsaTransformation = "RSA/ECB/PKCS1Padding"
    private lateinit var keyStore: KeyStore
    private var iv: ByteArray? = null
    private lateinit var rsaPublicKey: java.security.PublicKey
    private lateinit var rsaPrivateKey: java.security.PrivateKey
    private val bioAlias = "biometric_key_alias"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        createKeyIfNecessary()
        createRSAKeyIfNecessary()
        loadRSAKeyPair()
        setContent {
            MaterialTheme {
                BiometricDemoScreen(
                    onCreateBioKey = { createBiometricKeyIfNecessary() },
                    onEncryptWithBio = { plain, onResult ->
                    }
                )
            }
        }
    }

    // AES相关
    @RequiresApi(Build.VERSION_CODES.P)
    private fun createKeyIfNecessary() {
        if (!keyStore.containsAlias(alias)) {
            val keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            val builder = android.security.keystore.KeyGenParameterSpec.Builder(
                alias,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(true)
            // 优先尝试使用StrongBox
            try {
                builder.setIsStrongBoxBacked(true)
                keyGenerator.init(builder.build())
                keyGenerator.generateKey()
            } catch (e: Exception) {
                // StrongBox不可用时自动降级为TEE
                builder.setIsStrongBoxBacked(false)
                keyGenerator.init(builder.build())
                keyGenerator.generateKey()
            }
        }
    }
    private fun getSecretKey(): SecretKey {
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
    private fun encrypt(plainText: String): ByteArray {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        iv = cipher.iv
        return cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
    }
    private fun decrypt(cipherText: ByteArray, iv: ByteArray): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(iv))
        val decrypted = cipher.doFinal(cipherText)
        return String(decrypted, Charsets.UTF_8)
    }

    // RSA相关
    @RequiresApi(Build.VERSION_CODES.P)
    private fun createRSAKeyIfNecessary() {
        if (!keyStore.containsAlias(rsaAlias)) {
            val kpg = java.security.KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
            val builder = android.security.keystore.KeyGenParameterSpec.Builder(
                rsaAlias,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setDigests(android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            // 优先尝试使用StrongBox
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    builder.setIsStrongBoxBacked(true)
                }
                kpg.initialize(builder.build())
                kpg.generateKeyPair()
            } catch (e: Exception) {
                // StrongBox不可用时自动降级为TEE
                builder.setIsStrongBoxBacked(false)
                kpg.initialize(builder.build())
                kpg.generateKeyPair()
            }
        }
    }
    private fun loadRSAKeyPair() {
        val entry = keyStore.getEntry(rsaAlias, null) as KeyStore.PrivateKeyEntry
        rsaPrivateKey = entry.privateKey
        rsaPublicKey = entry.certificate.publicKey
    }
    /**
     * 使用RSA公钥加密明文数据
     *
     * 原理说明：
     * - 非对称加密算法（如RSA）有一对密钥：公钥和私钥。
     * - 公钥通常用于加密数据，任何人都可以获取公钥进行加密。
     * - 只有持有私钥的人才能解密用公钥加密的数据，保证了数据的机密性。
     *
     * 应用场景：
     * - 保护数据传输安全（如HTTPS、敏感信息传递等）。
     * - 适合加密较短的数据（如对称密钥、摘要等），大数据量建议用对称加密。
     *
     * @param plainText 需要加密的明文字符串
     * @return 加密后的字节数组（密文）
     */
    private fun rsaEncrypt(plainText: String): ByteArray {
        // 获取Cipher实例，指定加密算法和填充方式
        val cipher = Cipher.getInstance(rsaTransformation)
        // 用公钥初始化Cipher，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
        // 执行加密操作，返回密文
        return cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
    }
    /**
     * 使用RSA私钥解密密文数据
     *
     * 原理说明：
     * - 只有持有私钥的人才能解密用公钥加密的数据。
     * - 这样即使密文被截获，没有私钥也无法还原明文。
     *
     * 应用场景：
     * - 与rsaEncrypt配合，用于安全的数据传输。
     *
     * 注意事项：
     * - 密文必须是用对应公钥加密得到的。
     * - 解密失败通常是密钥不匹配或数据被篡改。
     *
     * @param cipherText 需要解密的密文字节数组
     * @return 解密后的明文字符串
     */
    private fun rsaDecrypt(cipherText: ByteArray): String {
        // 获取Cipher实例，指定加密算法和填充方式
        val cipher = Cipher.getInstance(rsaTransformation)
        // 用私钥初始化Cipher，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey)
        // 执行解密操作，返回明文
        val decrypted = cipher.doFinal(cipherText)
        return String(decrypted, Charsets.UTF_8)
    }

    /**
     * 生成一个需要指纹（生物识别）认证才能访问的对称密钥
     * 该密钥只能在用户通过生物识别（如指纹、人脸）认证后才能被使用，适合高安全场景。
     * 需要Android 9(P)及以上。
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun createBiometricKeyIfNecessary() {
        if (!keyStore.containsAlias(bioAlias)) {
            val keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            val builder = android.security.keystore.KeyGenParameterSpec.Builder(
                bioAlias,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(true)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationValidityDurationSeconds(0)
                .setUserAuthenticationParameters(0, android.security.keystore.KeyProperties.AUTH_BIOMETRIC_STRONG)
            try {
                builder.setIsStrongBoxBacked(true)
                keyGenerator.init(builder.build())
                keyGenerator.generateKey()
            } catch (e: Exception) {
                builder.setIsStrongBoxBacked(false)
                keyGenerator.init(builder.build())
                keyGenerator.generateKey()
            }
        }
    }


}

@Composable
fun BiometricDemoScreen(
    onCreateBioKey: () -> Unit,
    onEncryptWithBio: (String, (String) -> Unit) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(Modifier.padding(24.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("输入要加密的内容") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            onCreateBioKey()
            result = "生物识别密钥已生成（如已存在则无操作）"
        }) {
            Text("生成生物识别密钥")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (input.isNotEmpty()) {
                onEncryptWithBio(input) { msg -> result = msg }
            } else {
                result = "请输入内容"
            }
        }) {
            Text("指纹认证并加密")
        }
        Spacer(Modifier.height(24.dp))
        Text(result)
    }
}