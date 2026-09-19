package com.example.xpen.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val keyRows = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf(".", "0", "⌫")
)

@Composable
fun NumberPad(
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keyRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { key ->
                    OutlinedButton(
                        onClick = {
                            when (key) {
                                "." -> onDecimal()
                                "⌫" -> onBackspace()
                                else -> onDigit(key)
                            }
                        },
                        modifier = Modifier.weight(1f).height(64.dp)
                    ) {
                        Text(key, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}