package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    maxRating: Int,
    colorActive: Color = Color.Yellow,
    colorInActive: Color = Color.Transparent,
    imageVector: ImageVector,
    rating: Float,
    extraInternalIconPadding: Dp = 0.dp,
    iconSize: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    require(maxRating > 0 && rating >= 0 && rating <= maxRating) {
        "Invalid rating parameters in RatingBar"
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(extraInternalIconPadding)) {
        repeat(maxRating) { index ->
            val progress = if (index < rating.toInt()) {
                1f
            } else if (index == rating.toInt()) {
                rating - rating.toInt()
            } else {
                0f
            }
            val brush = Brush.horizontalGradient(
                0f to colorActive,
                progress to colorActive,
                progress to colorInActive,
                1f to colorInActive,
            )
            Icon(
                imageVector = imageVector,
                modifier = Modifier.size(iconSize)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    },
                contentDescription = "Star Rate Icon - ${index + 1}",
            )
        }
    }
}

@Preview
@Composable
fun RatingBarPreview() {
    MaterialTheme {
        Card {
            Column {
                RatingBar(
                    imageVector = Icons.Filled.StarRate,
                    maxRating = 10,
                    rating = 8.5f,
                    iconSize = 24.dp,
                    extraInternalIconPadding = 4.dp,
                )
                RatingBar(
                    imageVector = Icons.Filled.StarRate,
                    maxRating = 10,
                    rating = 8.9f,
                    iconSize = 24.dp,
                    extraInternalIconPadding = 4.dp,
                    colorActive = Color.Red,
                    colorInActive = Color.Green,
                )
            }
        }
    }
}
