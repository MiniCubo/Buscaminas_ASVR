package com.example.buscaminasasvr

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminasasvr.ui.theme.BuscaminasASVRTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuscaminasASVRTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("Buscaminas") }) }) { innerPadding ->
                    Tablero(name = "Android", modifier = Modifier.padding(innerPadding).fillMaxSize(), mensaje = {mensaje()})
                }
            }
        }
    }

    @Preview(showBackground = true)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TableroPreview() {
        BuscaminasASVRTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { TopAppBar(title = { Text("Buscaminas") }) }
            ) { innerPadding ->
                Tablero(
                    name = "Android",
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    mensaje = {}
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)

    @Composable
    fun Tablero(name: String, modifier: Modifier = Modifier, mensaje: () -> Unit) {
        val columnas = 6
        val filas = 10

        var estadoBotones = remember { List(filas*columnas) { mutableStateOf(true) } }
        var minas = remember { List(filas*columnas){mutableStateOf(asignaMina())} }
        var verAlerta = remember { mutableStateOf(false) }
        var atinados = remember {mutableStateOf(0)}
        var verGanar = remember { mutableStateOf(false)}

        Column(
            modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            for (i in 1..filas) {
                Row(modifier = Modifier.weight(.1f).fillMaxSize()) {
                    for(j in 1..columnas){
                        val index = (i-1) * columnas + j - 1
                        Button(onClick = {
                            estadoBotones[index].value = !estadoBotones[index].value
                            if(!minas[index].value){
                                mensaje()
                                atinados.value += 1
                                if(atinados.value.toDouble() / (filas*columnas).toDouble() > 0.5) verGanar.value = true
                            }
                            else verAlerta.value = true
                        }, modifier = Modifier.weight(1f).fillMaxSize().padding(2.dp), shape = RectangleShape, enabled = estadoBotones[index].value){
                            Text(if (!estadoBotones[index].value && minas[index].value) "O" else "X",
                                style = TextStyle(fontSize = 24.sp), color = if(!estadoBotones[index].value && minas[index].value) Color.Red else Color.Black)
                        }
                    }
                }
            }
        }

        if(verAlerta.value){
            AlertDialog(onDismissRequest = {verAlerta.value = false},
            title = {Text(text = "Perdiste")}, text = {Text(text = "Encontraste una mina")},
            confirmButton = { Button(onClick = {
                verAlerta.value = false
                verGanar.value = false
                estadoBotones.forEach{it.value = true}
                minas.forEach { it.value = asignaMina() }
            }) { Text(text = "Reiniciar juego")} })
        }

        if(verGanar.value){
            AlertDialog(onDismissRequest = {verGanar.value = false},
                title = {Text(text = "Ganaste")}, text = {Text(text = "Acabaste el tablero")},
                confirmButton = { Button(onClick = {
                    verAlerta.value = false
                    verGanar.value = false
                    estadoBotones.forEach{it.value = true}
                    minas.forEach { it.value = asignaMina() }
                }) { Text(text = "Reiniciar juego")} })
        }
    }

    fun mensaje() {
        Toast.makeText(this, "No mina", Toast.LENGTH_SHORT).show()
    }

    fun asignaMina() : Boolean {
        val numeroRandom = Random.nextInt(10)
        return (numeroRandom > 7)
    }
}