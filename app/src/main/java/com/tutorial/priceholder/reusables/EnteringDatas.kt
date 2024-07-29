package com.tutorial.priceholder.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tutorial.priceholder.R
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.database.DataBaseObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StatefulEnteringDatas(viewModel: StateViewModel){

    val states = viewModel.enteringDataStates
    val dbProcess = viewModel.dbProcess

    val forWho by states.forWho.collectAsState()
    val numberOfPortion by states.numberOfPortion.collectAsState()
    val priceForEachPortion by states.priceForEachPortion.collectAsState()
    val taxiPrice by states.taxiPrice.collectAsState()
    val mealOfDay by states.mealOfDay.collectAsState()
    val noteForThatDay by states.noteForThatDay.collectAsState()
    val dateOfThatDay by states.dateOfThatDay.collectAsState()

    val onClick = {

        val dbObject = DataBaseObject(
            objectID = System.currentTimeMillis(),
            forWho = forWho,
            numberOfPortion = numberOfPortion,
            priceForEachPortion = priceForEachPortion,
            taxiPrice = taxiPrice,
            mealOfDay = mealOfDay,
            noteForThatDay = noteForThatDay,
            dateOfThatDay = dateOfThatDay
        )

        CoroutineScope(Dispatchers.IO).launch { dbProcess.addItem(dbObject) }

        Unit
    }

    StatelessEnteringDatas(
        viewModel = viewModel,
        forWho, { states.updateForWho(it) },
        numberOfPortion, { states.updateNumberOfPortion(it) },
        priceForEachPortion, { states.updatePriceForEachPortion(it) },
        taxiPrice, { states.updateTaxiPrice(it) },
        mealOfDay, { states.updateMealOfDay(it) },
        noteForThatDay, { states.updateNoteForThatDay(it) },
        dateOfThatDay, { states.updateDateOfThatDay(it) },
        btnOnClick = onClick
    )
}

@Composable
fun StatelessEnteringDatas(
    viewModel: StateViewModel,
    forWho:String, onForWho:(String) -> Unit,
    numberOfPortion:String, onNumberOfPortion:(String) -> Unit,
    priceForEachPortion:String, onPriceForEachPortion:(String) -> Unit,
    taxiPrice:String, onTaxiPrice:(String) -> Unit,
    mealOfDay:String, onMealOfDay:(String) -> Unit,
    noteForThatDay:String, onNoteForThatDay:(String) -> Unit,
    dateOfThatDay:String, onDateOfThatDay:(String) -> Unit,
    btnOnClick:() -> Unit
){

    val defaults = viewModel.defaults.getDefaults()
    
    val onDismiss = { viewModel.updateEnteringDataST(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Card(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(top = 16.dp, bottom = 32.dp, start = 32.dp, end = 32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                Row(                    
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "close screen")
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            onForWho("Ömer Abi")
                            onNumberOfPortion(defaults["numberForOmer"].toString()
                            )
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = forWho == "Ömer Abi",
                            onClick = {
                                onForWho("Ömer Abi")
                                onNumberOfPortion(defaults["numberForOmer"].toString())
                            })
                        Text(
                            text = "Ömer Abi     ",
                            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))
                        )
                    }

                    Row(
                        modifier = Modifier.clickable {
                            onForWho("Diğer Şantiye")
                            onNumberOfPortion(defaults["numberForDiger"].toString())
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Diğer Şantiye",
                            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))
                        )
                        RadioButton(
                            selected = forWho == "Diğer Şantiye",
                            onClick = {
                                onForWho("Diğer Şantiye")
                                onNumberOfPortion(defaults["numberForDiger"].toString())
                            })
                    }
                }

                CustomizedTextField(
                    value = numberOfPortion,
                    label = "Porsiyon Sayısı",
                    onValueChange = onNumberOfPortion,
                    justNumbers = true
                )

                CustomizedTextField(
                    value = priceForEachPortion,
                    label = "Porsiyon Fiyatı",
                    onValueChange = onPriceForEachPortion,
                    justNumbers = true
                )

                CustomizedTextField(
                    value = if (forWho == "Ömer Abi") taxiPrice else "0",
                    label = "Taksi Ücreti",
                    onValueChange = onTaxiPrice,
                    enabled = forWho == "Ömer Abi",
                    justNumbers = true
                )

                CustomizedTextField(
                    value = mealOfDay,
                    label = "Günün Yemeği",
                    onValueChange = onMealOfDay
                )

                CustomizedTextField(
                    value = noteForThatDay,
                    label = "Bugün için bir not",
                    onValueChange = onNoteForThatDay
                )

                CustomizedTextField(
                    value = dateOfThatDay,
                    label = "Tarih",
                    onValueChange = onDateOfThatDay
                )

                Button(
                    onClick = { 
                        btnOnClick() 
                        onDismiss()
                    },
                    modifier = Modifier.width(180.dp)
                ) {
                    Text(
                        text = "Ekle",
                        fontFamily = FontFamily(Font(R.font.opensans_semicondensed_bold))
                    )
                }
            }
        }
    }
}