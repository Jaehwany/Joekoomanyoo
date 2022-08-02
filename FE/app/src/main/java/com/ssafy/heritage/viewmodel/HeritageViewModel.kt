package com.ssafy.heritage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.heritage.data.dto.Heritage
import com.ssafy.heritage.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "HeritageViewModel___"

class HeritageViewModel : ViewModel() {

    private val repository = Repository.get()

    private val _heritageList = MutableLiveData<List<Heritage>>()
    val heritageList: LiveData<List<Heritage>>
        get() = _heritageList

    private val _heritage = MutableLiveData<Heritage>()
    val heritage: LiveData<Heritage>
        get() = _heritage

    // 전체 문화유산 목록 가져옴
    fun getHeritageList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.selectAllHeritage().let { response ->
                if (response.isSuccessful) {
                    var list = (response.body() as MutableList<Heritage>)
                    list.sortBy { it.heritageScrapCnt } // 스크랩순 정렬
                    _heritageList.postValue(list)
                } else {
                    Log.d(TAG, "${response.code()}")
                }

            }
        }
    }
    // private 값을 변경할 수 있는 함수
    fun setHeritage(heritage: Heritage) {
        _heritage.postValue(heritage)
    }

    // 더미 테스트용
    fun test(list: List<Heritage>) {
        _heritageList.value = list
    }
}