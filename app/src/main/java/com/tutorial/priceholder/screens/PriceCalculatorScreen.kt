package com.tutorial.priceholder.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorial.priceholder.R
import com.tutorial.priceholder.StateViewModel
import com.tutorial.priceholder.reusables.CustomizedTextField
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalculatingScreen(viewModel: StateViewModel){

    var startDate by rememberSaveable { mutableStateOf("") }
    val startDateDict = remember { mutableStateMapOf("error" to "true", "msg" to "Tarihi 'gg.aa.yyyy' şeklinde giriniz") }
    var endDate by rememberSaveable { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) }
    val endDateDict = remember{ mutableStateMapOf("error" to "false", "msg" to "") }
    var result by remember {
        mutableStateOf("")
    }
    val noteList = remember{ mutableStateListOf<String>() }

    val dateControl = {
        date:String ->
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        dateFormat.isLenient = false ///lenient yumuşak, hoşgörülü anlamına gelir, geçersiz tarihler için hata fırlatır.
        try {
            dateFormat.parse(date)
            true
        } catch (e: ParseException) {
            false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 120.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Önce fiyatın hesaplanmaya başlanacağı günü, daha sonra fiyatın hesaplanacağı son günü girin." +
                "\nİlk gün ve son gün hesaplamaya dahil edilir\nTarihleri gg.aa.yyyy şeklinde giriniz",
            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_lightitalic)),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
        
        CustomizedTextField(
            value = startDate,
            onValueChange = {
                if (dateControl(it)){
                    startDateDict["error"] = "false"
                    startDateDict["msg"] = ""
                } else{
                    startDateDict["error"] = "true"
                    startDateDict["msg"] = "Tarihi 'gg.aa.yyyy' şeklinde giriniz"
                }
                startDate = it
            },
            label = "Başlangıç Tarihi",
            supportingText = startDateDict["msg"]!!,
            isError = startDateDict["error"] != "false",
            justNumbers = true
        )

        CustomizedTextField(
            value = endDate,
            onValueChange = {
                if (dateControl(it)){
                    endDateDict["error"] = "false"
                    endDateDict["msg"] = ""
                } else{
                    endDateDict["error"] = "true"
                    endDateDict["msg"] = "Tarihi 'gg.aa.yyyy' şeklinde giriniz"
                }
                endDate = it
            },
            label = "Bitiş Tarihi",
            supportingText = endDateDict["msg"]!!,
            isError = endDateDict["error"] != "false",
            justNumbers = true
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                if (startDateDict["error"] == "false" && endDateDict["error"] == "false"){
                    val resultDict = runBlocking{
                        noteList.clear()
                        getNotes(startDate, endDate, viewModel).forEach { noteList.add("${it.dateOfThatDay} -> ${it.noteForThatDay}") }
                        calculatePrice(startDate, endDate, viewModel)
                    }
                    result = "Ömer Abi Kişi Sayısı: ${resultDict["omerToplamKisi"]}\n" +
                    "Ömer Abi taksi hariç toplam Ücret: ${resultDict["omerToplamFiyat"]}\n" +
                    "Diğer Şantiye Kişi Sayısı: ${resultDict["digerToplamKisi"]}\n" +
                    "Diğer Şantiye Toplam Ücret: ${resultDict["digerToplamFiyat"]}\n" +
                    "Taksi Ücreti: ${resultDict["taksiToplam"]}\n\n" +
                    "Ömer Abi taksi ile birlikte toplam ücret: ${resultDict["taksiToplam"]!! + resultDict["omerToplamFiyat"]!!}"
                }
            },
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                text = "Hesapla",
                fontFamily = FontFamily(Font(R.font.opensans_semicondensed_bold))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        Divider()

        Text(
            text = result,
            fontFamily = FontFamily(Font(R.font.opensans_semicondensed_semibold)),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
        )

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(noteList){
                Text(text = it, fontFamily = FontFamily(Font(R.font.opensans_semicondensed_medium)))
            }
        }
    }
}

suspend fun getAvaibleObjects(start:String, end:String ,viewModel: StateViewModel) = runBlocking{

    val dbProcess = viewModel.dbProcess

    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val startAsMilis = dateFormat.parse(start)!!.time
    val endAsMilis = dateFormat.parse(end)!!.time

    dbProcess.getItems().filter {
            val timeAsMilis = dateFormat.parse(it.dateOfThatDay)!!.time
            timeAsMilis in startAsMilis..endAsMilis
    }
}

suspend fun calculatePrice(start: String, end: String, viewModel: StateViewModel) = runBlocking{

    var omerToplamKisi = 0
    var omerToplamFiyat = 0
    var digerToplamKisi = 0
    var digerToplamFiyat = 0
    var taksiToplam = 0

    getAvaibleObjects(start, end, viewModel).forEach {

        if (it.forWho == "Ömer Abi"){
            omerToplamKisi += it.numberOfPortion.toInt()
            omerToplamFiyat += it.numberOfPortion.toInt() * it.priceForEachPortion.toInt()
            taksiToplam += it.taxiPrice.toInt()
        } else {
            digerToplamKisi += it.numberOfPortion.toInt()
            digerToplamFiyat += it.numberOfPortion.toInt() * it.priceForEachPortion.toInt()
        }
    }

    mapOf(
        "omerToplamKisi" to omerToplamKisi,
        "omerToplamFiyat" to omerToplamFiyat,
        "digerToplamKisi" to digerToplamKisi,
        "digerToplamFiyat" to digerToplamFiyat,
        "taksiToplam" to taksiToplam
    )
}

suspend fun getNotes(start: String, end: String, viewModel: StateViewModel) = runBlocking {

    getAvaibleObjects(start, end, viewModel).filter { it.noteForThatDay.isNotEmpty() }
}