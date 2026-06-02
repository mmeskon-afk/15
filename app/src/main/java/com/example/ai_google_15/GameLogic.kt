package com.example.ai_google_15

import kotlinx.coroutines.*
import kotlin.math.abs

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
fun shuffleBoard(currentTiles: List<Int>): List<Int> {
    val newTiles = currentTiles.toMutableList()
    repeat(40) {
        val empty = newTiles.indexOf(0)
        val neighbors = getNeighbors(empty)
        if (neighbors.isNotEmpty()) {
            val n = neighbors.random()
            newTiles[empty] = newTiles[n].also { newTiles[n] = 0 }
        }
    }
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
    val g: Int, // количество сделанных шагов от старта
    val f: Int  // общая оценка (g + h)
) : Comparable<Node> {
    override fun compareTo(other: Node): Int = this.f.compareTo(other.f)
}

// AI ход (возвращает следующее состояние поля на пути к выигрышу)
suspend fun getAIMove(currentTiles: List<Int>, winState: List<Int>): List<Int> = withContext(Dispatchers.Default) {
    if (currentTiles == winState) return@withContext currentTiles

    val initialEmpty = currentTiles.indexOf(0)
    val queue = java.util.PriorityQueue<Node>()
    queue.add(Node(currentTiles, initialEmpty, 0, getManhattan(currentTiles)))

    // Храним только посещенные состояния и глубину, на которой их встретили
    val visited = mutableMapOf<List<Int>, Int>()
    visited[currentTiles] = 0

    // Карта для восстановления пути: Ключ = состояние, Значение = его родитель
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

        // Ограничиваем максимальную глубину поиска во избежание зависаний
        if (current.g >= 40) continue

        val empty = current.emptyIdx
        val nextG = current.g + 1

        for (nextIdx in getNeighbors(empty)) {
            // Быстро создаем новое состояние, зная индексы для перестановки
            val nextState = currState.toMutableList()
            nextState[empty] = nextState[nextIdx].also { nextState[nextIdx] = 0 }
            val nextStateList = nextState.toList()

            // Проверяем, не посещали ли мы это состояние ранее с меньшей стоимостью
            val prevG = visited[nextStateList]
            if (prevG == null || nextG < prevG) {
                visited[nextStateList] = nextG
                parentMap[nextStateList] = currState

                val f = nextG + getManhattan(nextStateList)
                queue.add(Node(nextStateList, nextIdx, nextG, f))
            }
        }
    }

    // Если путь найден, восстанавливаем его по цепочке родителей назад до самого первого шага
    if (targetState != null || parentMap.containsKey(winState)) {
        var curr = targetState ?: winState
        val path = mutableListOf<List<Int>>()
        while (curr != currentTiles) {
            path.add(curr)
            curr = parentMap[curr] ?: break
        }
        if (path.isNotEmpty()) {
            return@withContext path.last() // Самый первый шаг от текущего положения к победе
        }
    }

    // Резервный вариант (fallback): если за 3500 итераций решение не найдено, делаем один лучший жадный ход
    val emptyIdx = currentTiles.indexOf(0)
    val candidates = getNeighbors(emptyIdx).map { neighborIdx ->
        val nextState = makeMove(currentTiles, neighborIdx, emptyIdx)
        neighborIdx to getManhattan(nextState)
    }
    val bestNeighbor = candidates.minByOrNull { it.second }?.first
    return@withContext if (bestNeighbor != null) {
        makeMove(currentTiles, bestNeighbor, emptyIdx)
    } else {
        currentTiles
    }
}
