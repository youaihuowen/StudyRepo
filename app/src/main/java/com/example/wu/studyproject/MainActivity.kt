package com.example.wu.studyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.wu.studyproject.databinding.ActivityMainBinding
import com.example.wu.studyproject.fragment.TestLiveDataFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val live = MutableLiveData<String>()
    val liveNew = Transformations.map(live, Function {
        Pair<Int,String>(it.hashCode(),it);
    })

    val live1=MutableLiveData<String>()
    val live2 = MutableLiveData<String>()
    val mediator = MediatorLiveData<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    fun initView(){
        val liveDataFragment = TestLiveDataFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_layout,liveDataFragment)
            .commit()
        //ToDO fragment显隐使用hide show时不会改变fragment的生命周期
        binding.btnHideFragment.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .detach(liveDataFragment)
                .commit()
        }

        binding.btnShowFragment.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .attach(liveDataFragment)
                .commit()
        }

        binding.btnChange.setOnClickListener {
            live.value = "当前时间戳：${System.currentTimeMillis()}"
        }

        binding.btnChangeLive1.setOnClickListener{
            live1.value="one live change time:${System.currentTimeMillis()}"
        }

        binding.btnChangeLive2.setOnClickListener {
            live2.value="two live change time:${System.currentTimeMillis()}"
        }

        live.observe(this, Observer {
            binding.tvMsg.text="Activity中liveData值：$it"
            Log.i("LiveData", "Activity中liveData数据变化控件刷新")
        })


        liveNew.observe(this, Observer {
            binding.tvMap.text="$it";
        })


        mediator.addSource(live2, Observer {
            Log.i("mediatorLiveData", "two $it ")
            mediator.value="$it"
        })
        mediator.addSource(live1, Observer {
            Log.i("mediatorLiveData", "one $it ")
            mediator.value="$it"
        })

    }
}