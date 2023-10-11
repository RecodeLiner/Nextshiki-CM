package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR.strings.description_in_object
import com.rcl.nextshiki.MR.strings.score_in_object
import com.rcl.nextshiki.MR.strings.status_in_object
import com.rcl.nextshiki.MR.strings.text_empty
import com.rcl.nextshiki.generateImageLoader
import com.rcl.nextshiki.getString
import com.rcl.nextshiki.models.searchobject.CharacterModel
import com.rcl.nextshiki.models.searchobject.ObjById
import com.rcl.nextshiki.screens.search.searchelement.SearchElementScreen
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter

object ContentObject {
    @Composable
    fun CharacterContentObject(value: CharacterModel) {

    }
    @Composable
    fun CharMobileUI(value: CharacterModel){

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun BasicContentObject(value: ObjById) {
        val widthSizeClass = calculateWindowSizeClass().widthSizeClass
        when (widthSizeClass) {
            Compact ->
                mobileUI(value)

            else ->
                desktopUI(value)
        }
    }

    @Composable
    private fun mobileUI(value: ObjById) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                imageBlock(value)
                textBlock(value)
                scoreBlock(value)
                statusObject(value)
                getDescription(value.description)
            }
        }
    }

    @Composable
    private fun desktopUI(value: ObjById) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth().weight(1f)) {
                imageBlock(value)
                textBlock(value)
                scoreBlock(value)
                statusObject(value)
            }
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                item {
                    getDescription(value.description)
                }
            }
        }
    }

    @Composable
    private fun imageBlock(value: ObjById) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
                val painter = rememberImagePainter(BuildConfig.DOMAIN + value.image!!.original!!)
                Image(
                    modifier = Modifier.width(maxWidth / 2)
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.Center),
                    painter = painter,
                    contentDescription = "Profile image"
                )
            }
        }
    }

    @Composable
    private fun textBlock(value: ObjById) {
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                text = when (Locale.current.language) {
                    "ru" ->
                        value.russian.toString()

                    else ->
                        value.english.toString()
                },
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun scoreBlock(value: ObjById) {
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = getString(score_in_object)
                )
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = value.score!!,
                )
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Star icon in content"
                )
            }
        }
    }

    @Composable
    private fun statusObject(value: ObjById) {
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = getString(status_in_object)
                )
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = value.status!!,
                )
            }
        }
    }

    @Composable
    private fun getDescription(value: String?) {
        val navigator = LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = getString(description_in_object)
                )
                if (value != null) {
                    val annotatedString = buildAnnotatedString {
                        val listOpen = Regex("\\[character").findAll(value)
                            .map { it.range.first }
                            .toList()
                        val listClose = Regex("""\[/character""".trimIndent()).findAll(value)
                            .map { it.range.first }
                            .toList()
                        var tempValue = 0
                        var lastOpen = 0

                        listOpen.forEach {
                            append(value.substring(lastOpen, it - 1)+" ")
                            val closeIndex = listClose[tempValue]
                            tempValue++
                            val charId = value.substring(it, closeIndex).split("character=")[1].split("\\[")[0]
                            val name = value.substring(it, closeIndex).split("]")[1]
                            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                pushStringAnnotation(tag = "id", annotation = charId)
                                append(name)
                            }
                            lastOpen = closeIndex+"""[\character]""".length
                        }
                        append(value.substring(lastOpen, value.lastIndex))
                    }

                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(offset, offset)
                                .firstOrNull()?.let { span ->
                                    navigator.push(SearchElementScreen(type = "characters", id = span.item))
                                }
                        },
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                } else {
                    Text(
                        text = getString(text_empty)
                    )
                }
            }
        }
    }
}