package com.tercihpusulasi.tercihpusulasi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tercihpusulasi.tercihpusulasi.roomdatabase.Program
import com.tercihpusulasi.tercihpusulasi.roomdatabase.University

class ProgramListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val university = intent.getParcelableExtra<University>("universityData")
            val programList = university?.programs?: listOf()
            Spacer(modifier = Modifier.height(20.dp))

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // Program listesi burada gösterilecek
                ProgramList(
                    programList = programList,
                    modifier = Modifier.padding(innerPadding)
                )
            }

        }
    }
}

@Composable
fun ProgramList(programList: List<Program>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(programList) { program ->
            ProgramItem(program = program)
        }
    }
}

@Composable
fun ProgramItem(program: Program) {
    // Her bir program için bir kart görünümü
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            val intent = Intent(context, ProgramDetailActivity::class.java)
            intent.putExtra("programData",program)
            context.startActivity(intent)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = program.name)
            Text(text = "Kontenjan: ${program.quota}")
            Text(text = "Puan Türü: ${program.score_type}")
        }
    }
}