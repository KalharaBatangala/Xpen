package com.example.xpen.ui.entry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xpen.R
import com.example.xpen.XpenApplication
import com.example.xpen.ui.components.NumberPad
import kotlinx.coroutines.delay

@Composable
fun EntryScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as XpenApplication
    val viewModel: EntryViewModel = viewModel(
        factory = EntryViewModelFactory(app.repository)
    )

    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("F") }
    var showSavedBanner by remember { mutableStateOf(false) }

    LaunchedEffect(showSavedBanner) {
        if (showSavedBanner) {
            delay(1000)
            showSavedBanner = false
        }
    }

    fun appendDigit(d: String) {
        if (amountText.length < 10) amountText += d
    }
    fun appendDecimal() {
        if (!amountText.contains(".")) {
            amountText = if (amountText.isEmpty()) "0." else "$amountText."
        }
    }
    fun backspace() {
        if (amountText.isNotEmpty()) amountText = amountText.dropLast(1)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Image(
                painter = painterResource(id = R.drawable.xpen_logo_transparent),
                contentDescription = "Xpen logo",
                modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(64.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (amountText.isEmpty()) "Rs: 0.00" else "Rs: $amountText",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                CategoryButton("F", selectedCategory == "F") { selectedCategory = "F" }
                CategoryButton("T", selectedCategory == "T") { selectedCategory = "T" }
                CategoryButton("O", selectedCategory == "O") { selectedCategory = "O" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.saveExpense(amount, selectedCategory) {
                            showSavedBanner = true
                        }
                        amountText = ""
                        selectedCategory = "F"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Enter", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            NumberPad(
                onDigit = ::appendDigit,
                onDecimal = ::appendDecimal,
                onBackspace = ::backspace,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        AnimatedVisibility(
            visible = showSavedBanner,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -20 }),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 96.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1B5E20), shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "✅  Saved",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun RowScope.CategoryButton(code: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                when (code) {
                    "F" -> Color(0xFF4CAF50)
                    "T" -> Color(0xFF2196F3)
                    else -> Color(0xFFFF9800)
                }
            } else Color.LightGray
        )
    ) {
        Text(code, fontWeight = FontWeight.Bold)
    }
}