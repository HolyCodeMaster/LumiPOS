package com.project.lumipos.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.lumipos.ui.theme.LumiPOSColors
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS,
    ERROR,
    INFO,
    WARNING
}

@Composable
fun Toast(
    message: String,
    type: ToastType = ToastType.INFO,
    visible: Boolean,
    onDismiss: () -> Unit
) {
    LaunchedEffect(visible) {
        if (visible) {
            delay(3000) // Auto-dismiss efter 3 sekunder
            onDismiss()
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (type) {
                        ToastType.SUCCESS -> LumiPOSColors.Grøn.copy(alpha = 0.95f)
                        ToastType.ERROR -> LumiPOSColors.Rød.copy(alpha = 0.95f)
                        ToastType.WARNING -> LumiPOSColors.Orange.copy(alpha = 0.95f)
                        ToastType.INFO -> LumiPOSColors.AccentBlå.copy(alpha = 0.95f)
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikon baseret på type
                    Text(
                        text = when (type) {
                            ToastType.SUCCESS -> "✓"
                            ToastType.ERROR -> "✕"
                            ToastType.WARNING -> "⚠"
                            ToastType.INFO -> "ℹ"
                        },
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    
                    // Besked
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Luk knap
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Toast state holder
 */
class ToastState {
    var message by mutableStateOf("")
        private set
    var type by mutableStateOf(ToastType.INFO)
        private set
    var visible by mutableStateOf(false)
        private set
    
    fun show(message: String, type: ToastType = ToastType.INFO) {
        this.message = message
        this.type = type
        this.visible = true
    }
    
    fun dismiss() {
        visible = false
    }
}

@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}

/**
 * Snackbar-style toast (bundsted)
 */
@Composable
fun SnackbarToast(
    message: String,
    type: ToastType = ToastType.INFO,
    visible: Boolean,
    onDismiss: () -> Unit
) {
    LaunchedEffect(visible) {
        if (visible) {
            delay(3000)
            onDismiss()
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Farvet indikator bar
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                when (type) {
                                    ToastType.SUCCESS -> LumiPOSColors.Grøn
                                    ToastType.ERROR -> LumiPOSColors.Rød
                                    ToastType.WARNING -> LumiPOSColors.Orange
                                    ToastType.INFO -> LumiPOSColors.AccentBlå
                                }
                            )
                    )
                    
                    // Ikon
                    Text(
                        text = when (type) {
                            ToastType.SUCCESS -> "✓"
                            ToastType.ERROR -> "✕"
                            ToastType.WARNING -> "⚠"
                            ToastType.INFO -> "ℹ"
                        },
                        fontSize = 20.sp,
                        color = when (type) {
                            ToastType.SUCCESS -> LumiPOSColors.Grøn
                            ToastType.ERROR -> LumiPOSColors.Rød
                            ToastType.WARNING -> LumiPOSColors.Orange
                            ToastType.INFO -> LumiPOSColors.AccentBlå
                        }
                    )
                    
                    // Besked
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
