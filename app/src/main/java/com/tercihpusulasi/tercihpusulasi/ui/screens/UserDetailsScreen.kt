package com.tercihpusulasi.tercihpusulasi.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.gson.Gson
import com.sametdundar.guaranteeapp.roomdatabase.FormData
import com.sametdundar.guaranteeapp.roomdatabase.FormViewModel
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import com.tercihpusulasi.tercihpusulasi.utils.JsonConverter.fromJson
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(formData: FormData, navController: NavHostController) {

    val imageUri = fromJson<ArrayList<String>>(formData.imageUris ?: "")

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    imageUris = imageUri.map { Uri.parse(it) } as ArrayList

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {

        // Ana Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

            Column {
                Text(
                    "ÜRÜN DETAY BİLGİLERİ", modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            }

            ImagePickerAppDetail(formData, imageUris, onImagesSelected = { uris ->
                imageUris = uris
            })

            val viewModel: FormViewModel = hiltViewModel()

            FormScreenDetail(formData, viewModel, navController, imageUris)

        }

    }

}

@Composable
fun ImagePickerAppDetail(
    formData: FormData,
    imageUris: List<Uri>,
    onImagesSelected: (List<Uri>) -> Unit
) {

    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.let {
            it.forEach { uri ->
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            onImagesSelected(it)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Ürün Görseli ve Fatura Görseli Ekle")
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Resimler arasına boşluk ekle
        ) {
            items(imageUris) { uri ->
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier.size(100.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = uri)
                                .build()
                        ),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(100.dp) // Resimleri 100x100 dp boyutuna küçült
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp)) // Köşeleri yuvarlak yap
                            .clickable { selectedImageUri = uri }, // Tıklanınca resmi büyüt
                        contentScale = ContentScale.Crop
                    )
                    // Sil butonu
                    IconButton(
                        onClick = {
                            onImagesSelected(imageUris.toMutableList().apply { remove(uri) })
                        }
                    ) {
                        Icon(
                            painter = rememberAsyncImagePainter(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Delete Image",
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormScreenDetail(
    formData: FormData,
    viewModel: FormViewModel,
    navController: NavHostController,
    imageUris: List<Uri>
) {
    var baslik by remember { mutableStateOf(TextFieldValue(formData.name)) }
    var email by remember { mutableStateOf(TextFieldValue(formData.email)) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue(formData.phoneNumber)) }
    var address by remember { mutableStateOf(TextFieldValue(formData.address)) }
    var noteStart by remember { mutableStateOf(TextFieldValue(formData.noteStart)) }
    var noteEnd by remember { mutableStateOf(TextFieldValue(formData.noteEnd)) }
    var noteTime by remember { mutableStateOf(TextFieldValue(formData.noteTime)) }
    var additinalInformation by remember { mutableStateOf(TextFieldValue(formData.additionalInformation)) }
    var isChecked by remember { mutableStateOf(formData.isChecked) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name input field
        TextField(
            value = baslik,
            onValueChange = { baslik = it },
            label = { Text("Başlık") },
            modifier = Modifier.fillMaxWidth()
        )

        // Email input field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Mail Adresi") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        // Phone number input field
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Telefon Numarası") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        // Address input field
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Ürünün Bulunduğu Adres") },
            modifier = Modifier.fillMaxWidth()
        )

        // noteStart field
        TextField(
            value = noteStart,
            onValueChange = { noteStart = it },
            label = { Text("Garanti Başlangıcı") },
            modifier = Modifier.fillMaxWidth()
        )

        // noteEnd field
        TextField(
            value = noteEnd,
            onValueChange = { noteEnd = it },
            label = { Text("Garanti Bitişi") },
            modifier = Modifier.fillMaxWidth()
        )

        // noteTime field
        TextField(
            value = noteTime,
            onValueChange = { noteTime = it },
            label = { Text("Garanti Süresi") },
            modifier = Modifier.fillMaxWidth()
        )

        // Address input field
        TextField(
            value = additinalInformation,
            onValueChange = { additinalInformation = it },
            label = { Text("Ek Bilgi") },
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(), // Satırı genişlet
            verticalAlignment = Alignment.CenterVertically // Yükseklik merkezleme
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it } // Durumu güncelle
            )
            Spacer(modifier = Modifier.width(4.dp)) // Checkbox ile metin arasında boşluk
            Text("Garanti Devam Ediyor mu ?") // Duruma göre metin
        }


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    println("Form submitted with: $baslik, $email, $phoneNumber, $address")
                    coroutineScope.launch {
                        viewModel.updateFormData(
                            FormData(
                                formData.id,
                                baslik.text,
                                email.text,
                                phoneNumber.text,
                                address.text,
                                noteStart.text,
                                noteEnd.text,
                                noteTime.text,
                                additinalInformation.text,
                                isChecked,
                                Gson().toJson(imageUris.map { it.toString() }) // URI'leri JSON string'e çeviriyoruz
                            )
                        )

                        // Form alanlarını sıfırla
                        baslik = TextFieldValue("")
                        email = TextFieldValue("")
                        phoneNumber = TextFieldValue("")
                        address = TextFieldValue("")
                        additinalInformation = TextFieldValue("")

                    }
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Güncelle")
            }

            Spacer(modifier = Modifier.width(8.dp)) // Butonlar arasına boşluk ekliyoruz

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteFormData(formData)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Sil")
            }
        }
    }
}