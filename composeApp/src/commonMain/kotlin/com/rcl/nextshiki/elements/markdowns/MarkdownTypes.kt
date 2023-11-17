package com.rcl.nextshiki.elements.markdowns

sealed class MarkdownTag()

sealed class SimpleTag() : MarkdownTag()

sealed class TextSize(): SimpleTag() {
    data object Regular : TextSize()

}

sealed class TextFormat() : SimpleTag() {

}

enum class MonospaceCodeBlockLanguage {
    None, Java, Kotlin, CSS, HTML, JavaScript, C, CSharp, CPlusPlus // etc,
}

sealed class TextType() : SimpleTag() {
    data object Text : TextType()
    //  data object Link
    data class Character(val id: MarkdownTagIntTypeParameter) : TextType()
}

sealed class DisplayAddition(): SimpleTag() {

}
sealed interface MarkdownTagParameter

// tagParameter=455
data class MarkdownTagIntTypeParameter(val key: String, val toInt: Int) : MarkdownTagParameter

// tagParameter="example"
data class MarkdownTagStringTypeParameter(val key: String, val value: String) : MarkdownTagParameter

// tagParameter=true
data class MarkdownTagBooleanTypeParameter(val key: String, val value: Boolean) : MarkdownTagParameter

// tagParameter=["100", "something"]
data class MarkdownTagStringArrayTypeParameter(val key: String, val arrayItems: List<String>) : MarkdownTagParameter

data class MarkdownTextItem(
    val text: String,
    val size: TextSize,
    val type: TextType,
    val formats: List<TextFormat>,
    val displayAdditions: List<DisplayAddition>
) : MarkdownItem

sealed interface MarkdownItem
