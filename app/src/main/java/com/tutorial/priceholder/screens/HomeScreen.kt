package com.tutorial.priceholder.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.tutorial.priceholder.R
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.reusables.StatefulEnteringDatas

// Fiyat, kişi, tarih, yemek ve not bilgilerinin alındığını, fiyatın hesaplandığı ve veri tabanına eklendiği sayfa

@Composable
fun HomeScreen(viewModel: StateViewModel) {

    val enteringDialogST by viewModel.enteringDataST.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = { viewModel.updateEnteringDataST(true) },
            modifier = Modifier
                .width(200.dp)
                .height(60.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF16A085),
                contentColor = Color(0XFFEEEEEE)
            )
        ) {
            Text(text = "Yeni Fiş Ekle", fontFamily = FontFamily(Font(R.font.opensans_semicondensed_bold)))
        }
    }

    if (enteringDialogST) {
        viewModel.enteringDataStates.turnIntoDefault()
        StatefulEnteringDatas(viewModel = viewModel)
    }

}