package com.example.ai_google_15.ui.component

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_google_15.ui.theme.Dimens
import com.example.ai_google_15.ui.theme.TextOnGold
import com.example.ai_google_15.ui.theme.TileGold
import kotlin.math.abs
import kotlin.math.roundToInt

private const val TAG = "GameBoard"

/**
 * Адаптивное игровое поле
 * - Поддерживает все ориентации (portrait/landscape)
 * - Автоматически подстраивается под размер экрана
 * - Работает на устройствах с любым соотношением сторон
 */
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

    // Адаптивный размер доски: максимум 360dp, минимум 280dp
    var boardSizePx by remember { mutableStateOf(0f) }
    var boardSizeDp by remember { mutableStateOf(Dimens.MaxBoardSize) }

    // Рассчитываем размер одной плитки
    val tileSizePx = if (boardSizePx > 0f) boardSizePx / 4f else 1f

    // Состояния для свайпов
    var dragAmountX by remember { mutableStateOf(0f) }
    var dragAmountY by remember { mutableStateOf(0f) }

    var activeTileIndex by remember { mutableStateOf(-1) }
    var emptyTileIndex by remember { mutableStateOf(-1) }
    var initialSwipeDirection by remember { mutableStateOf("") }

    // Адаптивный модификатор для доски
    val boardModifier = if (isLandscape) {
        Modifier
            .aspectRatio(Dimens.BoardAspectRatio)
            .padding(Dimens.PaddingXS)
    } else {
        Modifier
            .sizeIn(maxWidth = Dimens.MaxBoardSize, maxHeight = Dimens.MaxBoardSize)
            .sizeIn(minWidth = Dimens.MinBoardSize, minHeight = Dimens.MinBoardSize)
    }

    Box(
        modifier = boardModifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.CornerMedium))
            .padding(Dimens.BoardPadding)
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
                                Log.d(TAG, "Swipe detected: direction=$initialSwipeDirection, totalDrag=${totalDrag.roundToInt()}px")
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

                    // Адаптивный размер текста в зависимости от размера доски
                    val fontSize = when {
                        boardSizeDp < 260.dp -> 16.sp
                        boardSizeDp < 300.dp -> 18.sp
                        else -> 22.sp
                    }

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
                            .padding(Dimens.TilePadding)
                            .clip(RoundedCornerShape(Dimens.CornerSmall))
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$n",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isCorrect || isGold) FontWeight.Bold else FontWeight.Medium,
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
