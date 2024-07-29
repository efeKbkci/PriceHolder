package com.tutorial.priceholder.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.tutorial.priceholder.R
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.database.DataBaseObject
import com.tutorial.priceholder.reusables.StatelessEnteringDatas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DataScreen(viewModel: StateViewModel){

    val dbList = viewModel.dbList
    val dbProcess = viewModel.dbProcess
    val states = viewModel.enteringDataStates

    val expandState = remember { List(dbList.size) {index: Int ->  index to false}.toMutableStateMap() }
    val enteringDataST by viewModel.enteringDataST.collectAsState()
    var currentID by remember { mutableLongStateOf(0) }

    val forWho by states.forWho.collectAsState()
    val numberOfPortion by states.numberOfPortion.collectAsState()
    val priceForEachPortion by states.priceForEachPortion.collectAsState()
    val taxiPrice by states.taxiPrice.collectAsState()
    val mealOfDay by states.mealOfDay.collectAsState()
    val noteForThatDay by states.noteForThatDay.collectAsState()
    val dateOfThatDay by states.dateOfThatDay.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 115.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            dbList.onEachIndexed { index, dataBaseObject ->
                DataItem(
                    dbObject = dataBaseObject,
                    isExpanded = expandState[index] ?: true,
                    onExpand = {expandState[index] = !(expandState[index] ?: true)},
                    onDelete = { CoroutineScope(Dispatchers.IO).launch{ viewModel.dbProcess.removeItem(dataBaseObject) } },
                    onClick = {
                        viewModel.updateEnteringDataST(true)
                        currentID = dataBaseObject.objectID
                        states.apply {
                            dataBaseObject.apply {
                                updateForWho(this.forWho)
                                updateTaxiPrice(this.taxiPrice)
                                updateMealOfDay(this.mealOfDay)
                                updateDateOfThatDay(this.dateOfThatDay)
                                updateNumberOfPortion(this.numberOfPortion)
                                updatePriceForEachPortion(this.priceForEachPortion)
                                updateNoteForThatDay(this.noteForThatDay)
                            }
                        }
                    }
                )
            }
        }
    }

    if (enteringDataST){
        StatelessEnteringDatas(
            viewModel = viewModel,
            forWho = forWho,
            onForWho = { states.updateForWho(it) },
            numberOfPortion = numberOfPortion,
            onNumberOfPortion = { states.updateNumberOfPortion(it) },
            priceForEachPortion = priceForEachPortion,
            onPriceForEachPortion = { states.updatePriceForEachPortion(it) },
            taxiPrice = taxiPrice,
            onTaxiPrice = { states.updateTaxiPrice(it) },
            mealOfDay = mealOfDay,
            onMealOfDay = { states.updateMealOfDay(it) },
            noteForThatDay = noteForThatDay,
            onNoteForThatDay = { states.updateNoteForThatDay(it) },
            dateOfThatDay = dateOfThatDay,
            onDateOfThatDay = { states.updateDateOfThatDay(it) }
        ) {
            CoroutineScope(Dispatchers.IO).launch{
                dbProcess.updateItem(
                    DataBaseObject(
                        objectID = currentID,
                        forWho = forWho,
                        numberOfPortion = numberOfPortion,
                        priceForEachPortion = priceForEachPortion,
                        taxiPrice = taxiPrice,
                        mealOfDay = mealOfDay,
                        noteForThatDay = noteForThatDay,
                        dateOfThatDay = dateOfThatDay
                    )
                )

                viewModel.refreshList()
            }
        }
    }
}

private fun LazyListScope.DataItem( // Extension Function
    dbObject: DataBaseObject ,
    isExpanded:Boolean,
    onExpand:() -> Unit,
    onDelete:()->Unit,
    onClick:()->Unit
){

    item {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(18f),
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(if (dbObject.forWho == "Ömer Abi") 0xffffbe76 else 0xffff7979)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${dbObject.dateOfThatDay} | ${dbObject.forWho}",
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium))
                )
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Filled.RemoveCircle, contentDescription = null)
                }
                IconButton(onClick = onExpand) {
                    Icon(imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = null)
                }
            }
        }
    }

    if (isExpanded){
        item {
            Text(
                text = "Kişi Sayısı: ${dbObject.numberOfPortion}\nYemek: ${dbObject.mealOfDay}\nNot: ${dbObject.noteForThatDay}",
                fontFamily = FontFamily(Font(R.font.opensans_semicondensed_bold)),
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp),
            )
        }
    }
}