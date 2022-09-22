package com.example.wu.net

interface HttpCallback {
    /**
     * 网络请求成功回调
     * [data] 返回的回调结果数据
     */
    fun onSuccess(data:Any?)

    /**
     * 网络请求失败回调
     * [error] 返回的失败信息
     */
    fun onFail(error:Any?)
}