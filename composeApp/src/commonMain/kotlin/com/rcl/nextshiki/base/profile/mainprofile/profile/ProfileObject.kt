package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.NavigateNext
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.rcl.moko.MR.strings.more
import com.rcl.moko.MR.strings.picture_error
import com.rcl.moko.MR.strings.profile_about
import com.rcl.moko.MR.strings.profile_add_friend
import com.rcl.moko.MR.strings.profile_charts
import com.rcl.moko.MR.strings.profile_common_info
import com.rcl.moko.MR.strings.profile_friend
import com.rcl.moko.MR.strings.profile_friends
import com.rcl.moko.MR.strings.profile_ignore
import com.rcl.moko.MR.strings.profile_ignore_reset
import com.rcl.moko.MR.strings.profile_message
import com.rcl.moko.MR.strings.profile_rating
import com.rcl.moko.MR.strings.profile_scores
import com.rcl.moko.MR.strings.profile_statuses
import com.rcl.moko.MR.strings.profile_types
import com.rcl.moko.MR.strings.search_anime
import com.rcl.moko.MR.strings.search_manga
import com.rcl.nextshiki.elements.contentscreens.CommonName
import com.rcl.nextshiki.elements.contentscreens.htmlToAnnotatedString
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.models.searchobject.users.Stats
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.rcl.nextshiki.models.topics.User
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.rcl.nextshiki.models.universal.Image as ImageModel

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
        item(key = "profileCommonInfo") {
            ProfileCommonInfo(
                russian = data.russian,
                english = persistentListOf(data.nickname, data.name),
                inFriends = data.inFriends,
                isIgnored = data.isIgnored,
                lastOnline = data.lastOnline,
                friendFun = friendFun,
                ignoreFun = ignoreFun
            )
        }
        item(key = "friends") {
            FriendList(data.friendsList.subList(0, 10).toPersistentList(), data.friendsList.size > 11)
        }
        item(key = "infoBlock") {
            InfoBlock(data.commonInfo.toPersistentList(), data.aboutHtml)
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
            item(key = "profileCommonInfo") {
                ProfileCommonInfo(
                    russian = data.russian,
                    english = persistentListOf(data.nickname, data.name),
                    inFriends = data.inFriends,
                    isIgnored = data.isIgnored,
                    lastOnline = data.lastOnline,
                    friendFun = friendFun,
                    ignoreFun = ignoreFun
                )
            }
            item(key = "friends") {
                FriendList(data.friendsList.subList(0, 10).toPersistentList(), data.friendsList.size > 11)
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "infoBlock") {
                InfoBlock(data.commonInfo.toPersistentList(), data.aboutHtml)
            }
            item(key = "charts") {
                ChartList(data.stats)
            }
        }
    }
}

@Composable
private fun InfoBlock(commonInfo: ImmutableList<String>, aboutHtml: String?) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            CommonInfo(commonInfo)
            AboutInfo(aboutHtml)
        }
    }
}

