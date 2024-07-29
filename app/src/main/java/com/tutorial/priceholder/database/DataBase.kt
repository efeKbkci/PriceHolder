package com.tutorial.priceholder.database

interface DataBase {

    val filePath:String // Veri tabanı dosya yolu

    suspend fun <T> dbQuery(block: () -> T):T // Veri tabanı sorguları (block) burada çalıştırılacak

    suspend fun addItem(item:DataBaseObject)

    suspend fun getItems():List<DataBaseObject> // Bütün veri tabanını döner

    suspend fun updateItem(newObject: DataBaseObject)

    suspend fun removeItem(objectToBeRemoved:DataBaseObject)

    fun createDBFile():String // Veri tabanı dosyasını external storage içerisinde oluşturur

}