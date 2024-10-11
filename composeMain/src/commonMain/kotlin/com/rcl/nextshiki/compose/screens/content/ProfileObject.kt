package com.rcl.nextshiki.compose.screens.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.rcl.nextshiki.SharedRes.strings.content_name
import com.rcl.nextshiki.SharedRes.strings.profile_about
import com.rcl.nextshiki.SharedRes.strings.profile_add_friend
import com.rcl.nextshiki.SharedRes.strings.profile_common_info
import com.rcl.nextshiki.SharedRes.strings.profile_friend
import com.rcl.nextshiki.SharedRes.strings.profile_friends
import com.rcl.nextshiki.SharedRes.strings.profile_ignore
import com.rcl.nextshiki.SharedRes.strings.profile_ignore_reset
import com.rcl.nextshiki.SharedRes.strings.profile_message
import com.rcl.nextshiki.compose.AdaptiveRow
import com.rcl.nextshiki.compose.CommonCarouselList
import com.rcl.nextshiki.compose.HorizontalRoundedCornerShape
import com.rcl.nextshiki.compose.getLangRes
import com.rcl.nextshiki.compose.noRippleClickable
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.users.UserObject
import dev.icerock.moko.resources.compose.stringResource
import com.rcl.nextshiki.models.universal.Image as ImageModel

@Composable
fun ProfileObject(
    currentCode: String,
    data: UserObject,
    friendFun: (Boolean) -> Unit,
    ignoreFun: (Boolean) -> Unit,
    navigateTo: (SearchCardModel, SearchType) -> Unit
) {
    AdaptiveRow(
        {
            item(key = "profileIcon") {
                ProfileIcon(data.image)
            }
            item(key = "profileCommonInfo") {
                ProfileCommonInfo(
                    russian = data.russian,
                    english = listOf(data.nickname, data.name),
                    inFriends = data.inFriends,
                    isIgnored = data.isIgnored,
                    lastOnline = data.lastOnline,
                    friendFun = friendFun,
                    ignoreFun = ignoreFun,
                    currentCode = currentCode
                )
            }
            item(key = "friends") {
                CommonCarouselList(
                    navigateTo = navigateTo,
                    title = profile_friends,
                    carouselList = data.friendsList.subList(0, 10),
                    hasNext = data.friendsList.size > 11,
                    currentCode = currentCode,
                    searchType = SearchType.Users
                )

            }
        },
        {
            item(key = "infoBlock") {
                InfoBlock(data.commonInfo, data.aboutHtml)
            }
            item(key = "charts") {
                ChartList(data.stats)
            }
        }
    )
}

@Composable
private fun InfoBlock(commonInfo: List<String>, aboutHtml: String?) {
    Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ) {
            CommonInfo(commonInfo)
            AboutInfo(aboutHtml)
        }
    }
}

@Composable
private fun ProfileCommonInfo(
    russian: String?,
    english: List<String?>,
    currentCode: String,
    inFriends: Boolean?,
    isIgnored: Boolean?,
    lastOnline: String?,
    friendFun: (Boolean) -> Unit,
    ignoreFun: (Boolean) -> Unit
) {
    Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(content_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    russian?.let { russian ->
                        english[0]?.let { english ->
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                text = getLangRes(
                                    russian = russian,
                                    english = english,
                                    currentCode = currentCode
                                ),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
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
        ChatCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            onClick = {

            }
        )

        friended?.let { friendBool ->
            FriendCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                friended = friendBool,
                friendFun = {
                    onFriendToggle(it)
                    friended = it
                }
            )
        }

        ignored?.let { ignoreBool ->
            IgnoreCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                ignored = ignoreBool,
                ignoreFun = {
                    onIgnoreToggle(it)
                    ignored = it
                }
            )
        }
    }
}

@Composable
private fun ChatCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Card(
    modifier = modifier,
    colors = CardDefaults.cardColors()
        .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
    shape = HorizontalRoundedCornerShape(
        start = 20.dp,
        end = 4.dp
    )
) {
    Column(
        modifier = Modifier.padding(15.dp).noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = "profile chat"
        )
        Text(stringResource(profile_message), textAlign = TextAlign.Center)
    }
}

