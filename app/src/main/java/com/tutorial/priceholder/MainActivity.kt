package com.tutorial.priceholder

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.tutorial.priceholder.navigator.CustomizedNavigationDrawer
import com.tutorial.priceholder.ui.theme.PriceHolderTheme

class MainActivity : ComponentActivity() {

    private val myViewModel by lazy {
        StateViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermission()

        setContent {
            PriceHolderTheme {
                CustomizedNavigationDrawer(viewModel = myViewModel)
            }
        }
    }

    override fun onStop() {
        backupFile(viewModel = myViewModel)
        super.onStop()
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
            ),
            0
        )
    }
}

