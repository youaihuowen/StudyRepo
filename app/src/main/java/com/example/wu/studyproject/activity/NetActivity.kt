package com.example.wu.studyproject.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.example.wu.net.HttpCallback
import com.example.wu.net.OkHttpApi
import com.example.wu.studyproject.databinding.ActivityNetBinding

class NetActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initNet()
        
    }
    
    fun initNet(){
        val map:Map<String,String> = mapOf(
            "key" to "free",
            "appid" to "0",
            "msg" to "你好！我想和你做朋友，可以吗？"
        )
        val okHttpApi:OkHttpApi = OkHttpApi()
        okHttpApi.get("api.php",map,object :HttpCallback{
            override fun onSuccess(data: Any?) {
                LogUtils.d("success : ${data.toString()}")
                runOnUiThread {
                    binding.tvMsg.text=data.toString()
                }
            }

            override fun onFail(error: Any?) {
                LogUtils.d("fail : ${error.toString()}")
                binding.tvMsg.text=error.toString()
            }
        })

        okHttpApi.post("",LoginReq(),object:HttpCallback{
            override fun onSuccess(data: Any?) {
                LogUtils.d("success : ${data.toString()}")
                runOnUiThread {
                    binding.tvMsg.text=data.toString()
                }
            }

            override fun onFail(error: Any?) {
                LogUtils.d("fail : ${error.toString()}")
                binding.tvMsg.text=error.toString()
            }

        })
    }
}

data class LoginReq(val mobi:String="18648957777", val password:String ="cn5123456")