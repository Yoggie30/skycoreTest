package com.example.yogeshtestapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.yogeshtestapplication.model.ResponseDO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CommonRepository : BaseRepository() {

    @SuppressLint("CheckResult")
    fun search(context: Context, obj: HashMap<String, Any>): MutableLiveData<ResponseDO> {
        val header = "Bearer XPFgzKwZGK1yqRxHi0d5xsARFOLpXIvccQj5jekqTnysweGyoIfVUHcH2tPfGq5Oc9kwKHPkcOjk2d1Xobn7aTjOFeop8x41IUfVvg2Y27KiINjYPADcE7Qza0RkX3Yx"
        val data = MutableLiveData<ResponseDO>()
        getNetworkService(context)
            .search(header, obj)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response: ResponseDO? ->
                if (response != null) {
                    response.status_flag = true
                    data.postValue(response)
                }
            }, { error: Throwable? ->
                error?.printStackTrace()
                data.postValue(ResponseDO())
            })
        return data
    }

}

