package com.rcl.nextshiki.elements

/*

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun basicContentObject(value: ObjById) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        Compact ->
            contentMobileUI(value)

        else ->
            contentDesktopUI(value)
    }
}

@Composable
private fun contentMobileUI(value: ObjById) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            imageBlock(value)
            nameBlock(value)
            scoreBlock(value)
            statusObject(value)
            getDescription(value.description, value.descriptionSource)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun contentDesktopUI(value: ObjById) {
    FlowColumn(verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.Center) {
        Box {
            imageBlock(value)
        }
        Column {
            nameBlock(value)
            scoreBlock(value)
            statusObject(value)
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            getMarkdownedDescription(value.description, value.descriptionSource)
            franchiseBlock(value)
        }
    }
}

@Composable
private fun imageBlock(value: ObjById) = BoxWithConstraints {
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

@Composable
private fun nameBlock(value: ObjById) = Text(
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.padding(top = 16.dp),
    text = when (Locale.current.language) {
        "ru" ->
            value.russian.toString()

        else ->
            value.english.toString()
    },
    textAlign = TextAlign.Center
)

@Composable
private fun scoreBlock(value: ObjById) = Row(
    modifier = Modifier.padding(top = 16.dp),
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

@Composable
private fun statusObject(value: ObjById) = Row(
    modifier = Modifier.padding(top = 16.dp),
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

@Composable
private fun getMarkdownedDescription(description: String?, source: String?) = Box(modifier = Modifier.padding(top = 16.dp)) {
    val navigator = LocalNavigator.currentOrThrow
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = getString(description_in_object)
        )
        if (description != null) {
            val list = getMarkdownItemsFromString(description)
            val annotatedString = buildAnnotatedString {
                list.forEach { item ->
                    if (item is MarkdownTextItem) {
                        append(item.text)
                    }
                }
            }
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { span ->
                            Napier.i(span.tag)
                            navigator.push(SearchElementScreen(type = "characters", id = span.item))
                        }
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${getString(MR.strings.source)}: ${source ?: getString(MR.strings.unknown)}"
            )
        } else {
            Text(
                text = getString(text_empty)
            )
        }
    }
}

@Composable
private fun getDescription(description: String?, source: String?) = Box(modifier = Modifier.padding(top = 16.dp)) {
    val navigator = LocalNavigator.currentOrThrow
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = getString(description_in_object)
        )
        if (description != null) {
            val annotatedString = buildAnnotatedString {
                val listOpen = Regex("\\[character").findAll(description)
                    .map { it.range.first }
                    .toList()
                val listClose = Regex("""\[/character""".trimIndent()).findAll(description)
                    .map { it.range.first }
                    .toList()
                var tempValue = 0
                var lastOpen = 0

                listOpen.forEach {
                    append(description.substring(lastOpen, it - 1) + " ")
                    val closeIndex = listClose[tempValue]
                    tempValue++
                    val charId = description.substring(it, closeIndex).split("character=")[1].split("\\[")[0]
                    val name = description.substring(it, closeIndex).split("]")[1]
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        pushStringAnnotation(tag = "id", annotation = charId)
                        append(name)
                    }
                    lastOpen = closeIndex + """[\character]""".length
                }
                append(description.substring(lastOpen, description.lastIndex))
            }

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { span ->
                            Napier.i(span.tag)
                            navigator.push(SearchElementScreen(type = "characters", id = span.item))
                        }
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${getString(MR.strings.source)}: ${source ?: getString(MR.strings.unknown)}"
            )
        } else {
            Text(
                text = getString(text_empty)
            )
        }
    }
}

@Composable
fun franchiseBlock(value: ObjById) = Column {
    Text(
        text = getString(franchise)
    )
    val franchise = mutableStateOf<FranchiseModel?>(null)
    LaunchedEffect(Unit) {
        franchise.value = koin.get<KtorRepository>().getAnimeFranchise(value.id!!.toString())
    }
    if (franchise.value != null) {
        LazyRow {
            franchise.value!!.nodes.forEach { node ->
                item {
                    franchiseElement(node)
                }
            }
        }
    }
}

@Composable
fun franchiseElement(nodes: Nodes) {
    Box(modifier = Modifier.clip(RoundedCornerShape(10.dp))) {
        if (nodes.imageUrl != null) {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
                val painter = rememberImagePainter(nodes.imageUrl)
                Image(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                    painter = painter,
                    contentDescription = "Profile image"
                )
            }
        }
        if (nodes.name != null) {
            Text(
                text = nodes.name,
                textAlign = TextAlign.Right
            )
        }
    }
}*/