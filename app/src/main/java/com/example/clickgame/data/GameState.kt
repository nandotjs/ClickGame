package com.example.clickgame.data

import com.example.clickgame.R
import kotlin.random.Random

data class GameState(
    var cliques: Int = 0,
    var numeroObjetivo: Int = Random.nextInt(5, 20),
    var imagemAtual: Int = R.drawable.image_egg,
    var jogoFinalizado: Boolean = false,
    var desistiu: Boolean = false,
    var chegouUltimo: Boolean = false
)
