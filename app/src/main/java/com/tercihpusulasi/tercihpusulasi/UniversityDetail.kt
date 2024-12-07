package com.tercihpusulasi.tercihpusulasi

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tercihpusulasi.tercihpusulasi.roomdatabase.University
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue

class UniversityDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            window.statusBarColor = DarkBlue.toArgb()
            val university = intent.getParcelableExtra<University>("universityData")
            UniversityDetailScreen(university?.name?:"",university?.city?:"") {
                val intent = Intent(this, ProgramListActivity::class.java)
                intent.putExtra("universityData",university)
                startActivity(intent)
            }
        }
    }
}

@Composable
fun UniversityDetailScreen(
    universityName: String,
    city: String,
    onProgramsClick: () -> Unit // Bölümler butonuna tıklama
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp)) // Dikey boşluk

        // Üniversite Adı
        Text(
            text = universityName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Bölümler Butonu
        Button(
            onClick = onProgramsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bölümler")
        }

        // Diğer Bilgiler
        Text(
            text = "Diğer Bilgiler",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
