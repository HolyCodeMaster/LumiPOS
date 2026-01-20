package com.project.lumipos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * LumiPOS Custom Icons - Geometrik şekiller ile çizilmiş ikonlar
 * Emoji/Unicode problemlerini çözmek için Canvas ve Box kullanımı
 */

@Composable
fun ClockIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = size.toPx() / 2
        val center = Offset(radius, radius)
        
        // Dış daire
        drawCircle(
            color = color,
            radius = radius * 0.85f,
            center = center,
            style = Stroke(width = 2f)
        )
        
        // Saat ibresi (kısa)
        drawLine(
            color = color,
            start = center,
            end = Offset(center.x, center.y - radius * 0.4f),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
        
        // Dakika ibresi (uzun)
        drawLine(
            color = color,
            start = center,
            end = Offset(center.x + radius * 0.5f, center.y - radius * 0.2f),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CheckIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val path = Path().apply {
            moveTo(size.toPx() * 0.2f, size.toPx() * 0.5f)
            lineTo(size.toPx() * 0.45f, size.toPx() * 0.75f)
            lineTo(size.toPx() * 0.85f, size.toPx() * 0.25f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ProgressCircleIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = size.toPx() / 2
        
        // Dış halka
        drawCircle(
            color = color,
            radius = radius * 0.8f,
            center = Offset(radius, radius),
            style = Stroke(width = 2.5f)
        )
        
        // İç nokta
        drawCircle(
            color = color,
            radius = radius * 0.35f,
            center = Offset(radius, radius)
        )
    }
}

@Composable
fun BoxIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val boxSize = size.toPx() * 0.8f
        val offset = size.toPx() * 0.1f
        
        // Kutu ana gövde
        drawRect(
            color = color,
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(boxSize, boxSize),
            style = Stroke(width = 2f)
        )
        
        // Orta çizgi
        drawLine(
            color = color,
            start = Offset(offset, offset + boxSize / 2),
            end = Offset(offset + boxSize, offset + boxSize / 2),
            strokeWidth = 2f
        )
    }
}

@Composable
fun TableIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val tableSize = size.toPx() * 0.85f
        val offset = size.toPx() * 0.075f
        
        // Masa üstü
        drawRect(
            color = color,
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(tableSize, tableSize * 0.6f),
            style = Stroke(width = 2f)
        )
        
        // Bacaklar
        drawLine(
            color = color,
            start = Offset(offset + tableSize * 0.2f, offset + tableSize * 0.6f),
            end = Offset(offset + tableSize * 0.2f, offset + tableSize * 0.9f),
            strokeWidth = 2f
        )
        drawLine(
            color = color,
            start = Offset(offset + tableSize * 0.8f, offset + tableSize * 0.6f),
            end = Offset(offset + tableSize * 0.8f, offset + tableSize * 0.9f),
            strokeWidth = 2f
        )
    }
}

@Composable
fun CartIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val cartSize = size.toPx()
        
        // Sepet gövdesi
        drawPath(
            path = Path().apply {
                moveTo(cartSize * 0.2f, cartSize * 0.3f)
                lineTo(cartSize * 0.3f, cartSize * 0.65f)
                lineTo(cartSize * 0.8f, cartSize * 0.65f)
                lineTo(cartSize * 0.85f, cartSize * 0.3f)
                close()
            },
            color = color,
            style = Stroke(width = 2f)
        )
        
        // Tekerler
        drawCircle(
            color = color,
            radius = cartSize * 0.06f,
            center = Offset(cartSize * 0.4f, cartSize * 0.8f)
        )
        drawCircle(
            color = color,
            radius = cartSize * 0.06f,
            center = Offset(cartSize * 0.7f, cartSize * 0.8f)
        )
    }
}