@Composable
private fun ProfileCommonInfo(
    russian: String?,
    english: ImmutableList<String?>,
    inFriends: Boolean?,
    isIgnored: Boolean?,
    lastOnline: String?,
    friendFun: (Boolean) -> Unit,
    ignoreFun: (Boolean) -> Unit
) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            CommonName(russian, english)
            ActionButtons(inFriends, isIgnored, friendFun, ignoreFun)
            LastOnline(lastOnline)
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
            colors = CardDefaults.cardColors()
                .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
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
                colors = CardDefaults.cardColors()
                    .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
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
                colors = CardDefaults.cardColors()
                    .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
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
private fun FriendList(friendList: ImmutableList<User>, hasNext: Boolean) {
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(10.dp)) {
            Text(stringResource(profile_friends), style = MaterialTheme.typography.headlineSmall)
            Card(
                colors = CardDefaults.cardColors()
                    .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary))
            ) {
                LazyRow(
                    state = rowState,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(5.dp).padding(start = 10.dp).draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            coroutineScope.launch {
                                rowState.scrollBy(-delta)
                            }
                        },
                    ),
                ) {
                    items(friendList, key = { it.id ?: "Unexpected user" }) { friend ->
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.width(50.dp)) {
                            friend.image?.x160?.let { imageLink ->
                                Box(
                                    Modifier.clip(CircleShape)
                                ) { FriendIcon(url = imageLink) }
                            }
                            friend.nickname?.let { nickname ->
                                Text(
                                    nickname,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if (hasNext) {
                        item(key = "moreFriends") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = "more friends",
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(stringResource(more), maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendIcon(url: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
    when (painter.state) {
        is AsyncImagePainter.State.Error -> {
            Column {
                Icon(Icons.Default.Error, contentDescription = "error")
                Text(stringResource(picture_error))
            }
        }

        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(painter = painter, "friend profile pic")
        }

        else -> {}
    }
}


@Composable
private fun ProfileIcon(image: ImageModel?) {
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
                containerColor = paletteState.color.harmonize(MaterialTheme.colorScheme.primary)
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
        Text(text = lastOnline.capitalize(Locale.current), modifier = Modifier.padding(start = 10.dp))
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun AboutInfo(aboutHtml: String?) {
    if (!aboutHtml.isNullOrEmpty()) {
        var isVisible by remember { mutableStateOf(false) }
        Column(modifier = Modifier.noRippleClickable { isVisible = isVisible.not() }) {
            Text(text = stringResource(profile_about), style = MaterialTheme.typography.headlineSmall)
            AnimatedVisibility(isVisible) {
                Card(
                    colors = CardDefaults.cardColors()
                        .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
                ) {
                    Box(modifier = Modifier.padding(5.dp).padding(start = 10.dp).fillMaxWidth()) {
                        val state = rememberRichTextState()
                        state.setConfig(
                            linkColor = Color.Blue.harmonize(
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                matchSaturation = true
                            )
                        )
                        state.htmlToAnnotatedString(aboutHtml)
                        RichText(
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                            state = state
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun CommonInfo(commonInfo: ImmutableList<String>) {
    Column {
        Text(text = stringResource(profile_common_info), style = MaterialTheme.typography.headlineSmall)
        Card(
            colors = CardDefaults.cardColors()
                .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
        ) {
            Column(modifier = Modifier.padding(5.dp).padding(start = 10.dp).fillMaxWidth()) {
                repeat(commonInfo.size) {
                    val state = rememberRichTextState()
                    state.setConfig(
                        linkColor = Color.Blue.harmonize(
                            MaterialTheme.colorScheme.onPrimaryContainer,
                            matchSaturation = true
                        )
                    )
                    state.htmlToAnnotatedString(commonInfo[it])
                    RichText(
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        state = state
                    )
                }
            }
        }
    }
}

@Composable
@Stable
private fun ChartList(stats: Stats?) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(10.dp)) {
            Text(stringResource(profile_charts), style = MaterialTheme.typography.headlineSmall)
            ChartRow(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 4.dp,
                    bottomStart = 4.dp
                ),
                type = profile_scores,
                animeChart = stats?.scores?.anime?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.scores?.manga?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = RoundedCornerShape(
                    4.dp
                ),
                type = profile_statuses,
                animeChart = stats?.statuses?.anime?.toPersistentList()?.toChartElement({ it.name }, { it.size })
                    ?: persistentListOf(),
                mangaChart = stats?.statuses?.manga?.toPersistentList()?.toChartElement({ it.name }, { it.size })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = RoundedCornerShape(
                    4.dp
                ),
                type = profile_types,
                animeChart = stats?.types?.anime?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.types?.manga?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                ),
                type = profile_rating,
                animeChart = stats?.ratings?.anime?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.ratings?.manga?.toPersistentList()?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
        }
    }
}

@Composable
private fun ChartRow(
    shape: Shape,
    type: StringResource,
    animeChart: ImmutableList<ChartElement>,
    mangaChart: ImmutableList<ChartElement>
) {
    var enabled by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors()
            .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
        shape = shape
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp).noRippleClickable { enabled = enabled.not() }.fillMaxWidth()
        ) {
            Text(stringResource(type), style = MaterialTheme.typography.bodyMedium)
            AnimatedVisibility(enabled) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (animeChart.isNotEmpty()) {
                        ProfileChartElement(
                            anime = animeChart,
                            modifier = Modifier.weight(1f).padding(5.dp),
                            resource = search_anime
                        )
                    }
                    if (mangaChart.isNotEmpty()) {
                        ProfileChartElement(
                            anime = mangaChart,
                            modifier = Modifier.weight(1f).padding(5.dp),
                            resource = search_manga
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Stable
private fun ProfileChartElement(
    anime: ImmutableList<ChartElement>,
    modifier: Modifier = Modifier,
    resource: StringResource
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(stringResource(resource), style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                PieChart(
                    size = maxWidth,
                    chartElements = anime,
                    strokeWidth = 4.dp
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                anime.forEach { chartElement ->
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

@Composable
fun <T> ImmutableList<T>.toChartElement(
    nameSelector: (T) -> String?,
    valueSelector: (T) -> Int?
): ImmutableList<ChartElement> {
    val totalValue = this.sumOf { valueSelector(it) ?: 0 }
    return this.map { contentScore ->
        val percent = (valueSelector(contentScore)?.toFloat() ?: 0f) / totalValue
        ChartElement(
            nameSelector(contentScore),
            percent,
            (nameSelector(contentScore)?.toColorAsSeed()?.harmonize(MaterialTheme.colorScheme.primary)
                ?: Color.Transparent)
        )
    }.toPersistentList()
}
