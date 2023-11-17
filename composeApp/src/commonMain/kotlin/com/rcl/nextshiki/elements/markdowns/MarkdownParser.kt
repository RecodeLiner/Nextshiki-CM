package com.rcl.nextshiki.elements.markdowns

import io.github.aakira.napier.Napier

fun getMarkdownItemsFromString(sourceText: String): List<MarkdownItem> {
    val listOfCharacters = mutableListOf<MarkdownTextItem>()

    var currentlyProcessingAStartingTag = false
    var currentlyProcessingAnEndingTag = false
    val currentFoundStartingTagStringBuilder = StringBuilder()
    val currentFoundEndingTagStringBuilder = StringBuilder()

    val currentTagsInProcessing = mutableListOf<String>()

    fun processTaggedContent(taggedContent: String) {
        var contentsAssignedSize: TextSize = TextSize.Regular
        var contentsAssignedType: TextType = TextType.Text
        val contentsAssignedFormats = mutableListOf<TextFormat>()
        val contentsAssignedAdditions = mutableListOf<DisplayAddition>()

        currentTagsInProcessing.forEach {
            when (val tag = convertTagFromText(it)) {
                is TextSize -> contentsAssignedSize = tag
                is TextFormat -> contentsAssignedFormats.add(tag)
                is DisplayAddition -> contentsAssignedAdditions.add(tag)
                is TextType -> contentsAssignedType = tag
                null -> { }
            }
        }

        taggedContent.forEach {
            listOfCharacters.add(
                MarkdownTextItem(
                    it.toString(),
                    contentsAssignedSize,
                    contentsAssignedType,
                    contentsAssignedFormats,
                    contentsAssignedAdditions,
                )
            )
        }
    }

    sourceText.forEachIndexed { index, character ->
        val nextCharacter = sourceText.getOrNull(index + 1)

        if (!currentlyProcessingAStartingTag && !currentlyProcessingAnEndingTag && character != '[') {
            processTaggedContent(character.toString())
        }

        if (currentlyProcessingAStartingTag && character != ']') currentFoundStartingTagStringBuilder.append(character)

        if (character == '[' && nextCharacter != '/') currentlyProcessingAStartingTag = true

        if (character == ']' && currentlyProcessingAStartingTag) {
            currentlyProcessingAStartingTag = false
            currentTagsInProcessing.add(currentFoundStartingTagStringBuilder.toString())
            currentFoundStartingTagStringBuilder.clear()
            Napier.i("tagh" + currentTagsInProcessing.joinToString())
        }

        if (character == '[' && nextCharacter == '/') currentlyProcessingAnEndingTag = true

        if (character == ']' && currentlyProcessingAnEndingTag) {
            currentlyProcessingAnEndingTag = false

            val foundEndingTag = currentFoundEndingTagStringBuilder.toString()
            Napier.i("tagh" + foundEndingTag + "being removed")

            val matchingStartingTag = currentTagsInProcessing.findLast { it.startsWith(foundEndingTag) }

            if (matchingStartingTag != null) {
                currentTagsInProcessing.remove(matchingStartingTag)
            }

            currentFoundEndingTagStringBuilder.clear()
            Napier.i("tagh" + currentTagsInProcessing.joinToString())
        }
    }

    return listOfCharacters
}

fun convertTagFromText(tag: String): MarkdownTag? = when {
    tag.startsWith("character") -> {
        val parameter = processParametersFromString(tag).first() as MarkdownTagIntTypeParameter

        TextType.Character(parameter)
    }
    else -> null
}

fun processParametersFromString(rawParametersString: String): List<MarkdownTagParameter> {
    val parameters = mutableListOf<MarkdownTagParameter>()

    val rawParameterKeyValuesStrings = rawParametersString.split(",")

    rawParameterKeyValuesStrings.forEach { str ->
        val (key, value) = str.split("=")

        if (value.all { char -> char.isDigit() }) {
            parameters.add(MarkdownTagIntTypeParameter(key, value.toInt()))
        } else if (value == "true" || value == "false") {
            parameters.add(MarkdownTagBooleanTypeParameter(key, value.toBoolean()))
        } else if (value.startsWith("[")) {
            val arrayItems = value.substring(0, value.lastIndex).split(" ").map { arrayItem ->
                arrayItem.removeSurrounding(""""""")
            }
            parameters.add(MarkdownTagStringArrayTypeParameter(key, arrayItems))
        } else {
            parameters.add(MarkdownTagStringTypeParameter(key, value))
        }
    }

    return parameters
}
