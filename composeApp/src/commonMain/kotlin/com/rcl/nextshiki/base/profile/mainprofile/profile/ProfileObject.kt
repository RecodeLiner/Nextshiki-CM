package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.moko.MR.strings.profile_about
import com.rcl.moko.MR.strings.profile_add_friend
import com.rcl.moko.MR.strings.profile_common_info
import com.rcl.moko.MR.strings.profile_friend
import com.rcl.moko.MR.strings.profile_ignore
import com.rcl.moko.MR.strings.profile_ignore_reset
import com.rcl.moko.MR.strings.profile_message
import com.rcl.moko.MR.strings.profile_scores
import com.rcl.moko.MR.strings.search_anime
import com.rcl.moko.MR.strings.search_manga
import com.rcl.nextshiki.elements.contentscreens.CommonName
import com.rcl.nextshiki.elements.contentscreens.htmlToAnnotatedString
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.models.searchobject.users.ContentScore
import com.rcl.nextshiki.models.searchobject.users.Stats
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.rcl.nextshiki.models.universal.Image
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProfileObject(data: UserObject, friendFun: (Boolean) -> Unit, ignoreFun: (Boolean) -> Unit) {
    val size = calculateWindowSizeClass().widthSizeClass
    when (size) {
        Compact -> {
            mobileUI(data, friendFun, ignoreFun)
        }

        else -> {
            desktopUI(data, friendFun, ignoreFun)
        }
    }
}

@Composable
private fun mobileUI(data: UserObject, friendFun: (Boolean) -> Unit, ignoreFun: (Boolean) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
        item(key = "profileIcon") {
            ProfileIcon(data.image)
        }
        item(key = "name") {
            CommonName(data.russian, persistentListOf(data.nickname, data.name))
        }
        item(key = "actions") {
            ActionButtons(data.inFriends, data.isIgnored, friendFun, ignoreFun)
        }
        item(key = "online") {
            LastOnline(data.lastOnline)
        }
        item(key = "commonInfo") {
            CommonInfo(data.commonInfo.toPersistentList())
        }
        item(key = "about") {
            AboutInfo(data.aboutHtml)
        }
        item(key = "charts") {
            ChartList(data.stats)
        }
    }
}

@Composable
private fun desktopUI(data: UserObject, friendFun: (Boolean) -> Unit, ignoreFun: (Boolean) -> Unit) {
    Row {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "profileIcon") {
                ProfileIcon(data.image)
            }
            item(key = "name") {
                CommonName(data.russian, persistentListOf(data.nickname, data.name))
            }
            item(key = "actions") {
                ActionButtons(data.inFriends, data.isIgnored, friendFun, ignoreFun)
            }
            item(key = "online") {
                LastOnline(data.lastOnline)
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "commonInfo") {
                CommonInfo(data.commonInfo.toPersistentList())
            }
            item(key = "about") {
                AboutInfo(data.aboutHtml)
            }
            item(key = "charts") {
                ChartList(data.stats)
            }
        }
    }
}

