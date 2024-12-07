package com.tercihpusulasi.tercihpusulasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.tercihpusulasi.tercihpusulasi.roomdatabase.Student
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import java.util.concurrent.TimeUnit

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseApp.getInstance()
            auth = Firebase.auth
            window.statusBarColor = DarkBlue.toArgb()
            val isLoading = remember { mutableStateOf(false) }

            val user = auth.currentUser
            val database = FirebaseDatabase.getInstance()

            val studentsRef = database.getReference("students")

            studentsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val student = data.getValue(Student::class.java)
                        student?.let {
                            if (user?.phoneNumber == student.phoneNumber) {
                                if (student.admin) {
//                                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                            }
                        }
                    }


                    // Artık studentList içinde tüm öğrenci verileri mevcut
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Veritabanı hatası: ${error.message}")
                }
            })
            LoginScreen(isLoading)
            IndeterminateProgressBar(isLoading.value)

        }
    }


    fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Otomatik olarak doğrulandı
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Hata
                    Log.e("FirebaseAuth", "Doğrulama başarısız: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    this@LoginActivity.verificationId = verificationId
                    val intent = Intent(this@LoginActivity, OtpScreenActivity::class.java)
                    intent.putExtra("verificationId", verificationId)
                    startActivity(intent)
                    finish()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                Log.d("FirebaseAuth", "Kullanıcı girişi başarılı: ${user?.phoneNumber}")
            } else {
                Log.e("FirebaseAuth", "Giriş başarısız: ${task.exception?.message}")
            }
        }
    }

}


@Composable
fun LoginScreen(
    isLoading: MutableState<Boolean>
) {
    var phoneNumber by remember { mutableStateOf("") }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tercih Pusulası",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )

            // Email Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Cep Telefonu") },
                placeholder = { Text("Cep telefonu girin") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            val context = LocalContext.current

            // Login Button
            Button(
                onClick = {
                    isLoading.value = true
                    (context as LoginActivity?)?.sendVerificationCode(phoneNumber)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Giriş Yap")
            }


            TextButton(onClick = {

            }) {
                Text("Bize ulaşın")
            }


        }
    }


}

@Composable
fun IndeterminateProgressBar(isVisible: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

