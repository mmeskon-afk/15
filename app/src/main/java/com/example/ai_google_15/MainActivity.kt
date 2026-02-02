package com.example.ai_google_15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.*
import com.example.ai_google_15.ui.theme.AI_google_15Theme
import kotlin.math.abs
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AI_google_15Theme { Surface(Modifier.fillMaxSize()) { GameFifteen() } } }
    }
}
@Composable
fun GameFifteen() {
    val win = remember { (1..15).toList() + 0 }
    var tiles by remember { mutableStateOf(win) }
    var isVictory by remember { mutableStateOf(false) }

    fun move(idx: Int, force: Boolean = false) {
        val empty = tiles.indexOf(0)
        if (force || (abs(idx / 4 - empty / 4) + abs(idx % 4 - empty % 4) == 1)) {
            tiles = tiles.toMutableList().apply { this[empty] = this[idx].also { this[idx] = 0 } }
            if (!force && tiles == win) isVictory = true
        }
    }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Пятнашки", style = MaterialTheme.typography.displayLarge, modifier = Modifier.padding(bottom = 32.dp))
        Box(Modifier.size(320.dp).background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)).padding(4.dp)) {
            LazyVerticalGrid(GridCells.Fixed(4), userScrollEnabled = false) {
                itemsIndexed(tiles, key = { _, it -> it }) { i, n ->
                    if (n != 0) Box(
                        Modifier.padding(4.dp).aspectRatio(1f).clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable { move(i) }.animateItem(),
                        Alignment.Center
                    ) { Text("$n", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge) }
                }
            }
        }
        Button({
            repeat(150) {
                val empty = tiles.indexOf(0)
                val neighbors = listOfNotNull(
                    if (empty >= 4) empty - 4 else null, if (empty < 12) empty + 4 else null,
                    if (empty % 4 > 0) empty - 1 else null, if (empty % 4 < 3) empty + 1 else null
                ).random()
                move(neighbors, true)
            }
            isVictory = false
        }, Modifier.padding(top = 32.dp).fillMaxWidth(0.6f).height(50.dp)) { Text("Перемешать") }
        if (isVictory) AlertDialog(
            onDismissRequest = { isVictory = false },
            confirmButton = { TextButton({ isVictory = false }) { Text("OK") } },
            title = { Text("Победа!") }, text = { Text("Вы собрали пазл!") }
        )
    }
}