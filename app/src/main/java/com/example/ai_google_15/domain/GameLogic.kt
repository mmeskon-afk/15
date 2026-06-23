package com.example.ai_google_15.domain

import android.util.Log
import kotlinx.coroutines.*
import kotlin.math.abs

private const val TAG = "GameLogic"

// Выигрышное состояние
val WIN_STATE = (1..15).toList() + 0

// Получение соседей пустой ячейки
fun getNeighbors(emptyIdx: Int): List<Int> = listOfNotNull(
    if (emptyIdx >= 4) emptyIdx - 4 else null,
    if (emptyIdx < 12) emptyIdx + 4 else null,
    if (emptyIdx % 4 > 0) emptyIdx - 1 else null,
    if (emptyIdx % 4 < 3) emptyIdx + 1 else null
)

// Манхэттенское расстояние для эвристики
fun getManhattan(state: List<Int>): Int = state.indices.sumOf { i ->
    val v = state[i]
    if (v == 0) 0 else abs(i / 4 - (v - 1) / 4) + abs(i % 4 - (v - 1) % 4)
}

// Перемешивание поля (новая игра)
fun shuffleBoard(currentTiles: List<Int>, steps: Int = 40): List<Int> {
    Log.d(TAG, "shuffleBoard: starting shuffle from state=$currentTiles, steps=$steps")
    val newTiles = currentTiles.toMutableList()
    repeat(steps) {
        val empty = newTiles.indexOf(0)
        val neighbors = getNeighbors(empty)
        if (neighbors.isNotEmpty()) {
            val n = neighbors.random()
            newTiles[empty] = newTiles[n].also { newTiles[n] = 0 }
        }
    }
    Log.d(TAG, "shuffleBoard: result state=$newTiles, manhattan=${getManhattan(newTiles)}")
    return newTiles
}

// Совершение хода
fun makeMove(tiles: List<Int>, fromIdx: Int, toIdx: Int): List<Int> {
    val newTiles = tiles.toMutableList()
    newTiles[toIdx] = newTiles[fromIdx].also { newTiles[fromIdx] = 0 }
    return newTiles
}

// Легковесный класс для хранения узла в PriorityQueue
private data class Node(
    val state: List<Int>,
    val emptyIdx: Int,
    val g: Int,
    val f: Int
) : Comparable<Node> {
    override fun compareTo(other: Node): Int = this.f.compareTo(other.f)
}

// AI ход (возвращает следующее состояние поля на пути к выигрышу)
suspend fun getAIMove(currentTiles: List<Int>, winState: List<Int>): List<Int> = withContext(Dispatchers.Default) {
    Log.d(TAG, "getAIMove: start, manhattan=${getManhattan(currentTiles)}, tiles=$currentTiles")
    if (currentTiles == winState) {
        Log.d(TAG, "getAIMove: already solved")
        return@withContext currentTiles
    }

    val initialEmpty = currentTiles.indexOf(0)
    val queue = java.util.PriorityQueue<Node>()
    queue.add(Node(currentTiles, initialEmpty, 0, getManhattan(currentTiles)))

    val visited = mutableMapOf<List<Int>, Int>()
    visited[currentTiles] = 0

    val parentMap = mutableMapOf<List<Int>, List<Int>>()

    var iterations = 0
    var targetState: List<Int>? = null

    while (queue.isNotEmpty() && iterations < 3500) {
        iterations++
        val current = queue.poll()!!
        val currState = current.state

        if (currState == winState) {
            targetState = currState
            break
        }

        if (current.g >= 40) continue

        val empty = current.emptyIdx
        val nextG = current.g + 1

        for (nextIdx in getNeighbors(empty)) {
            val nextState = currState.toMutableList()
            nextState[empty] = nextState[nextIdx].also { nextState[nextIdx] = 0 }
            val nextStateList = nextState.toList()

            val prevG = visited[nextStateList]
            if (prevG == null || nextG < prevG) {
                visited[nextStateList] = nextG
                parentMap[nextStateList] = currState

                val f = nextG + getManhattan(nextStateList)
                queue.add(Node(nextStateList, nextIdx, nextG, f))
            }
        }
    }

    Log.d(TAG, "getAIMove: completed after $iterations iterations, visited=${visited.size} states")

    if (targetState != null || parentMap.containsKey(winState)) {
        var curr = targetState ?: winState
        val path = mutableListOf<List<Int>>()
        while (curr != currentTiles) {
            path.add(curr)
            curr = parentMap[curr] ?: break
        }
        if (path.isNotEmpty()) {
            Log.d(TAG, "getAIMove: optimal path found, path length=${path.size}")
            return@withContext path.last()
        }
    }

    Log.w(TAG, "getAIMove: no optimal path found, using greedy fallback")
    val emptyIdx = currentTiles.indexOf(0)
    val candidates = getNeighbors(emptyIdx).map { neighborIdx ->
        val nextState = makeMove(currentTiles, neighborIdx, emptyIdx)
        neighborIdx to getManhattan(nextState)
    }
    val bestNeighbor = candidates.minByOrNull { it.second }?.first
    return@withContext if (bestNeighbor != null) {
        Log.d(TAG, "getAIMove: greedy fallback, bestNeighbor=$bestNeighbor, manhattan=${candidates.minByOrNull { it.second }?.second}")
        makeMove(currentTiles, bestNeighbor, emptyIdx)
    } else {
        Log.w(TAG, "getAIMove: no valid moves available")
        currentTiles
    }
}