@Composable
private fun IgnoreCard(
    ignored: Boolean,
    modifier: Modifier = Modifier,
    ignoreFun: (Boolean) -> Unit,
) = Card(
    colors = CardDefaults.cardColors()
        .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
    modifier = modifier,
    shape = HorizontalRoundedCornerShape(
        end = 20.dp,
        start = 4.dp
    )
) {
    IgnoreBlock(
        ignored = ignored,
        onIgnoreToggle = ignoreFun
    )
}

@Composable
private fun IgnoreBlock(
    ignored: Boolean,
    onIgnoreToggle: (Boolean) -> Unit
) {
    AnimatedContent(ignored) { ignoreState ->
        Column(
            modifier = Modifier
                .noRippleClickable {
                    onIgnoreToggle(ignored.not())
                }
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (ignoreState) Icons.Outlined.VisibilityOff else Icons.Filled.VisibilityOff,
                contentDescription = "profile ignore"
            )
            Text(
                text = stringResource((if (ignoreState) profile_ignore else profile_ignore_reset)),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FriendCard(
    friended: Boolean,
    modifier: Modifier = Modifier,
    friendFun: (Boolean) -> Unit
) = Card(
    colors = CardDefaults.cardColors()
        .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
    modifier = modifier,
    shape = RoundedCornerShape(4.dp)
) {
    FriendBlock(
        friended = friended,
        onFriendToggle = friendFun
    )
}

@Composable
private fun FriendBlock(
    friended: Boolean,
    onFriendToggle: (Boolean) -> Unit
) {
    AnimatedContent(friended) { friendState ->
        Column(
            modifier = Modifier
                .noRippleClickable {
                    onFriendToggle(friended.not())
                }
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (friendState) Icons.Filled.PersonAdd else Icons.Outlined.PersonAdd,
                contentDescription = "profile add friend"
            )
            Text(
                text = stringResource(if (friendState) profile_friend else profile_add_friend),
                textAlign = TextAlign.Center
            )
        }
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
    val painterState by painter.state.collectAsState()
    val loader = rememberPainterLoader()
    val paletteState = rememberDominantColorState(loader = loader)
    LaunchedEffect(painterState) {
        if (painterState is AsyncImagePainter.State.Success) {
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
            when (painterState) {
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
        Text(
            text = lastOnline.capitalize(Locale.current),
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun AboutInfo(aboutHtml: String?) {
    if (!aboutHtml.isNullOrEmpty()) {
        val state = rememberRichTextState()
        state.config.linkColor = Color.Blue.harmonize(
            MaterialTheme.colorScheme.onPrimaryContainer,
            matchSaturation = true
        )
        var isVisible by remember { mutableStateOf(false) }
        LaunchedEffect(isVisible) {
            if (isVisible) {
                state.setHtml(aboutHtml)
            } else {
                state.setText("...")
            }
        }
        Column(modifier = Modifier.noRippleClickable { isVisible = isVisible.not() }) {
            Text(
                text = stringResource(profile_about),
                style = MaterialTheme.typography.headlineSmall
            )
            Card(
                colors = CardDefaults.cardColors()
                    .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
            ) {
                Box(modifier = Modifier.padding(5.dp).padding(start = 10.dp).fillMaxWidth()) {
                    RichText(
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        state = state
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun CommonInfo(commonInfo: List<String>) {
    Column {
        Text(
            text = stringResource(profile_common_info),
            style = MaterialTheme.typography.headlineSmall
        )
        Card(
            colors = CardDefaults.cardColors()
                .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
        ) {
            Column(modifier = Modifier.padding(5.dp).padding(start = 10.dp).fillMaxWidth()) {
                repeat(commonInfo.size) {
                    val state = rememberRichTextState()
                    state.config.linkColor = Color.Blue.harmonize(
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        matchSaturation = true
                    )
                    state.setHtml(commonInfo[it])
                    RichText(
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        state = state
                    )
                }
            }
        }
    }
}
