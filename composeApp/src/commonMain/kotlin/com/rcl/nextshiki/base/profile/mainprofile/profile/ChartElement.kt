package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
    var sum: Float = 0.toFloat()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

@Composable
fun PieChart(
    chartElements: ImmutableList<ChartElement>,
    size: Dp,
    strokeWidth: Dp,
) {
    Canvas(modifier = Modifier.size(size)) {
        val totalPercent = chartElements.sumOf { it.percent }
        var currentAngle = -90f

        chartElements.forEach { element ->
            val angle = (element.percent / totalPercent) * 360f
            drawPieSlice(
                color = element.color,
                startAngle = currentAngle,
                sweepAngle = angle,
                strokeWidth = strokeWidth.toPx(),
            )
            currentAngle += angle
        }
    }
}

private fun DrawScope.drawPieSlice(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    strokeWidth: Float,
) {
    val radius = (size.minDimension / 2) - (strokeWidth / 2)

    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(strokeWidth, cap = StrokeCap.Round),
    )
}

@Preview
@Composable
fun PreviewPieChart() {
    val chartElements = persistentListOf(
        ChartElement(percent = 30f, color = Color.Red),
        ChartElement(percent = 40f, color = Color.Green),
        ChartElement(percent = 30f, color = Color.Blue),
    )

    Card {
        PieChart(
            chartElements = chartElements,
            size = 200.dp,
            strokeWidth = 4.dp,
        )
    }
}

data class ChartElement(
    val name: String? = null,
    val percent: Float,
    val color: Color,
)

fun String.toColorAsSeed(): Color {
    val seed = Random(this.hashCode())

    val alpha = seed.nextInt(256)
    val red = seed.nextInt(256)
    val green = seed.nextInt(256)
    val blue = seed.nextInt(256)

    return Color((alpha shl 24) or (red shl 16) or (green shl 8) or blue)
}