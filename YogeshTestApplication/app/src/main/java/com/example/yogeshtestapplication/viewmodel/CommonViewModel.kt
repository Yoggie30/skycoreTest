package com.example.yogeshtestapplication.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yogeshtestapplication.model.ResponseDO
import com.example.yogeshtestapplication.repository.CommonRepository

class CommonViewModel : ViewModel() {

    fun search(context: Context, obj: HashMap<String, Any>): MutableLiveData<ResponseDO> {
        return CommonRepository.search(context, obj)
    }

}

