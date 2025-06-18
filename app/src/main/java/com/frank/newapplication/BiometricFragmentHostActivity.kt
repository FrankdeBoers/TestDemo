package com.frank.newapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.frank.newapplication.fragment.BiometricComposeFragment

class BiometricFragmentHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, BiometricComposeFragment(), "bio_compose")
                .commit()
        }
    }
} 