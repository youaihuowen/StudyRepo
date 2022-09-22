package com.example.wu.net

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpApi : HttpApi {
    companion object{
        private const val Tag = "OkHttpApi"
    }
    private var baseUrl ="http://api.qingyunke.com/"
    //OkHttpClient
    private val mClient:OkHttpClient=OkHttpClient.Builder()
        .callTimeout(10,TimeUnit.SECONDS) //完整请求超时时长，从发起到接收返回数据，默认0，不限定
        .connectTimeout(10,TimeUnit.SECONDS) //与服务器建立连接的时长，默认10s
        .readTimeout(10,TimeUnit.SECONDS) //读取服务器返回数据时长
        .writeTimeout(10,TimeUnit.SECONDS) //向服务器写入数据的时长，默认10s
        .retryOnConnectionFailure(true) //重连
        .followRedirects(false) //重定向
        .build()

    override fun get(path: String, params: Map<String, Any>, callback: HttpCallback) {
        var url = "$baseUrl$path"
        LogUtils.d(url);
        val urlBuilder=url.toHttpUrl().newBuilder()
        params.forEach{
            urlBuilder.addEncodedQueryParameter(it.key,it.value.toString())
        }
        val request = Request.Builder()
            .get()
            .url(urlBuilder.build())
            .build()
        mClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.onFail(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }

        })
    }

    override fun post(path: String, body: Any, callback: HttpCallback) {
        val url = "$baseUrl$path"
        val request = Request.Builder()
            .post(Gson().toJson(body).toRequestBody())
            .url("https://testapi.cniao5.com/accounts/login")
            .build()
        mClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.onFail(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }

        })
    }
}