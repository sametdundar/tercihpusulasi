package com.sametdundar.guaranteeapp.roomdatabase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "form_data")
data class FormData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val noteStart: String,
    val noteEnd: String,
    val noteTime: String,
    val additionalInformation: String,
    val isChecked: Boolean,
    val imageUris: String // JSON formatında URI'leri tutacağız
): Parcelable {

    companion object {
        fun from(
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
        ): FormData {
            val imageUrisJson = Gson().toJson(imageUris) // URI'leri JSON string'e çeviriyoruz
            return FormData(
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                noteStart = noteStart,
                noteEnd = noteEnd,
                noteTime = noteTime,
                additionalInformation = additionalInformation,
                isChecked = isChecked,
                imageUris = imageUrisJson
            )
        }
    }
}
