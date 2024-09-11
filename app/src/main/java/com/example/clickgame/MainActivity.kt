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
import androidx.compose.ui.unit.dp
import com.example.clickgame.ui.theme.ClickgameTheme
import androidx.compose.ui.platform.LocalContext
import com.example.clickgame.data.*
import com.example.clickgame.data.GameState

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
    var gameState by remember { mutableStateOf(GameState()) }
    var listaDeImagens by remember { mutableStateOf(tipos.random()) }

    val context = LocalContext.current

    val atualizarImagem = {
        val proporcao = gameState.cliques.toFloat() / gameState.numeroObjetivo
        val indexImagem = when {
            proporcao < 0.33f -> 0
            proporcao < 0.66f -> 1
            proporcao < 1f -> 2
            else -> {
                gameState = gameState.copy(chegouUltimo = true)
                3
            }
        }
        gameState = gameState.copy(imagemAtual = listaDeImagens[indexImagem])
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (gameState.jogoFinalizado) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(gameState.imagemAtual), contentDescription = null)
                if (gameState.chegouUltimo) {
                    Text(text = "Você chegou no último. Parabéns!")
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(text = "Novo jogo?")
                Row {
                    Button(onClick = {
                        gameState = GameState()
                        listaDeImagens = tipos.random()
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
                        gameState = gameState.copy(cliques = gameState.cliques + 1)
                        atualizarImagem()
                        if (gameState.cliques >= gameState.numeroObjetivo) {
                            gameState = gameState.copy(jogoFinalizado = true)
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(gameState.imagemAtual), contentDescription = null)
            }
        }

        Button(
            onClick = {
                gameState = gameState.copy(jogoFinalizado = true, desistiu = true, imagemAtual = R.drawable.image_desistencia)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Desistir")
        }
    }
}