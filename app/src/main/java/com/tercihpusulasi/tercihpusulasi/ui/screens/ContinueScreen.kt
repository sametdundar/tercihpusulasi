package com.tercihpusulasi.tercihpusulasi.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.tercihpusulasi.tercihpusulasi.LoginActivity
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue

@Composable
fun ContinueScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {

            Spacer(modifier = Modifier.height(280.dp))

            val context = LocalContext.current

            Text(
                "GARANTİNİ DEVAM ETTİR\nÇOK YAKINDA SİZİNLE OLACAK", modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
                    .clickable {
                        FirebaseAuth
                            .getInstance()
                            .signOut()
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()

                    },
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }
    }
}