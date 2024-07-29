package com.tutorial.priceholder

import android.os.Environment
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

fun backupFile(viewModel: StateViewModel){

    // OkHttpClient örneği oluştur
    val client = OkHttpClient()

    // Gönderilecek dosya
    val file = File(viewModel.dbFileName) // Yüklemek istediğiniz dosyanın yolu

    // Dosyayı içeren bir request body oluştur
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", file.name,
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        .build()

    // HTTP POST isteğini yapılandır
    val request = Request.Builder()
        .url("http://192.168.1.112:80/") // Dosyanın gönderileceği URL
        .post(requestBody)
        .build()

    // İsteği asenkron olarak gönder ve yanıtı işle
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("myapp","sunucuya ulaşılamadı", e)
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.i("myapp","Dosya Başarıyla Yüklendi")
            } else {
                Log.i("myapp","Dosya Yüklenemedi: ${response.code}")
            }
        }
    })
}