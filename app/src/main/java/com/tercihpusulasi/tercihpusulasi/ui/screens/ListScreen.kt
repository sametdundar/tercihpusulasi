package com.tercihpusulasi.tercihpusulasi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sametdundar.guaranteeapp.roomdatabase.FormData
import com.sametdundar.guaranteeapp.roomdatabase.FormViewModel
import com.tercihpusulasi.tercihpusulasi.navigation.ScreenRoutes
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import com.tercihpusulasi.tercihpusulasi.ui.theme.GreenColor
import com.tercihpusulasi.tercihpusulasi.ui.theme.RedColor
import com.tercihpusulasi.tercihpusulasi.utils.JsonConverter.toJson

@Composable
fun ListScreen(viewModel: FormViewModel = hiltViewModel(), navController: NavHostController) {

    val allFormData by viewModel.allFormData.observeAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                // Başlık
                item {
                    Text(
                        "GARANTİ BİLGİLERİNİ EKLEDİĞİNİZ ÜRÜNLERİN LİSTESİ",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                }

                // Boşluk ekleme
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Kullanıcı Listesi
                items(allFormData.reversed()) { formData ->
                    ListItem(formData = formData, navController,viewModel)
                }
            }
        }
    }
}

@Composable
fun ListItem(formData: FormData, navController: NavHostController, viewModel: FormViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = if(formData.isChecked) GreenColor else RedColor),
        onClick = {

            navController.navigate("${ScreenRoutes.FormDetail.route}?formData=${toJson(formData)}")

        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = formData.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Kaç Yıl Garantisi ${if(formData.isChecked) "Var" else "Vardı"}: ${formData.noteTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = "Garanti Devam Ediyor mu: ${if (formData.isChecked) "Devam Ediyor" else "Bitti"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}