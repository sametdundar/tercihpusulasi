package com.tercihpusulasi.tercihpusulasi.ui.screens

import android.content.Intent
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sametdundar.guaranteeapp.roomdatabase.FormViewModel
import com.tercihpusulasi.tercihpusulasi.UniversityDetail
import com.tercihpusulasi.tercihpusulasi.roomdatabase.University
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import com.tercihpusulasi.tercihpusulasi.ui.theme.Purple40

@Composable
fun ListScreen(viewModel: FormViewModel = hiltViewModel(), navController: NavHostController) {

    val universityList = remember { mutableStateOf(listOf<University>()) }


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
                        "ÜNİVERSİTELERİN LİSTESİ",
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


                val universitiesRef = FirebaseDatabase.getInstance().getReference("universities")
                universitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<University>()
                        for (data in snapshot.children) {
                            val universityModel = data.getValue(University::class.java)
                            universityModel?.let {
                                list.add(it)
                            }
                        }
                        universityList.value = list
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Veritabanı hatası: ${error.message}")
                    }
                })

                items(universityList.value) { university ->
                    ListItem(university = university, navController, viewModel)
                }


            }
        }
    }
}

@Composable
fun ListItem(university: University, navController: NavHostController, viewModel: FormViewModel) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Purple40),
        onClick = {
            val intent = Intent(context, UniversityDetail::class.java)
            intent.putExtra("universityData",university)
            context.startActivity(intent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = university.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}