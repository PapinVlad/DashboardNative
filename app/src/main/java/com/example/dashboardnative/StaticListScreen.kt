package com.example.dashboardnative

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

// A45 step 1: planned row layout (D1). Same deterministic data and formatting as the RN app.
// Integer arithmetic only, so both apps format identical strings.

data class Asset(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceCents: Int,
    val changeBp: Int, // percentage change in basis points (1 bp = 0.01 %)
)

private const val N = 500

private val DATA: List<Asset> = List(N) { i ->
    Asset(
        id = i,
        name = "Asset $i",
        symbol = "A" + i.toString().padStart(3, '0'),
        priceCents = 10000 + (i * 3737) % 90000,
        changeBp = (i * 1313) % 4100 - 2000,
    )
}

private fun pad2(n: Int) = if (n < 10) "0$n" else n.toString()

private fun formatPrice(cents: Int) = "${cents / 100}.${pad2(cents % 100)}"

private fun formatChange(bp: Int): String {
    val a = abs(bp)
    val sign = if (bp > 0) "+" else if (bp < 0) "-" else ""
    return "$sign${a / 100}.${pad2(a % 100)}%"
}

private fun arrow(bp: Int) = if (bp > 0) "▲" else if (bp < 0) "▼" else "–"

private fun changeColor(bp: Int) =
    if (bp > 0) Color(0xFF1B7F3B) else if (bp < 0) Color(0xFFC62828) else Color(0xFF666666)

private val NameStyle = TextStyle(fontSize = 16.sp, lineHeight = 20.sp, color = Color(0xFF000000))
private val SymbolStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, color = Color(0xFF666666))
private val PriceStyle = TextStyle(fontSize = 16.sp, lineHeight = 20.sp, color = Color(0xFF000000))
private val ChangeStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp)

@Composable
private fun AssetRow(item: Asset) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(item.name, style = NameStyle)
            Text(item.symbol, style = SymbolStyle)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatPrice(item.priceCents), style = PriceStyle)
            Text(
                "${arrow(item.changeBp)} ${formatChange(item.changeBp)}",
                style = ChangeStyle.copy(color = changeColor(item.changeBp)),
            )
        }
    }
}

@Composable
fun StaticListScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().testTag("asset_list")) {
            items(DATA, key = { it.id }) { item -> AssetRow(item) }
        }
    }
}
