package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.moko.MR.strings.description_in_object
import com.rcl.moko.MR.strings.score_in_object
import com.rcl.moko.MR.strings.source
import com.rcl.moko.MR.strings.text_empty
import com.rcl.moko.MR.strings.unknown
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AnimeScreen(data: AnimeObject) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        Compact -> {
            mobile(data)
        }

        else -> {
            desktop(data)
        }
    }
}

@Composable
fun mobile(data: AnimeObject) {
    LazyColumn {
        item {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(BuildConfig.DOMAIN + (data.image?.original))
                    .size(Size.ORIGINAL)
                    .build()
            )
            when (painter.state) {
                is Success -> {
                    AnimePicture(painter)
                }

                is AsyncImagePainter.State.Empty -> {

                }
                is AsyncImagePainter.State.Error -> {

                }
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
        item {
            AnimeName(data)
        }
        item {
            AnimeScore(data)
        }
        item {
            AnimeDescription(data)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun desktop(data: AnimeObject) {
    FlowColumn {
        Box {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(BuildConfig.DOMAIN + (data.image?.original))
                    .size(Size.ORIGINAL)
                    .build()
            )
            when (painter.state) {
                is Success -> {
                    AnimePicture(painter)
                }

                is AsyncImagePainter.State.Empty -> {

                }
                is AsyncImagePainter.State.Error -> {

                }
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
        Column {
            AnimeName(data)
            AnimeScore(data)
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AnimeDescription(data)
        }
    }
}

@Composable
private fun AnimePicture(painter: Painter) {
    BoxWithConstraints {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(maxWidth / 2)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .aspectRatio(1f),
        )
    }
}

@Composable
private fun AnimeName(data: AnimeObject) {
    Text(
        text = when (Locale.current.language) {
            "ru" -> data.russian ?: ""
            else -> data.english[0] ?: ""
        },
        softWrap = true,
        modifier = Modifier.padding(12.dp)
    )

    if (data.english.size != 1) {
        //TODO: Add another names
    }
}

@Composable
private fun AnimeScore(data: AnimeObject) {
    Row {
        Text(
            text = stringResource(score_in_object)
        )
        data.score?.let {
            Text(
                text = it,
            )
        }
        Icon(
            Icons.Default.Star,
            contentDescription = "Star icon in content"
        )
    }
}

@Composable
private fun AnimeDescription(data: AnimeObject) {
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(description_in_object)
        )
        if (data.descriptionHtml != null) {

            val text = htmlToAnnotatedString(data.descriptionHtml)

            ClickableText(
                text = text,
                onClick = { offset ->
                    text.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { span ->
                            Napier.i(span.tag)

                        }
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${stringResource(source)}: ${
                    data.descriptionSource ?: stringResource(
                        unknown
                    )
                }"
            )
        } else {
            Text(
                text = stringResource(text_empty)
            )
        }
    }
}

fun htmlToAnnotatedString(html: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        val tagRegex = Regex("<[^>]+>")
        val tags = tagRegex.findAll(html)
        var lastIndex = 0
        tags.forEach { tag ->
            val tagValue = tag.value
            val tagIndex = tag.range.first
            val cleanTag = tagValue.replace(Regex("""\s*=\s*["'][^"']*["']"""), "")
            append(html.substring(lastIndex, tagIndex))
            lastIndex = tag.range.last + 1
            when {
                cleanTag.startsWith("<b>") -> {
                    withStyle(style = SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                        append(cleanTag.removePrefix("<b>").removeSuffix("</b>"))
                    }
                }

                cleanTag.startsWith("<i>") -> {
                    withStyle(style = SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                        append(cleanTag.removePrefix("<i>").removeSuffix("</i>"))
                    }
                }

                cleanTag.startsWith("<a") -> {
                    val url = Regex("""href=["'](.*?)["']""").find(cleanTag)?.groupValues?.get(1)
                    if (url != null) {
                        pushStringAnnotation("URL", url)
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(cleanTag.removePrefix("<a href=\"$url\">").removeSuffix("</a>"))
                        }
                        pop()
                    } else {
                        append(cleanTag)
                    }
                }

                cleanTag.startsWith("<div>") -> {
                    append(cleanTag.removePrefix("<div>").removeSuffix("</div>"))
                }

                else -> {
                    append(cleanTag)
                }
            }
        }
        append(html.substring(lastIndex))
    }
    return annotatedString
}
