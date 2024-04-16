package com.rcl.nextshiki.elements.contentscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.moko.MR
import com.rcl.nextshiki.base.profile.mainprofile.profile.RatingBar
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AsyncPicture(painter: Painter) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(maxWidth / 2)
                .align(Alignment.Center).clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f),
        )
    }
}

@Stable
@Composable
fun CommonName(russian: String?, english: ImmutableList<String?>) {
    Text(
        style = MaterialTheme.typography.headlineSmall, text = when (Locale.current.language) {
            "ru" -> russian ?: ""
            else -> english[0] ?: ""
        }, overflow = TextOverflow.Ellipsis
    )

    //TODO: Add another names
    /*if (data.english.size != 1) {

    }*/
}

@Composable
fun CommonScore(score: String?) {
    Column {
        Row {
            Text(
                text = "${stringResource(MR.strings.score_in_object)} "
            )
            score?.let {
                Text(
                    text = it,
                )
            }
        }
        RatingBar(
            imageVector = Icons.Filled.StarRate,
            maxRating = 10,
            rating = score?.toFloatOrNull() ?: 0f,
        )
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun CommonDescription(descriptionHtml: String?, descriptionSource: String?, navigateTo: (String, SearchType) -> Unit) {
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge, text = stringResource(MR.strings.description_in_object)
        )
        if (descriptionHtml != null) {

            val myUriHandler by remember {
                mutableStateOf(object : UriHandler {
                    override fun openUri(uri: String) {
                        val list = uri.split("/")
                        when (list[3]) {
                            "animes" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Anime)
                            }

                            "mangas" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Manga)
                            }

                            "ranobe" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Ranobe)
                            }

                            "people" -> {
                                navigateTo(list[4].split("-")[0], SearchType.People)
                            }

                            "users" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Users)
                            }

                            "characters" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Characters)
                            }

                            else -> Napier.i("uri - $uri, part - ${list[3]}")
                        }
                    }
                })
            }

            val state = rememberRichTextState()
            state.setConfig(
                linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true)
            )
            state.htmlToAnnotatedString(descriptionHtml)

            CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                RichText(
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    state = state
                )
            }

            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${stringResource(MR.strings.source)}: ${
                    descriptionSource ?: stringResource(
                        MR.strings.unknown
                    )
                }"
            )
        } else {
            Text(
                text = stringResource(MR.strings.text_empty)
            )
        }
    }
}

@Composable
fun CommonState(status: String?) {
    Row {
        Text("${stringResource(MR.strings.status_in_object)} ")
        Text(
            stringResource(
                when (status) {
                    "released" -> MR.strings.status_released
                    "anons" -> MR.strings.status_anons
                    "ongoing" -> MR.strings.status_ongoing
                    "paused" -> MR.strings.status_paused
                    "discontinued" -> MR.strings.status_discontinued
                    else -> MR.strings.unknown
                }
            )
        )
    }
}

fun RichTextState.htmlToAnnotatedString(html: String) {
    this.setHtml(html)
}