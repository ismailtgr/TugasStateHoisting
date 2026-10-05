package com.example.state_hoisting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // State yang dikelola oleh Parent (State Hoisting)
                val hargaTiket by rememberSaveable { mutableIntStateOf(50000) }
                var jumlahTiket by rememberSaveable { mutableIntStateOf(1) }
                var namaPembeli by rememberSaveable { mutableStateOf("") }

                // State Status Pemesanan
                var statusText by rememberSaveable { mutableStateOf("Silakan pesan tiket") }
                var statusType by rememberSaveable { mutableStateOf("info") } // "info", "loading", "success", "error"
                var isProcessing by rememberSaveable { mutableStateOf(false) }

                // LaunchedEffect untuk menangani alur pemesanan tiket
                LaunchedEffect(isProcessing) {
                    if (isProcessing) {
                        statusText = "Memproses pesanan..."
                        statusType = "loading"
                        delay(2000) // Delay 2 detik sesuai alur

                        statusText = "Tiket berhasil dipesan!"
                        statusType = "success"
                        isProcessing = false
                    }
                }

                TiketScreen(
                    hargaTiket = hargaTiket,
                    jumlahTiket = jumlahTiket,
                    namaPembeli = namaPembeli,
                    statusText = statusText,
                    statusType = statusType,
                    isProcessing = isProcessing,
                    onNamaChange = {
                        namaPembeli = it
                        if (statusType == "error" && it.isNotBlank()) {
                            statusText = "Silakan pesan tiket"
                            statusType = "info"
                        }
                    },
                    onTambahJumlah = { jumlahTiket++ },
                    onKurangJumlah = { if (jumlahTiket > 1) jumlahTiket-- },
                    onPesanClick = {
                        if (namaPembeli.isBlank()) {
                            statusText = "Nama harus diisi"
                            statusType = "error"
                        } else {
                            isProcessing = true
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiketScreen(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    statusText: String,
    statusType: String,
    isProcessing: Boolean,
    onNamaChange: (String) -> Unit,
    onTambahJumlah: () -> Unit,
    onKurangJumlah: () -> Unit,
    onPesanClick: () -> Unit
) {
    val totalHarga = hargaTiket * jumlahTiket

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemesanan Tiket", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Input Nama
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Detail Pemesan", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = namaPembeli,
                        onValueChange = onNamaChange,
                        label = { Text("Nama Lengkap") },
                        placeholder = { Text("Masukkan nama Anda") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Card Jumlah Tiket & Rincian Harga
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Jumlah Tiket", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = onKurangJumlah, enabled = jumlahTiket > 1 && !isProcessing) {
                                    Icon(Icons.Default.Remove, contentDescription = "Kurang")
                                }
                                Text(
                                    text = "$jumlahTiket",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(onClick = onTambahJumlah, enabled = !isProcessing) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah")
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Pembayaran", fontSize = 12.sp, color = Color.Gray)
                            Text("Rp $totalHarga", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Tombol Pesan Tiket
            Button(
                onClick = onPesanClick,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pesan Tiket Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Card Banner Status
            StatusCard(statusText = statusText, statusType = statusType)
        }
    }
}

@Composable
fun StatusCard(statusText: String, statusType: String) {
    val (backgroundColor, contentColor, icon) = when (statusType) {
        "loading" -> Triple(Color(0xFFE3F2FD), Color(0xFF1E88E5), null)
        "success" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.CheckCircle)
        "error" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Default.Error)
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF616161), Icons.Default.Info)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (statusType == "loading") {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = contentColor)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Status: $statusText",
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}