package com.example.ai_google_15.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_google_15.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "GameViewModel"

class GameViewModel : ViewModel() {
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

    fun initGame(difficulty: String) {
        if (isInitialized) {
            Log.d(TAG, "initGame: already initialized, skipping")
            return
        }
        isInitialized = true

        shuffleSteps = when (difficulty) {
            "Легкий" -> 50
            "Сложный" -> 200
            else -> 100
        }
        Log.i(TAG, "initGame: difficulty=$difficulty, shuffleSteps=$shuffleSteps")
        resetGame()
    }

    fun resetGame() {
        Log.d(TAG, "resetGame: resetting game state")
        isVictory = false
        isThinking = false
        solvedByAI = false
        moveCount = 0
        goldTilesCount = 0
        tiles = shuffleBoard(WIN_STATE, shuffleSteps)
        Log.d(TAG, "resetGame: new tiles=$tiles, manhattan=${getManhattan(tiles)}")
    }

    fun performAIMove() {
        if (isThinking || isVictory) {
            Log.d(TAG, "performAIMove: blocked (isThinking=$isThinking, isVictory=$isVictory)")
            return
        }
        Log.i(TAG, "performAIMove: requesting AI move")
        isThinking = true
        solvedByAI = true
        viewModelScope.launch {
            val newTiles = getAIMove(tiles, WIN_STATE)
            if (newTiles != tiles) {
                tiles = newTiles
                moveCount++
                Log.d(TAG, "performAIMove: AI move applied, moveCount=$moveCount, manhattan=${getManhattan(tiles)}")
            } else {
                Log.w(TAG, "performAIMove: AI returned same state, no progress")
            }
            if (tiles == WIN_STATE) triggerVictorySequence()
            isThinking = false
        }
    }

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
            Log.d(TAG, "handleSwipe: direction=$direction, moveCount=$moveCount, manhattan=${getManhattan(tiles)}")
            if (tiles == WIN_STATE) triggerVictorySequence()
        } else {
            Log.d(TAG, "handleSwipe: direction=$direction blocked at edge (emptyIdx=$emptyIndex)")
        }
    }

    private fun triggerVictorySequence() {
        Log.i(TAG, "triggerVictorySequence: victory! moveCount=$moveCount, solvedByAI=$solvedByAI")
        isVictory = true
        viewModelScope.launch {
            for (i in 1..15) {
                delay(500)
                goldTilesCount = i
            }
        }
    }
}
