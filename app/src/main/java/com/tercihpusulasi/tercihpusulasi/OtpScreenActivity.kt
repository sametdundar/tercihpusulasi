package com.tercihpusulasi.tercihpusulasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.tercihpusulasi.tercihpusulasi.roomdatabase.Student
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue

class OtpScreenActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseApp.getInstance()
            auth = Firebase.auth
            window.statusBarColor = DarkBlue.toArgb()
            verificationId = intent.getStringExtra("verificationId")
            OTPScreen { otp ->
                verifyCode(otp)

            }
        }
    }

    fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                val database = FirebaseDatabase.getInstance()

                val studentsRef = database.getReference("students")

                studentsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val student = data.getValue(Student::class.java)
                            student?.let {
                                if (user?.phoneNumber == student.phoneNumber) {
                                    if (student.admin) {
                                        val intent = Intent(this@OtpScreenActivity, AdminActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else {
                                        val intent = Intent(this@OtpScreenActivity, MainActivity::class.java)
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
                Log.d("FirebaseAuth", "Kullanıcı girişi başarılı: ${user?.phoneNumber}")
            } else {
                Log.e("FirebaseAuth", "Giriş başarısız: ${task.exception?.message}")
            }
        }
    }
}

@Composable
fun OTPScreen(
    otpLength: Int = 6,
    onOtpSubmit: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    val maxOtpLength = otpLength

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "OTP Doğrulama",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // OTP Input
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            repeat(maxOtpLength) { index ->
                val char = if (index < otpCode.length) otpCode[index].toString() else ""
                OutlinedTextField(
                    value = char,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && otpCode.length < maxOtpLength) {
                            otpCode += newValue
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    singleLine = true,
                    modifier = Modifier
                        .width(50.dp)
                        .height(60.dp),
                    enabled = false // Sadece dekorasyon için
                )
            }
        }

        OutlinedTextField(
            value = otpCode,
            onValueChange = {
                if (it.length <= maxOtpLength && it.all { char -> char.isDigit() }) {
                    otpCode = it
                }
            },
            label = { Text("OTP Kodu") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Verify Button
        Button(
            onClick = { onOtpSubmit(otpCode) },
            enabled = otpCode.length == maxOtpLength,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Doğrula")
        }
    }
}



