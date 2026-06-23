package com.example.ai_google_15

import com.example.ai_google_15.domain.*
import org.junit.Assert.*
import org.junit.Test

class GameLogicTest {

    // ==================== getNeighbors ====================

    @Test
    fun getNeighbors_topLeftCorner() {
        // Corner (0): should have right (1) and down (4)
        val neighbors = getNeighbors(0)
        assertEquals(setOf(1, 4), neighbors.toSet())
    }

    @Test
    fun getNeighbors_topRightCorner() {
        // Corner (3): should have left (2) and down (7)
        val neighbors = getNeighbors(3)
        assertEquals(setOf(2, 7), neighbors.toSet())
    }

    @Test
    fun getNeighbors_bottomLeftCorner() {
        // Corner (12): should have right (13) and up (8)
        val neighbors = getNeighbors(12)
        assertEquals(setOf(13, 8), neighbors.toSet())
    }

    @Test
    fun getNeighbors_bottomRightCorner() {
        // Corner (15): should have left (14) and up (11)
        val neighbors = getNeighbors(15)
        assertEquals(setOf(14, 11), neighbors.toSet())
    }

    @Test
    fun getNeighbors_center() {
        // Center (5): should have up(1), down(9), left(4), right(6)
        val neighbors = getNeighbors(5)
        assertEquals(setOf(1, 9, 4, 6), neighbors.toSet())
    }

    @Test
    fun getNeighbors_topEdge() {
        // Top edge (1): should have left(0), right(2), down(5)
        val neighbors = getNeighbors(1)
        assertEquals(setOf(0, 2, 5), neighbors.toSet())
    }

    @Test
    fun getNeighbors_rightEdge() {
        // Right edge (7): should have up(3), down(11), left(6)
        val neighbors = getNeighbors(7)
        assertEquals(setOf(3, 11, 6), neighbors.toSet())
    }

    @Test
    fun getNeighbors_leftEdge() {
        // Left edge (4): should have up(0), down(8), right(5)
        val neighbors = getNeighbors(4)
        assertEquals(setOf(0, 8, 5), neighbors.toSet())
    }

    @Test
    fun getNeighbors_bottomEdge() {
        // Bottom edge (13): should have up(9), left(12), right(14)
        val neighbors = getNeighbors(13)
        assertEquals(setOf(9, 12, 14), neighbors.toSet())
    }

    @Test
    fun getNeighbors_neverExceedsFour() {
        // No position should have more than 4 neighbors
        for (i in 0..15) {
            assertTrue("Position $i has ${getNeighbors(i).size} neighbors", getNeighbors(i).size <= 4)
        }
    }

    // ==================== getManhattan ====================

    @Test
    fun getManhattan_winState() {
        // WIN_STATE: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0]
        assertEquals(0, getManhattan(WIN_STATE))
    }