@Composable
fun CoffeeIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val cupSize = size.toPx()
        
        // Fincan
        drawPath(
            path = Path().apply {
                moveTo(cupSize * 0.25f, cupSize * 0.3f)
                lineTo(cupSize * 0.3f, cupSize * 0.75f)
                lineTo(cupSize * 0.7f, cupSize * 0.75f)
                lineTo(cupSize * 0.75f, cupSize * 0.3f)
                close()
            },
            color = color,
            style = Stroke(width = 2f)
        )
        
        // Kulp
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(cupSize * 0.65f, cupSize * 0.4f),
            size = androidx.compose.ui.geometry.Size(cupSize * 0.25f, cupSize * 0.3f),
            style = Stroke(width = 2f)
        )
        
        // Buhar
        drawLine(
            color = color.copy(alpha = 0.6f),
            start = Offset(cupSize * 0.4f, cupSize * 0.15f),
            end = Offset(cupSize * 0.35f, cupSize * 0.05f),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun BreadIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val breadSize = size.toPx()
        
        // Ekmek şekli (yuvarlak üst)
        drawPath(
            path = Path().apply {
                moveTo(breadSize * 0.2f, breadSize * 0.6f)
                lineTo(breadSize * 0.2f, breadSize * 0.8f)
                lineTo(breadSize * 0.8f, breadSize * 0.8f)
                lineTo(breadSize * 0.8f, breadSize * 0.6f)
                arcTo(
                    rect = Rect(
                        left = breadSize * 0.2f,
                        top = breadSize * 0.2f,
                        right = breadSize * 0.8f,
                        bottom = breadSize * 0.6f
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                close()
            },
            color = color,
            style = Stroke(width = 2f)
        )
        
        // Doku çizgileri
        drawLine(
            color = color.copy(alpha = 0.5f),
            start = Offset(breadSize * 0.35f, breadSize * 0.5f),
            end = Offset(breadSize * 0.35f, breadSize * 0.7f),
            strokeWidth = 1f
        )
        drawLine(
            color = color.copy(alpha = 0.5f),
            start = Offset(breadSize * 0.5f, breadSize * 0.45f),
            end = Offset(breadSize * 0.5f, breadSize * 0.7f),
            strokeWidth = 1f
        )
    }
}

@Composable
fun DrinkIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val glassSize = size.toPx()
        
        // Bardak
        drawPath(
            path = Path().apply {
                moveTo(glassSize * 0.3f, glassSize * 0.2f)
                lineTo(glassSize * 0.25f, glassSize * 0.8f)
                lineTo(glassSize * 0.75f, glassSize * 0.8f)
                lineTo(glassSize * 0.7f, glassSize * 0.2f)
                close()
            },
            color = color,
            style = Stroke(width = 2f)
        )
        
        // Sıvı seviyesi
        drawLine(
            color = color.copy(alpha = 0.5f),
            start = Offset(glassSize * 0.28f, glassSize * 0.5f),
            end = Offset(glassSize * 0.72f, glassSize * 0.5f),
            strokeWidth = 2f
        )
        
        // Pipet
        drawLine(
            color = color,
            start = Offset(glassSize * 0.6f, glassSize * 0.1f),
            end = Offset(glassSize * 0.55f, glassSize * 0.6f),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun MoneyIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val iconSize = size.toPx()
        
        // Elmas şekli
        drawPath(
            path = Path().apply {
                moveTo(iconSize * 0.5f, iconSize * 0.1f)
                lineTo(iconSize * 0.85f, iconSize * 0.5f)
                lineTo(iconSize * 0.5f, iconSize * 0.9f)
                lineTo(iconSize * 0.15f, iconSize * 0.5f)
                close()
            },
            color = color,
            style = Stroke(width = 2f)
        )
        
        // İç çizgiler
        drawLine(
            color = color,
            start = Offset(iconSize * 0.3f, iconSize * 0.3f),
            end = Offset(iconSize * 0.7f, iconSize * 0.3f),
            strokeWidth = 1.5f
        )
        drawLine(
            color = color,
            start = Offset(iconSize * 0.5f, iconSize * 0.1f),
            end = Offset(iconSize * 0.5f, iconSize * 0.9f),
            strokeWidth = 1.5f
        )
    }
}

@Composable
fun ListIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val iconSize = size.toPx()
        
        // Clipboard şekli
        drawRoundRect(
            color = color,
            topLeft = Offset(iconSize * 0.2f, iconSize * 0.15f),
            size = androidx.compose.ui.geometry.Size(iconSize * 0.6f, iconSize * 0.75f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(iconSize * 0.05f),
            style = Stroke(width = 2f)
        )
        
        // Üst tutacak
        drawRoundRect(
            color = color,
            topLeft = Offset(iconSize * 0.35f, iconSize * 0.05f),
            size = androidx.compose.ui.geometry.Size(iconSize * 0.3f, iconSize * 0.12f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(iconSize * 0.03f),
            style = Stroke(width = 1.5f)
        )
        
        // Liste çizgileri
        listOf(0.35f, 0.5f, 0.65f).forEach { y ->
            drawLine(
                color = color,
                start = Offset(iconSize * 0.3f, iconSize * y),
                end = Offset(iconSize * 0.7f, iconSize * y),
                strokeWidth = 1.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun AllItemsIcon(
    size: Dp = 20.dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val iconSize = size.toPx()
        val dotRadius = iconSize * 0.08f
        
        // 3x3 grid noktalar
        for (row in 0..2) {
            for (col in 0..2) {
                drawCircle(
                    color = color,
                    radius = dotRadius,
                    center = Offset(
                        iconSize * (0.25f + col * 0.25f),
                        iconSize * (0.25f + row * 0.25f)
                    )
                )
            }
        }
    }
}
