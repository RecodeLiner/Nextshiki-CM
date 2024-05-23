package com.rcl.nextshiki.base.profile.mainprofile.profile

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
import com.rcl.mr.MR.strings.profile_about
import com.rcl.mr.MR.strings.profile_add_friend
import com.rcl.mr.MR.strings.profile_common_info
import com.rcl.mr.MR.strings.profile_friend
import com.rcl.mr.MR.strings.profile_friends
import com.rcl.mr.MR.strings.profile_ignore
import com.rcl.mr.MR.strings.profile_ignore_reset
import com.rcl.mr.MR.strings.profile_message
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.elements.contentscreens.CommonCarouselList
import com.rcl.nextshiki.elements.contentscreens.CommonName
import com.rcl.nextshiki.elements.contentscreens.htmlToAnnotatedString
import com.rcl.nextshiki.elements.contentscreens.toCarouselModel
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.rcl.nextshiki.models.universal.Image as ImageModel

@Composable
fun ProfileObject(data: UserObject, friendFun: (Boolean) -> Unit, ignoreFun: (Boolean) -> Unit, navigateTo: (String, SearchType) -> Unit) {
    AdaptiveRow(
        firstRow = {
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
                CommonCarouselList(
                    navigateTo = navigateTo,
                    title = profile_friends,
                    carouselList = data.friendsList.subList(0, 10).toPersistentList()
                        .toCarouselModel(
                            idSelector = { it.id },
                            englishNameSelector = { it.nickname },
                            russianNameSelector = { it.nickname },
                            imageSelector = { it.avatar },
                            searchTypeSelector = { SearchType.Users },
                            urlSelector = { it.url }
                        ),
                    hasNext = data.friendsList.size > 11)
            }
        },
        secondRow = {
            item(key = "infoBlock") {
                InfoBlock(data.commonInfo.toPersistentList(), data.aboutHtml)
            }
            item(key = "charts") {
                ChartList(data.stats)
            }
        }
    )
}

@Composable
private fun InfoBlock(commonInfo: ImmutableList<String>, aboutHtml: String?) {
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
    english: ImmutableList<String?>,
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "profile chat"
                )
                Text(profile_message.getLocalizableString(), textAlign = TextAlign.Center)
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
                            text = (if (friendState == true) profile_friend else profile_add_friend).getLocalizableString(),
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
                            text = (if (ignoreState == true) profile_ignore else profile_ignore_reset).getLocalizableString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
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
        state.setConfig(
            linkColor = Color.Blue.harmonize(
                MaterialTheme.colorScheme.onPrimaryContainer,
                matchSaturation = true
            )
        )
        var isVisible by remember { mutableStateOf(false) }
        LaunchedEffect(isVisible) {
            if (isVisible) {
                state.htmlToAnnotatedString(aboutHtml)
            } else {
                state.setText("...")
            }
        }
        Column(modifier = Modifier.noRippleClickable { isVisible = isVisible.not() }) {
            Text(
                text = profile_about.getLocalizableString(),
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
private fun CommonInfo(commonInfo: ImmutableList<String>) {
    Column {
        Text(
            text = profile_common_info.getLocalizableString(),
            style = MaterialTheme.typography.headlineSmall
        )
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
