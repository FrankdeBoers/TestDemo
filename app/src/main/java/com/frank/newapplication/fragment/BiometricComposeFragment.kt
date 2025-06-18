package com.frank.newapplication.fragment

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class BiometricComposeFragment : Fragment() {
    private val bioAlias = "biometric_key_alias"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    BiometricDemoScreen(
                        onCreateBioKey = { createBiometricKeyIfNecessary() },
                        onEncryptWithBio = { plain, onResult ->
                            showBiometricPromptForEncrypt(plain, onResult)
                        }
                    )
                }
            }
        }
    }

    // 生成生物识别密钥（只需一次）
    private fun createBiometricKeyIfNecessary() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
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

    // 获取Cipher并弹出指纹认证，认证通过后加密
    private fun showBiometricPromptForEncrypt(
        plain: String,
        onResult: (String) -> Unit
    ) {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = (keyStore.getEntry(bioAlias, null) as KeyStore.SecretKeyEntry).secretKey
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("指纹认证")
            .setSubtitle("请验证指纹以加密内容")
            .setNegativeButtonText("取消")
            .build()
        val biometricPrompt = BiometricPrompt(
            this, // 传入Fragment实例
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    val c = result.cryptoObject?.cipher!!
                    val encrypted = c.doFinal(plain.toByteArray(Charsets.UTF_8))
                    val iv = c.iv
                    onResult("加密成功: " +
                        Base64.encodeToString(encrypted, Base64.DEFAULT) +
                        "\nIV: " + Base64.encodeToString(iv, Base64.DEFAULT))
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onResult("认证失败: $errString")
                }
                override fun onAuthenticationFailed() {
                    onResult("认证失败")
                }
            }
        )
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
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