package com.example.wu.studyproject.net

import okhttp3.*
import retrofit2.http.Tag
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日志拦截器
 */
class KtHttpLogInterceptor(block: (KtHttpLogInterceptor.() -> Unit)? = null) : Interceptor {
    private var logLevel: LogLevel = LogLevel.NONE
    private var colorLevel: ColorLevel = ColorLevel.DEBUG //默认是debug级别的logcat
    private var logTag: String = TAG //日志logcat的TAG

    init {
        block?.invoke(this)
    }
    /**
     * 设置 logLevel
     */
    fun logLevel(level: LogLevel): KtHttpLogInterceptor {
        logLevel = level
        return this
    }
    /**
     * 设置 colorLevel
     */
    fun colorLevel(level: ColorLevel): KtHttpLogInterceptor {
        colorLevel = level
        return this
    }
    /**
     * 设置 log 的 tag
     */
    fun logTag(tag:String):KtHttpLogInterceptor{
        logTag=tag
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        //请求
        val request = chain.request()
        //响应
        return kotlin.runCatching { chain.proceed(request) }
            .onFailure {
                it.printStackTrace()

            }.onSuccess {
                if (logLevel == LogLevel.NONE) {
                    return it
                }

            }.getOrThrow()
    }

    /**
     * 记录日志请求
     */
    fun logRequest(request: Request,connection: Connection?){
        val sb = StringBuilder()
        sb.appendLine("\r\n")
        sb.appendLine("->")
        when(logLevel){
            LogLevel.NONE->{
                //do nothing
            }
            LogLevel.BASIC->{
                logBasicReq(sb,request, connection)
            }
            LogLevel.HEADERS->{
                logHeadersReq(sb,request, connection)
            }
            LogLevel.BODY->{
                logBodyReq(sb,request, connection)
            }
        }
    }

    /**
     * 记录响应日志
     * [response] 响应数据
     */
    fun logResponse(response: Response){
        val sb = StringBuilder()
        sb.appendLine("\r\n")
        sb.appendLine("<-")
        when(logLevel){
            LogLevel.NONE->{
                //do nothing
            }
            LogLevel.BASIC->{

            }
            LogLevel.HEADERS->{

            }
            LogLevel.BODY->{

            }
        }
    }

    //region log request
    private fun logBodyReq(sb: StringBuilder,request: Request,connection: Connection?){
        logHeadersReq(sb, request, connection)
        sb.appendLine("RequestBody:${request.body.toString()}")
    }
    private fun logHeadersReq(sb: StringBuilder,request: Request,connection: Connection?){
        logBasicReq(sb,request,connection)
        val headersStr = request.headers.joinToString {
            "请求Header:{${it.first} = ${it.second}}"
        }
        sb.appendLine(headersStr)
    }
    private fun logBasicReq(sb: StringBuilder,request: Request,connection: Connection?){
        sb.appendLine("请求 method: ${request.method} url: ${request.url.toString()} tag:" +
                "${request.tag()} protocol:${connection?.protocol() ?: Protocol.HTTP_1_1}" )
    }
    //endregion

    //region log response
     private fun logHeadersRsp(response: Response,sb: StringBuilder){

     }
    private fun logBasicRsp(response: Response,sb: StringBuilder){
        sb.appendLine("响应protocol:${response.protocol} code:${response.code} message:${response.message}")
        sb.appendLine("响应request url:${response.request.url.toString()}")
    }
    //endregion

    companion object {
        private const val TAG = "<KtHttp>" //默认的TAG
        const val MILLIS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSXXX" //时间格式化

        /**
         * 转化为格式化的时间字符串
         */
        fun ToDataTimeStr(millis: Long, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(millis)
        }

    }

    /**
     * 打印日志的范围
     */
    enum class LogLevel {
        NONE, //不打印
        BASIC, //只打印行首，请求/响应
        HEADERS, //打印所有请求的所有 header
        BODY, //打印所有
    }

    /**
     * log颜色等级，对应Android logcat 的 v d i w e
     */
    enum class ColorLevel {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}