@Composable
private fun ActionButtons(
    isInFriends: Boolean?,
    isIgnored: Boolean?,
    onFriendToggle: (Boolean) -> Unit,
    onIgnoreToggle: (Boolean) -> Unit
) {
    var friended by remember { mutableStateOf(isInFriends) }
    var ignored by remember { mutableStateOf(isIgnored) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
    ) {
        Card(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                bottomStart = 20.dp,
                topEnd = 4.dp,
                bottomEnd = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "profile chat")
                Text(stringResource(profile_message), textAlign = TextAlign.Center)
            }
        }

        if (friended != null) {
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(4.dp)
            ) {
                AnimatedContent(friended) { friendState ->
                    Column(
                        modifier = Modifier
                            .noRippleClickable {
                                onFriendToggle(friended!!)
                                friended = friended!!.not()
                            }
                            .padding(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (friendState == true) Icons.Filled.PersonAdd else Icons.Outlined.PersonAdd,
                            contentDescription = "profile add friend"
                        )
                        Text(
                            stringResource(if (friendState == true) profile_friend else profile_add_friend),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (ignored != null) {
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    topStart = 4.dp,
                    bottomStart = 4.dp
                )
            ) {
                AnimatedContent(ignored) { ignoreState ->
                    Column(
                        modifier = Modifier
                            .noRippleClickable {
                                onIgnoreToggle(ignored!!)
                                ignored = ignored!!.not()
                            }
                            .padding(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (ignoreState == true) Icons.Outlined.VisibilityOff else Icons.Filled.VisibilityOff,
                            contentDescription = "profile ignore"
                        )
                        Text(
                            stringResource(if (ignoreState == true) profile_ignore else profile_ignore_reset),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ProfileIcon(image: Image?) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(image?.x160)
            .size(Size.ORIGINAL)
            .build()
    )
    val loader = rememberPainterLoader()
    val paletteState = rememberDominantColorState(loader = loader)
    LaunchedEffect(painter.state) {
        if (painter.state is AsyncImagePainter.State.Success) {
            paletteState.updateFrom(painter)
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .width(this.maxWidth / 2)
                .aspectRatio(1f)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(
                containerColor = paletteState.color
            )
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Success -> {
                    Image(
                        modifier = Modifier.fillMaxSize().clip(shape = RoundedCornerShape(200.dp)),
                        painter = painter,
                        contentDescription = "Profile Image"
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Icon(imageVector = Icons.Default.Error, "Profile Icon Error")
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                else -> {

                }
            }
        }
    }
}

@Composable
private fun LastOnline(lastOnline: String?) {
    if (!lastOnline.isNullOrEmpty()) {
        Text(text = lastOnline.capitalize(Locale.current))
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun AboutInfo(aboutHtml: String?) {
    if (!aboutHtml.isNullOrEmpty()) {
        Column {
            Text(text = stringResource(profile_about), style = MaterialTheme.typography.headlineSmall)
            val state = rememberRichTextState()
            state.setConfig(
                linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true)
            )
            state.htmlToAnnotatedString(aboutHtml)
            RichText(
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                state = state
            )
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun CommonInfo(commonInfo: ImmutableList<String>) {
    Column {
        Text(text = stringResource(profile_common_info), style = MaterialTheme.typography.headlineSmall)
        repeat(commonInfo.size) {
            val state = rememberRichTextState()
            state.setConfig(
                linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true)
            )
            state.htmlToAnnotatedString(commonInfo[it])
            RichText(
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                state = state
            )
        }
    }
}

@Composable
@Stable
private fun ChartList(stats: Stats?) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(stringResource(profile_scores))
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            AnimeScoreChart(stats?.scores?.anime?.toPersistentList(), modifier = Modifier.weight(1f), search_anime)
            AnimeScoreChart(stats?.scores?.manga?.toPersistentList(), modifier = Modifier.weight(1f), search_manga)
        }
    }
}

@Composable
@Stable
private fun AnimeScoreChart(anime: ImmutableList<ContentScore>?, modifier: Modifier, resource: StringResource) {
    if (anime != null) {
        val list = anime.toChartElement()
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(stringResource(resource), style = MaterialTheme.typography.bodyMedium )
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                BoxWithConstraints(modifier = Modifier.weight(1f)) {
                    PieChart(
                        size = maxWidth,
                        chartElements = list,
                        strokeWidth = 4.dp
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    list.forEach { chartElement ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(chartElement.color)
                                    .clip(RoundedCornerShape(1.dp))
                            )
                            Text(
                                text = (" - ${chartElement.name}: ${(chartElement.percent * 100 * 10).roundToInt() / 10.0}%"),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImmutableList<ContentScore>.toChartElement(): ImmutableList<ChartElement> {
    val totalValue = this.sumOf { it.value ?: 0 }
    return this.map { contentScore ->
        val percent = (contentScore.value?.toFloat() ?: 0f) / totalValue
        ChartElement(
            contentScore.name,
            percent,
            (contentScore.name?.toColorAsSeed()?.harmonize(MaterialTheme.colorScheme.primary) ?: Color.Transparent)
        )
    }.toPersistentList()
}