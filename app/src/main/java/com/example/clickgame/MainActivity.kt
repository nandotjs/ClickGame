package com.example.clickgame

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clickgame.ui.theme.ClickgameTheme
import kotlin.random.Random
import androidx.compose.ui.platform.LocalContext
import com.example.clickgame.data.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickgameTheme(darkTheme = true) {
                JogoDeCliques()
            }
        }
    }
}

@Composable
fun JogoDeCliques() {
    var cliques by remember { mutableStateOf(0) }
    var numeroObjetivo by remember { mutableStateOf(Random.nextInt(5, 20)) }
    var listaDeImagens by remember { mutableStateOf(tipos.random()) }
    var imagemAtual by remember { mutableStateOf(listaDeImagens[0]) }
    var jogoFinalizado by remember { mutableStateOf(false) }
    var desistiu by remember { mutableStateOf(false) }
    var chegouUltimo by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val atualizarImagem = {
        val proporcao = cliques.toFloat() / numeroObjetivo
        val indexImagem = when {
            proporcao < 0.33f -> 0
            proporcao < 0.66f -> 1
            proporcao < 1f -> 2
            else -> {
                chegouUltimo = true
                3
            }
        }
        imagemAtual = listaDeImagens[indexImagem]
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (jogoFinalizado) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(imagemAtual), contentDescription = null)
                if (chegouUltimo) {
                    Text(text = "Você chegou no último. Parabéns!")
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(text = "Novo jogo?")
                Row {
                    Button(onClick = {
                        cliques = 0
                        numeroObjetivo = Random.nextInt(5, 20)
                        listaDeImagens = tipos.random()
                        imagemAtual = listaDeImagens[0]
                        jogoFinalizado = false
                        desistiu = false
                        chegouUltimo = false
                    }) {
                        Text(text = "Sim")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        (context as? Activity)?.finish()
                    }) {
                        Text(text = "Não")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        cliques++
                        atualizarImagem()
                        if (cliques >= numeroObjetivo) {
                            jogoFinalizado = true
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(imagemAtual), contentDescription = null)
            }
        }

        Button(
            onClick = {
                jogoFinalizado = true
                desistiu = true
                imagemAtual = R.drawable.image_desistencia
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Desistir")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ClickgameTheme {
        JogoDeCliques()
    }
}
