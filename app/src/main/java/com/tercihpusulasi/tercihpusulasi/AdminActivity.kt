package com.tercihpusulasi.tercihpusulasi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tercihpusulasi.tercihpusulasi.roomdatabase.Program
import com.tercihpusulasi.tercihpusulasi.roomdatabase.University
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            window.statusBarColor = DarkBlue.toArgb()
            UniversityFormScreen()

        }
    }
}

@Composable
fun UniversityFormScreen() {
    var universityName by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var universityId by remember { mutableStateOf("") }
    val programs = remember { mutableStateListOf<ProgramInput>() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Kaydırma aktif ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp)) // Dikey boşluk

        // Başlık
        Text(
            text = "Üniversite ve Program Formu",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Üniversite Adı
        OutlinedTextField(
            value = universityName,
            onValueChange = { universityName = it },
            label = { Text("Üniversite Adı") },
            placeholder = { Text("Üniversite adı girin") },
            modifier = Modifier.fillMaxWidth()
        )

        // Şehir
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Şehir") },
            placeholder = { Text("Şehir adı girin") },
            modifier = Modifier.fillMaxWidth()
        )

        // Üniversite ID
        OutlinedTextField(
            value = universityId,
            onValueChange = { universityId = it },
            label = { Text("Üniversite ID") },
            placeholder = { Text("Örn: 1") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Program Ekleme Alanı
        Text(
            text = "Programlar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        programs.forEachIndexed { index, program ->
            ProgramInputForm(
                program = program,
                onRemove = { programs.removeAt(index) }
            )
        }

        // Program Ekle Butonu
        Button(
            onClick = { programs.add(ProgramInput()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Program Ekle")
        }

        val context = LocalContext.current

        // Kaydet Butonu
        Button(
            onClick = {
                val university = University(
                    city = city,
                    id = universityId.toIntOrNull() ?: 0,
                    name = universityName,
                    programs = programs.map { it.toProgram() }
                )

                val database = FirebaseDatabase.getInstance()
                val universitiesRef = database.getReference("universities")

                universitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val univercityList = arrayListOf<University>()
                        for (data in snapshot.children) {
                            val univercityModel = data.getValue(University::class.java)
                            univercityModel?.let {
                                univercityList.add(univercityModel)
                            }
                        }

                        univercityList.add(university)


                        universitiesRef.setValue(univercityList)
                            .addOnSuccessListener {
                                Log.d("Firebase", "Üniversite listesi başarıyla eklendi.")
                                val intent = Intent(context, AdminActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Firebase", "Hata: ${exception.message}")
                            }
                        Log.d("UniversityForm", "Kaydedilen Üniversite: $university")


                        // Artık studentList içinde tüm öğrenci verileri mevcut
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Veritabanı hatası: ${error.message}")
                    }
                })


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kaydet")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Dikey boşluk

    }
}

// Program Input Modeli
data class ProgramInput(
    var name: MutableState<String> = mutableStateOf(""),
    var language: MutableState<String> = mutableStateOf(""),
    var id: MutableState<String> = mutableStateOf(""),
    var lastYearMinScore: MutableState<String> = mutableStateOf(""),
    var quota: MutableState<String> = mutableStateOf(""),
    var scoreType: MutableState<String> = mutableStateOf("")
) {
    fun toProgram() = Program(
        id = id.value.toIntOrNull() ?: 0,
        language = language.value,
        last_year_min_score = lastYearMinScore.value.toDoubleOrNull() ?: 0.0,
        name = name.value,
        quota = quota.value.toIntOrNull() ?: 0,
        score_type = scoreType.value
    )
}
@Composable
fun ProgramInputForm(program: ProgramInput, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Program Adı
            OutlinedTextField(
                value = program.name.value,
                onValueChange = { program.name.value = it },
                label = { Text("Program Adı") },
                placeholder = { Text("Program adı girin") },
                modifier = Modifier.fillMaxWidth()
            )

            // Program Dili
            OutlinedTextField(
                value = program.language.value,
                onValueChange = { program.language.value = it },
                label = { Text("Dil") },
                placeholder = { Text("Dil girin") },
                modifier = Modifier.fillMaxWidth()
            )

            // Program ID
            OutlinedTextField(
                value = program.id.value,
                onValueChange = { program.id.value = it },
                label = { Text("Program ID") },
                placeholder = { Text("Örn: 101") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Son Yıl Minimum Puan
            OutlinedTextField(
                value = program.lastYearMinScore.value,
                onValueChange = { program.lastYearMinScore.value = it },
                label = { Text("Son Yıl Minimum Puan") },
                placeholder = { Text("Örn: 450.5") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Kontenjan
            OutlinedTextField(
                value = program.quota.value,
                onValueChange = { program.quota.value = it },
                label = { Text("Kontenjan") },
                placeholder = { Text("Örn: 50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Puan Türü
            OutlinedTextField(
                value = program.scoreType.value,
                onValueChange = { program.scoreType.value = it },
                label = { Text("Puan Türü") },
                placeholder = { Text("Sayısal/Eşit Ağırlık") },
                modifier = Modifier.fillMaxWidth()
            )

            // Sil Butonu
            Button(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Programı Sil")
            }
        }
    }
}