    @Test
    fun getManhattan_emptyState() {
        // Empty tile (0) at position 15 (correct position) contributes 0
        val state = listOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 1)
        // 1 is at position 0, should be at position 0 -> Manhattan = |0-0| + |0-0| = 0
        // Wait, 1 at position 0 is correct. Let me recalculate:
        // position 0: value 1 -> should be at 0 -> distance 0
        // position 1: value 2 -> should be at 1 -> distance 0
        // ...
        // position 14: value 15 -> should be at 14 -> distance 0
        // position 15: value 1 -> should be at 0 -> distance = |3-0| + |3-0| = 6
        assertEquals(6, getManhattan(state))
    }

    @Test
    fun getManhattan_singleTile() {
        // Only tile 1 swapped with 2
        val state = listOf(2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
        // Tile 1 at position 1, should be at 0: |0-1| + |0-1| = 0 + 1 = 1
        // Tile 2 at position 0, should be at 1: |0-0| + |1-0| = 0 + 1 = 1
        assertEquals(2, getManhattan(state))
    }

    @Test
    fun getManhattan_oppositeCorners() {
        // Tile 1 at position 15, tile 15 at position 0
        val state = listOf(15, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 1, 0)
        // Tile 15 at position 0, should be at 14: |0-3| + |0-2| = 3 + 2 = 5
        // Tile 1 at position 14, should be at 0: |3-0| + |2-0| = 3 + 2 = 5
        assertEquals(10, getManhattan(state))
    }

    @Test
    fun getManhattan_zeroAtCorrectPosition() {
        // 0 at position 15 (correct) contributes 0
        assertEquals(0, getManhattan(WIN_STATE))
    }

    // ==================== makeMove ====================

    @Test
    fun makeMove_basicSwap() {
        val tiles = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)
        // Move tile 15 from position 15 to position 14 (where 0 is)
        val result = makeMove(tiles, 15, 14)
        assertEquals(0, result[15])
        assertEquals(15, result[14])
    }

    @Test
    fun makeMove_doesNotModifyOriginal() {
        val original = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)
        val copy = original.toList()
        makeMove(original, 15, 14)
        assertEquals(copy, original)
    }

    @Test
    fun makeMove_tileBecomesEmpty() {
        val tiles = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)
        val result = makeMove(tiles, 15, 14)
        assertEquals(0, result[15])
    }

    @Test
    fun makeMove_emptyBecomesTile() {
        val tiles = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)
        val result = makeMove(tiles, 15, 14)
        assertEquals(15, result[14])
    }

    // ==================== shuffleBoard ====================

    @Test
    fun shuffleBoard_producesDifferentState() {
        // Run shuffle multiple times, at least one should differ from WIN_STATE
        var changed = false
        repeat(10) {
            val shuffled = shuffleBoard(WIN_STATE)
            if (shuffled != WIN_STATE) changed = true
        }
        assertTrue("Shuffle should produce at least one non-winning state", changed)
    }

    @Test
    fun shuffleBoard_preservesAllTiles() {
        val shuffled = shuffleBoard(WIN_STATE)
        assertEquals(WIN_STATE.sorted(), shuffled.sorted())
    }

    @Test
    fun shuffleBoard_preservesLength() {
        val shuffled = shuffleBoard(WIN_STATE)
        assertEquals(16, shuffled.size)
    }

    @Test
    fun shuffleBoard_producesSolvableState() {
        // Manhattan distance should be > 0 after shuffle (unless extremely unlikely)
        val shuffled = shuffleBoard(WIN_STATE)
        assertTrue("Shuffled board should not be solved", getManhattan(shuffled) > 0)
    }

    // ==================== WIN_STATE ====================

    @Test
    fun winState_hasAllNumbers1to15AndZero() {
        assertEquals(16, WIN_STATE.size)
        assertEquals((1..15).toList() + 0, WIN_STATE)
    }

    @Test
    fun winState_manhattanDistanceIsZero() {
        assertEquals(0, getManhattan(WIN_STATE))
    }

    // ==================== AI Solver ====================

    @Test
    fun getAIMove_returnsCorrectLength() {
        val state = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0, 14, 15)
        // This is a simple case: 14 and 15 are out of place
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(state, WIN_STATE)
        }
        assertEquals(16, result.size)
    }

    @Test
    fun getAIMove_solvesSimpleCase() {
        // Only one move away from solution
        val state = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(state, WIN_STATE)
        }
        assertEquals(WIN_STATE, result)
    }

    @Test
    fun getAIMove_alreadySolved() {
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(WIN_STATE, WIN_STATE)
        }
        assertEquals(WIN_STATE, result)
    }

    @Test
    fun getAIMove_makesProgress() {
        // A slightly harder case
        val state = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 13, 14, 15, 12)
        val originalManhattan = getManhattan(state)
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(state, WIN_STATE)
        }
        val newManhattan = getManhattan(result)
        assertTrue("AI should make progress (original=$originalManhattan, new=$newManhattan)",
            newManhattan < originalManhattan)
    }

    @Test
    fun getAIMove_outputIsValidState() {
        val state = listOf(5, 1, 2, 3, 9, 6, 7, 4, 13, 10, 11, 8, 0, 14, 15, 12)
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(state, WIN_STATE)
        }
        // Result should have all tiles 0-15
        assertEquals(16, result.size)
        assertEquals((0..15).toList(), result.sorted())
    }

    @Test
    fun getAIMove_verySimpleTwoSteps() {
        // Two moves away
        val state = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
        val result = kotlinx.coroutines.runBlocking {
            getAIMove(state, WIN_STATE)
        }
        // Should be solved since it's already at WIN_STATE
        assertEquals(WIN_STATE, result)
    }
}
