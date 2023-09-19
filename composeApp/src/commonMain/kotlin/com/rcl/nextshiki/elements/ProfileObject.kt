package com.rcl.nextshiki.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.*
import com.rcl.nextshiki.MR.strings.not_enabled_by_scope
import com.rcl.nextshiki.MR.strings.profile_actions
import com.rcl.nextshiki.MR.strings.profile_add_friend
import com.rcl.nextshiki.MR.strings.profile_friend
import com.rcl.nextshiki.MR.strings.profile_message
import com.rcl.nextshiki.di.ktor.KtorModel.scope
import com.rcl.nextshiki.models.usermodel.Userdata
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.DelicateCoroutinesApi

object ProfileObject {
    @OptIn(ExperimentalAnimationApi::class)
    @DelicateCoroutinesApi
    @Composable
    fun ProfileObject(
        isCurrentUser: Boolean = false,
        value: Userdata,
        padding: PaddingValues = PaddingValues(),
        modifier: Modifier = Modifier,
        addToFriend: () -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.padding(padding).then(modifier)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CompositionLocalProvider(
                        LocalImageLoader provides remember { generateImageLoader() },
                    ) {
                        val painter = rememberImagePainter(value.image!!.x160!!)
                        Image(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .align(Center),
                            painter = painter,
                            contentDescription = "Profile image"
                        )
                    }
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Center)
                    ) {
                        Text(
                            text = value.lastOnline!!.upper(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                vertical = 12.dp,
                                horizontal = 24.dp
                            ),
                        )
                    }
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(top = 36.dp, start = 28.dp),
                        text = getString(profile_actions)
                    )
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .height(90.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                bottomStart = 20.dp,
                                topEnd = 4.dp,
                                bottomEnd = 4.dp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .noRippleClickable {

                                },
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Chat,
                                    contentDescription = "Chat icon"
                                )
                                Text(
                                    text = getString(profile_message)
                                )
                            }
                        }
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .noRippleClickable {
                                    if (scope.value.contains("friends")){
                                        addToFriend()
                                    }
                                }
                        ) {
                            AnimatedContent(
                                targetState = value.inFriends!!,
                                modifier = Modifier.fillMaxSize(),
                                label = ""
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = if (it) {
                                            Icons.Filled.Person
                                        } else {
                                            Icons.Outlined.PersonAdd
                                        },
                                        contentDescription = "Friend icon"
                                    )
                                    Text(
                                        text = getString(
                                            if (it) {
                                                profile_friend
                                            } else {
                                                profile_add_friend
                                            }
                                        )
                                    )
                                    if (!scope.value.contains("friends")){
                                        Text(
                                            text = getString(
                                                not_enabled_by_scope
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Card(
                            shape = RoundedCornerShape(
                                topEnd = 20.dp,
                                bottomEnd = 20.dp,
                                topStart = 4.dp,
                                bottomStart = 4.dp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.VisibilityOff,
                                    contentDescription = "Ignore icon"
                                )
                                Text(
                                    text = getString(MR.strings.profile_ignore)
                                )
                            }
                        }
                    }
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(top = 12.dp, start = 28.dp),
                        text = getString(MR.strings.profile_information)
                    )
                }
            }
            //TODO: Доделать эту недоделку
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp)
                            .height(90.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Card {

                            }
                        }
                    }
                }
            }
        }
    }
}