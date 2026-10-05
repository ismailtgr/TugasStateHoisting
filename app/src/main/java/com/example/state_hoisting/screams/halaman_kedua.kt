package com.example.state_hoisting.screams

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.state_hoisting.ui.theme.State_HoistingTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun halaman_kedua()
{
    var number by remember { mutableStateOf(0) }
    val label by remember {mutableStateOf("Decrement")}
    Column() {
        Text(text = "Halaman Kedua")
        CounterScreen(modifier = Modifier.padding(32.dp), number, label
        )
        {
            number--
        }
    }
}