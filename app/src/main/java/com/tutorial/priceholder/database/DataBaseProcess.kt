package com.tutorial.priceholder.database

import android.os.Build
import android.os.Environment
import android.util.Log
import com.tutorial.priceholder.StateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class DataBaseProcess(private val viewModel: StateViewModel):DataBase {

    override val filePath: String
        get() = createDBFile()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.refreshList()
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = runBlocking{  block() }

    override suspend fun addItem(item: DataBaseObject): Unit = dbQuery{
        val jsonDB = runBlocking{ getItems() }
        jsonDB.add(item)
        val jsonObject = Json.encodeToString(jsonDB)
        File(viewModel.dbFileName).writeText(jsonObject)
        viewModel.addItem(item)
    }

    override suspend fun getItems(): MutableList<DataBaseObject> = dbQuery {
        val fileContent = File(filePath).readText()
        Log.i("myapp", fileContent)
        Json.decodeFromString<MutableList<DataBaseObject>>(fileContent)
    }

    override suspend fun updateItem(newObject: DataBaseObject) = dbQuery {
        val jsonDB = runBlocking{ getItems() }
        val oldObject = jsonDB.first { it.objectID == newObject.objectID }
        jsonDB.add(jsonDB.indexOf(oldObject), newObject)
        jsonDB.remove(oldObject)
        val jsonObject = Json.encodeToString(jsonDB)
        File(viewModel.dbFileName).writeText(jsonObject)
    }

    override suspend fun removeItem(objectToBeRemoved: DataBaseObject) = dbQuery {
        val jsonDB = runBlocking{ getItems() }
        jsonDB.remove(objectToBeRemoved)
        val jsonObject = Json.encodeToString(jsonDB)
        File(viewModel.dbFileName).writeText(jsonObject)
        viewModel.removeItem(objectToBeRemoved)
        Unit
    }

    override fun createDBFile(): String {

        val pattern = Regex("[^a-zA-z]")

        var properDeviceName = pattern.replace(Build.DEVICE, "_").lowercase()

        if (properDeviceName.startsWith("_")) properDeviceName = properDeviceName.drop(1)

        val fileName = "${properDeviceName}_yemek.json"

        val myFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName)

        viewModel.dbFileName = myFile.absolutePath

        try {
            if (!myFile.exists()) {
                if (myFile.createNewFile()) {
                    Log.i("myapp","Dosya oluşturuldu: ${myFile.absolutePath}")
                    myFile.writeText("[]")
                } else {
                    Log.i("myapp","Dosya oluşturulamadı: ${myFile.absolutePath}")
                }
            } else {
                Log.i("myapp","Dosya zaten var: ${myFile.absolutePath}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return myFile.absolutePath
    }
}