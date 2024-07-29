package com.tutorial.priceholder.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.reusables.CustomizedTextField

// Default ayarların tanımlandığı kısımdır. Taksi parası, yemek fiyatı vs.

@Composable
fun SettingsScreen(viewModel: StateViewModel) {

    val defaults = viewModel.defaults

    val mealPrice by defaults.defaultMealPrice.collectAsState()
    val mealPriceDict = remember { mutableStateMapOf("error" to "false", "msg" to "") }
    val taxiPrice by defaults.defaultTaxiPrice.collectAsState()
    val taxiPriceDict = remember { mutableStateMapOf("error" to "false", "msg" to "") }
    val numberForOmer by defaults.defaultNumberForOmer.collectAsState()
    val numberForOmerDict = remember { mutableStateMapOf("error" to "false", "msg" to "") }
    val numberForDiger by defaults.defaultNumberForDiger.collectAsState()
    val numberForDigerDict = remember { mutableStateMapOf("error" to "false", "msg" to "") }

    val numberControl = {
        dict: SnapshotStateMap<String, String>, number:String ->
        if (number.toIntOrNull() == null) {
            dict["msg"] = "Fiyatı Doğru Biçimde Giriniz"
            dict["error"] = "true"
            false
        } else {
            dict["msg"] = ""
            dict["error"] = "false"
            true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 180.dp, bottom = 180.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomizedTextField(
            value = mealPrice,
            onValueChange = {
                numberControl(mealPriceDict, it)
                defaults.updateDefaultMealPrice(it)
            },
            label = "Yemek Ücreti",
            supportingText = mealPriceDict["msg"] ?: "",
            leadingIcon = Icons.Outlined.MonetizationOn,
            trailingIcon = Icons.Outlined.Save,
            onTrailingIcon = { defaults.updateDefault("mealPrice",mealPrice) },
            isError = mealPriceDict["error"] != "false",
            justNumbers = true
        )

        CustomizedTextField(
            value = taxiPrice,
            onValueChange = {
                numberControl(taxiPriceDict, it)
                defaults.updateDefaultTaxiPrice(it)
            },
            label = "Taksi Ücreti",
            supportingText = taxiPriceDict["msg"] ?: "",
            leadingIcon = Icons.Outlined.LocalTaxi,
            trailingIcon = Icons.Outlined.Save,
            onTrailingIcon = { defaults.updateDefault("taxiPrice",taxiPrice) },
            isError = taxiPriceDict["error"] != "false",
            justNumbers = true
        )

        CustomizedTextField(
            value = numberForOmer,
            onValueChange = {
                numberControl(numberForOmerDict, it)
                defaults.updateDefaultNumberForOmer(it)
            },
            label = "Ömer Abi Sayı",
            supportingText = numberForOmerDict["msg"] ?: "",
            leadingIcon = Icons.Outlined.PersonOutline,
            trailingIcon = Icons.Outlined.Save,
            onTrailingIcon = { defaults.updateDefault("numberForOmer",numberForOmer) },
            isError = numberForOmerDict["error"] != "false",
            justNumbers = true
        )

        CustomizedTextField(
            value = numberForDiger,
            onValueChange = {
                numberControl(numberForDigerDict, it)
                defaults.updateDefaultNumberForDiger(it)
            },
            label = "Diğer Şantiye Sayı",
            supportingText = numberForDigerDict["msg"] ?: "",
            leadingIcon = Icons.Outlined.PersonOutline,
            trailingIcon = Icons.Outlined.Save,
            onTrailingIcon = { defaults.updateDefault("numberForDiger",numberForDiger) },
            isError = numberForDigerDict["error"] != "false",
            justNumbers = true
        )
    }
}