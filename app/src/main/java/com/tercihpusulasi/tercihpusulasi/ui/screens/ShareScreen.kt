package com.tercihpusulasi.tercihpusulasi.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sametdundar.guaranteeapp.roomdatabase.FormViewModel
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import kotlinx.coroutines.launch

@Composable
fun ShareScreen(navController: NavHostController) {

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) } // imageUris burada tanımlandı

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

            Column {
                Text(
                    "ÜRÜNÜNÜZÜN GARANTİ BİLGİLERİNİ PAYLAŞIN", modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            }


            ImagePickerApp(imageUris, onImagesSelected = { uris ->
                imageUris = uris
            })

            val viewModel: FormViewModel = hiltViewModel()

            // FormScreen'e de imageUris'i geçiyoruz
            FormScreen(viewModel, navController, imageUris)

        }

    }

}

@Composable
fun ImagePickerApp(
    imageUris: List<Uri>, // imageUris parametre olarak alındı
    onImagesSelected: (List<Uri>) -> Unit // Seçilen resimleri geri döndüren bir callback
) {

    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Resim seçici başlatıcı (multiple resim seçimi için)
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

            onImagesSelected(it) // Seçilen URI'leri günceller
        }
    }

    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
//        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Ürün Görseli ve Fatura Görseli Ekle")
        }

        // Seçilen resimleri yatay bir listede göstermek için LazyRow kullanıyoruz
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
                        painter = rememberAsyncImagePainter(uri),
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

    // Eğer bir resim seçilmişse tam ekran göster
    selectedImageUri?.let { uri ->
        ImageDialog(uri) {
            selectedImageUri = null // Dialog kapatıldığında URI'yi sıfırla
        }
    }
}


@Composable
fun ImageDialog(uri: Uri, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var scale by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = maxOf(1f, scale),
                        scaleY = maxOf(1f, scale),
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .fillMaxWidth()
                    .aspectRatio(1f), // Oranı koruyarak resmin boyutunu ayarla
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = rememberAsyncImagePainter(android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun FormScreen(viewModel: FormViewModel, navController: NavHostController, imageUris: List<Uri>) {
    var baslik by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var noteStart by remember { mutableStateOf(TextFieldValue("")) }
    var noteEnd by remember { mutableStateOf(TextFieldValue("")) }
    var noteTime by remember { mutableStateOf(TextFieldValue("")) }
    var additinalInformation by remember { mutableStateOf(TextFieldValue("")) }
    var isChecked by remember { mutableStateOf(false) }

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


        // Submit button
        Button(
            onClick = {
                println("Form submitted with: $baslik, $email, $phoneNumber, $address")
                coroutineScope.launch {
                    viewModel.saveFormData(
                        baslik.text,
                        email.text,
                        phoneNumber.text,
                        address.text,
                        noteStart.text,
                        noteEnd.text,
                        noteTime.text,
                        additinalInformation.text,
                        isChecked,
                        imageUris.map { it.toString() }
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Paylaş")
        }
    }
}