package com.example.wu.net

/**
 * 网络请求统一接口
 */
interface HttpApi {
    /**
     * http get 异步请求封装
     */
    fun get(path: String, params: Map<String, Any>,callback:HttpCallback)
    /**
     * http get 同步请求封装
     */
    fun getSync(path: String, params: Map<String, Any>):Any?=Any()

    /**
     * http post 异步请求封装
     */
    fun post(path: String,body:Any,callback:HttpCallback)
    /**
     * http post 同步请求封装
     */
    fun post(path: String,body:Any):Any?=Any()
}