package com.example.ai_google_15

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_google_15.ui.theme.TileGold
import com.example.ai_google_15.ui.theme.TextOnGold
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun GameBoard(
    tiles: List<Int>,
    isThinking: Boolean,
    isVictory: Boolean,
    goldTilesCount: Int,
    onTileClick: (Int) -> Unit,
    onSwipe: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val density = LocalDensity.current

    var boardSizePx by remember { mutableStateOf(0f) }
    var boardSizeDp by remember { mutableStateOf(320.dp) }

    val tileSizePx = if (boardSizePx > 0f) boardSizePx / 4f else 1f

    var dragAmountX by remember { mutableStateOf(0f) }
    var dragAmountY by remember { mutableStateOf(0f) }

    var activeTileIndex by remember { mutableStateOf(-1) }
    var emptyTileIndex by remember { mutableStateOf(-1) }
    var initialSwipeDirection by remember { mutableStateOf("") }

    // ИСПРАВЛЕНО: Для ландшафта убран fillMaxHeight(), оставлен только aspectRatio
    val boardModifier = if (isLandscape) {
        Modifier
            .aspectRatio(1f)
            .padding(4.dp)
    } else {
        Modifier.size(320.dp)
    }

    Box(
        modifier = boardModifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(4.dp)
            .onGloballyPositioned { coordinates ->
                val size = coordinates.size.width.toFloat()
                if (size > 0f && boardSizePx != size) {
                    boardSizePx = size
                    boardSizeDp = with(density) { size.toDp() }
                }
            }
            .pointerInput(tiles, isThinking, isVictory, boardSizePx) {
                if (isThinking || isVictory || boardSizePx <= 0f) return@pointerInput

                detectDragGestures(
                    onDragStart = { startOffset ->
                        dragAmountX = 0f
                        dragAmountY = 0f
                        initialSwipeDirection = ""

                        val col = (startOffset.x / tileSizePx).toInt().coerceIn(0, 3)
                        val row = (startOffset.y / tileSizePx).toInt().coerceIn(0, 3)
                        val clickedIdx = row * 4 + col

                        val emptyIdx = tiles.indexOf(0)
                        val isRealNeighbor = abs(clickedIdx / 4 - emptyIdx / 4) + abs(clickedIdx % 4 - emptyIdx % 4) == 1

                        if (isRealNeighbor && tiles[clickedIdx] != 0) {
                            activeTileIndex = clickedIdx
                            emptyTileIndex = emptyIdx
                        } else {
                            activeTileIndex = -1
                            emptyTileIndex = -1
                        }
                    },
                    onDrag = { change, drag ->
                        change.consume()
                        if (activeTileIndex == -1 || emptyTileIndex == -1) return@detectDragGestures

                        if (initialSwipeDirection.isEmpty() && (abs(dragAmountX) > 10f || abs(dragAmountY) > 10f)) {
                            initialSwipeDirection = if (abs(dragAmountX) > abs(dragAmountY)) {
                                if (dragAmountX > 0) "right" else "left"
                            } else {
                                if (dragAmountY > 0) "down" else "up"
                            }
                        }

                        val activeRow = activeTileIndex / 4
                        val activeCol = activeTileIndex % 4
                        val emptyRow = emptyTileIndex / 4
                        val emptyCol = emptyTileIndex % 4

                        if (activeRow == emptyRow) {
                            dragAmountX += drag.x
                            if (emptyCol > activeCol) {
                                dragAmountX = dragAmountX.coerceIn(0f, tileSizePx)
                            } else {
                                dragAmountX = dragAmountX.coerceIn(-tileSizePx, 0f)
                            }
                            dragAmountY = 0f
                        } else if (activeCol == emptyCol) {
                            dragAmountY += drag.y
                            if (emptyRow > activeRow) {
                                dragAmountY = dragAmountY.coerceIn(0f, tileSizePx)
                            } else {
                                dragAmountY = dragAmountY.coerceIn(-tileSizePx, 0f)
                            }
                            dragAmountX = 0f
                        }
                    },
                    onDragEnd = {
                        if (activeTileIndex != -1 && emptyTileIndex != -1) {
                            val totalDrag = if (dragAmountX != 0f) abs(dragAmountX) else abs(dragAmountY)
                            if (totalDrag > tileSizePx / 2 && initialSwipeDirection.isNotEmpty()) {
                                onSwipe(initialSwipeDirection)
                            }
                        }
                        activeTileIndex = -1
                        emptyTileIndex = -1
                        dragAmountX = 0f
                        dragAmountY = 0f
                        initialSwipeDirection = ""
                    }
                )
            }
    ) {
        if (boardSizePx > 0f) {
            tiles.forEachIndexed { index, n ->
                if (n != 0) {
                    val isCorrect = index == (n - 1)
                    val isGold = n <= goldTilesCount

                    val backgroundColor by animateColorAsState(
                        targetValue = if (isGold) TileGold else MaterialTheme.colorScheme.secondaryContainer,
                        animationSpec = tween(durationMillis = 300),
                        label = "tileBackground"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isGold) TextOnGold else MaterialTheme.colorScheme.onSecondaryContainer,
                        animationSpec = tween(durationMillis = 300),
                        label = "tileText"
                    )

                    val baseRow = index / 4
                    val baseCol = index % 4

                    val singleTileSizeDp = boardSizeDp / 4
                    val baseOffsetX = baseCol * singleTileSizeDp.value
                    val baseOffsetY = baseRow * singleTileSizeDp.value

                    val fontSize = if (boardSizeDp < 260.dp) 16.sp else 22.sp

                    Box(
                        modifier = Modifier
                            .offset {
                                if (index == activeTileIndex) {
                                    IntOffset(dragAmountX.roundToInt(), dragAmountY.roundToInt())
                                } else {
                                    IntOffset(0, 0)
                                }
                            }
                            .padding(start = baseOffsetX.dp, top = baseOffsetY.dp)
                            .size(singleTileSizeDp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$n",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isCorrect || isGold) FontWeight.ExtraBold else FontWeight.Bold,
                                fontSize = fontSize
                            ),
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
