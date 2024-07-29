package com.tutorial.priceholder

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.priceholder.database.DataBaseObject
import com.tutorial.priceholder.database.DataBaseProcess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StateViewModel(val context: Context):ViewModel() {
    // Sc = Screen, TF = TextField, ST = State
    val defaults:Defaults = Defaults()
    val enteringDataStates = EnteringDataStates()
    val dbProcess:DataBaseProcess = DataBaseProcess(this)

    var dbFileName:String = ""
    
    init {
        defaults.setDefaults()
    }

    private val _enteringDataST = MutableStateFlow(false)
    val enteringDataST: StateFlow<Boolean> = _enteringDataST
    fun updateEnteringDataST(value:Boolean){
        _enteringDataST.value = value
    }

    var dbList = mutableStateListOf<DataBaseObject>()

    fun addItem(item:DataBaseObject) = dbList.add(0,item)
    fun removeItem(item: DataBaseObject) = dbList.remove(item)
    fun refreshList() = viewModelScope.launch {
        dbList.clear()
        dbProcess.getItems().forEach { dbList.add(it) }
        dbList.reverse()
    }

    inner class EnteringDataStates{

        private val _forWho = MutableStateFlow("Ömer Abi")
        val forWho: StateFlow<String> = _forWho
        fun updateForWho(value:String){
            _forWho.value = value
        }

        private val _numberOfPortion = MutableStateFlow(defaults.getDefaults()["numberForOmer"].toString())
        val numberOfPortion: StateFlow<String> = _numberOfPortion
        fun updateNumberOfPortion(value:String){
            _numberOfPortion.value = value
        }

        private val _priceForEachPortion = MutableStateFlow(defaults.getDefaults()["mealPrice"].toString())
        val priceForEachPortion: StateFlow<String> = _priceForEachPortion
        fun updatePriceForEachPortion(value:String){
            _priceForEachPortion.value = value
        }

        private val _taxiPrice = MutableStateFlow(defaults.getDefaults()["taxiPrice"].toString())
        val taxiPrice: StateFlow<String> = _taxiPrice
        fun updateTaxiPrice(value:String){
            _taxiPrice.value = value
        }

        private val _mealOfDay = MutableStateFlow("")
        val mealOfDay: StateFlow<String> = _mealOfDay
        fun updateMealOfDay(value:String){
            _mealOfDay.value = value
        }

        private val _noteForThatDay = MutableStateFlow("")
        val noteForThatDay: StateFlow<String> = _noteForThatDay
        fun updateNoteForThatDay(value:String){
            _noteForThatDay.value = value
        }

        private val _dateOfThatDay = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        val dateOfThatDay: StateFlow<String> = _dateOfThatDay
        fun updateDateOfThatDay(value:String){
            _dateOfThatDay.value = value
        }

        fun turnIntoDefault(){
            _forWho.value = "Ömer Abi"
            _numberOfPortion.value = defaults.getDefaults()["numberForOmer"].toString()
            _priceForEachPortion.value = defaults.getDefaults()["mealPrice"].toString()
            _taxiPrice.value = defaults.getDefaults()["taxiPrice"].toString()
            _mealOfDay.value = ""
            _noteForThatDay.value = ""
            _dateOfThatDay.value = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }

    inner class Defaults{

        private val sharedPreferences = context.getSharedPreferences("defaults", Context.MODE_PRIVATE)

        private val _defaultTaxiPrice = MutableStateFlow("0")
        val defaultTaxiPrice: StateFlow<String> = _defaultTaxiPrice
        fun updateDefaultTaxiPrice(value:String){
            _defaultTaxiPrice.value = value
        }

        private val _defaultMealPrice = MutableStateFlow("0")
        val defaultMealPrice: StateFlow<String> = _defaultMealPrice
        fun updateDefaultMealPrice(value:String){
            _defaultMealPrice.value = value
        }

        private val _defaultNumberForOmer = MutableStateFlow("0")
        val defaultNumberForOmer: StateFlow<String> = _defaultNumberForOmer
        fun updateDefaultNumberForOmer(value:String){
            _defaultNumberForOmer.value = value
        }

        private val _defaultNumberForDiger = MutableStateFlow("0")
        val defaultNumberForDiger: StateFlow<String> = _defaultNumberForDiger
        fun updateDefaultNumberForDiger(value:String){
            _defaultNumberForDiger.value = value
        }

        fun setDefaults(){ // Uygulama açılışında varsayılan değerler yerine yerleştirildi
            Log.i("myapp", _defaultTaxiPrice.toString())
            sharedPreferences.apply {
                _defaultTaxiPrice.value = getString("taxiPrice","0") ?: "0"
                _defaultMealPrice.value = getString("mealPrice","0") ?: "0"
                _defaultNumberForOmer.value = getString("numberForOmer", "0") ?: "0"
                _defaultNumberForDiger.value = getString("numberForDiger", "0") ?: "0"
            }
        }

        fun getDefaults():Map<String, String>{ // Edit penceresinde varsayılan değerler yerine yerleştirildi
            return sharedPreferences.let {// Bu şekilde kaydedilmeyen bir değer edit penceresinde görünmeyecek
                mapOf(
                    "taxiPrice" to it.getString("taxiPrice","0")!!,
                    "mealPrice" to it.getString("mealPrice","0")!!,
                    "numberForOmer" to it.getString("numberForOmer","0")!!,
                    "numberForDiger" to it.getString("numberForDiger","0")!!,
                )
            }
        }

        fun updateDefault(key:String,value:String){
            sharedPreferences.edit().apply {
                putString(key, value)
            }.apply()
        }
    }

    /*TODO: DEFAULT KISMI BİTTİ, SADECE KAYDEDİLEN VERİ İÇİN KAYDEDİLDİ POPUPI ÇIKMASINI SAĞLA*/
}