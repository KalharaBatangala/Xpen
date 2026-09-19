package com.example.xpen.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpen.R
import kotlinx.coroutines.delay

private val NavyEdge = Color(0xFF04051D)
private val NavyCenter = Color(0xFF0A1443)

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(NavyCenter, NavyEdge),
                    center = Offset.Unspecified,
                    radius = 1200f
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.xpen_logo_transparent),
            contentDescription = "Xpen logo",
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.Center)
        )
        Text(
            text = "Powered by | Kalhara Batangala",
            color = Color(0xFFC9A227), // muted gold, matches logo tone
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}