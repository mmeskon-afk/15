package com.example.ai_google_15

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    // Реактивные переменные состояния игры
    var tiles by mutableStateOf(WIN_STATE)
        private set

    var isVictory by mutableStateOf(false)
        private set

    var isThinking by mutableStateOf(false)
        private set

    var solvedByAI by mutableStateOf(false)
        private set

    var moveCount by mutableStateOf(0)
        private set

    var goldTilesCount by mutableStateOf(0)
        private set

    private var shuffleSteps = 40
    private var isInitialized = false

    // Запуск новой игры при первом открытии экрана
    fun initGame(difficulty: String) {
        if (isInitialized) return
        isInitialized = true

        shuffleSteps = when (difficulty) {
            "Легкий" -> 15
            "Сложный" -> 100
            else -> 40
        }
        resetGame()
    }

    // Перемешивание поля
    fun resetGame() {
        isVictory = false
        isThinking = false
        solvedByAI = false
        moveCount = 0
        goldTilesCount = 0
        tiles = shuffleBoardWithSteps(WIN_STATE, shuffleSteps)
    }

    // Подсказка и ход искусственного интеллекта
    fun performAIMove() {
        if (isThinking || isVictory) return
        isThinking = true
        solvedByAI = true
        viewModelScope.launch {
            val newTiles = getAIMove(tiles, WIN_STATE)
            if (newTiles != tiles) {
                tiles = newTiles
                moveCount++
            }
            if (tiles == WIN_STATE) triggerVictorySequence()
            isThinking = false
        }
    }

    // Физический свайп плитки игроком
    fun handleSwipe(direction: String) {
        if (isThinking || isVictory) return
        val emptyIndex = tiles.indexOf(0)
        val row = emptyIndex / 4
        val col = emptyIndex % 4
        val targetIndex = when (direction) {
            "left"  -> if (col < 3) emptyIndex + 1 else -1
            "right" -> if (col > 0) emptyIndex - 1 else -1
            "up"    -> if (row < 3) emptyIndex + 4 else -1
            "down"  -> if (row > 0) emptyIndex - 4 else -1
            else    -> -1
        }
        if (targetIndex != -1) {
            solvedByAI = false
            tiles = makeMove(tiles, targetIndex, emptyIndex)
            moveCount++
            if (tiles == WIN_STATE) triggerVictorySequence()
        }
    }

    // Анимация золотой волны при победе
    private fun triggerVictorySequence() {
        isVictory = true
        viewModelScope.launch {
            for (i in 1..15) {
                delay(500)
                goldTilesCount = i
            }
        }
    }

    // ИСПРАВЛЕНО: Функция теперь объявлена внутри класса ViewModel и доступна для вызовов выше
    private fun shuffleBoardWithSteps(currentTiles: List<Int>, steps: Int): List<Int> {
        val newTiles = currentTiles.toMutableList()
        repeat(steps) {
            val empty = newTiles.indexOf(0)
            val neighbors = getNeighbors(empty)
            if (neighbors.isNotEmpty()) {
                val n = neighbors.random()
                newTiles[empty] = newTiles[n].also { newTiles[n] = 0 }
            }
        }
        return newTiles
    }
}
