package com.sametdundar.guaranteeapp.roomdatabase

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tercihpusulasi.tercihpusulasi.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private var repository: FormRepository,
    private val application: MyApplication
) : ViewModel() {


    val allFormData: LiveData<List<FormData>>
    val formDataDetail: MutableLiveData<FormData> = MutableLiveData()

    init {
        val userDao = AppDatabase.getDatabase(application).formDao()
        repository = FormRepository(userDao)
        allFormData = repository.allUsers
    }

    fun saveFormData(
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        noteStart: String,
        noteEnd: String,
        noteTime: String,
        additionalInformation: String,
        isChecked:Boolean,
        imageUris: List<String>
    ) {

        val formData = FormData.from(name, email, phoneNumber, address,noteStart,noteEnd,noteTime,additionalInformation,isChecked, imageUris)

        viewModelScope.launch {
            repository.insertForm(formData)
        }
    }

    fun sendFormDataDetail(formData: FormData){
        formDataDetail.value = formData
    }

    fun updateFormData(formData: FormData) {
        viewModelScope.launch {
            repository.updateFormData(formData)
        }
    }

    fun deleteFormData(formData: FormData) {
        viewModelScope.launch {
            repository.deleteFormData(formData)
        }
    }
}
