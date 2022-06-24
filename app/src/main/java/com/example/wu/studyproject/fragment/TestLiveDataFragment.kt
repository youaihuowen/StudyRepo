package com.example.wu.studyproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.util.Function
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.wu.studyproject.MainActivity
import com.example.wu.studyproject.R
import com.example.wu.studyproject.databinding.FragmentTestLiveDataBinding

class TestLiveDataFragment : Fragment() {

    private lateinit var binding:FragmentTestLiveDataBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTestLiveDataBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).apply {
            live.observe(viewLifecycleOwner, Observer {
                binding.tvLivedataValue.text="Fragment中liveData值：$it"
                Log.i("LiveData", "Fragment中liveData值变化")
            })
            val liveOne = Transformations.map(live, Function {
                "map is ${it.takeLast(4)}"
            })
            liveOne.observe(viewLifecycleOwner, Observer {
                binding.tvLiveMap.text="$it"
            })

            mediator.observe(viewLifecycleOwner, Observer {
                binding.tvLiveMedia.text="$it";
            })

            val liveSW=Transformations.switchMap(mediator, Function {
                val temp = it.last().toInt()
                if(temp%2==0){
                    live1
                }else{
                    live2
                }
            })

            liveSW.observe(viewLifecycleOwner, Observer {
                binding.tvLiveSwitch.text="${it}"
                Log.i("liveSwitch", "run in liveSwitch observer!")
            })
        }
    }



}