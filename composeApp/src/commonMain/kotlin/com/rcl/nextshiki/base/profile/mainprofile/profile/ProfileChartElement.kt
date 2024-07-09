package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.harmonize
import com.rcl.mr.SharedRes.strings.profile_charts
import com.rcl.mr.SharedRes.strings.profile_rating
import com.rcl.mr.SharedRes.strings.profile_scores
import com.rcl.mr.SharedRes.strings.profile_statuses
import com.rcl.mr.SharedRes.strings.profile_types
import com.rcl.mr.SharedRes.strings.search_anime
import com.rcl.mr.SharedRes.strings.search_manga
import com.rcl.nextshiki.elements.VerticalRoundedCornerShape
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.searchobject.users.Stats
import dev.icerock.moko.resources.StringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

@Composable
internal fun ChartList(stats: Stats?) {
    Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                profile_charts.getLocalizableString(),
                style = MaterialTheme.typography.headlineSmall
            )
            ChartRow(
                shape = VerticalRoundedCornerShape(
                    top = 20.dp,
                    bottom = 4.dp
                ),
                typeTitle = profile_scores,
                animeChart = stats?.scores?.anime?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.scores?.manga?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = RoundedCornerShape(4.dp),
                typeTitle = profile_statuses,
                animeChart = stats?.statuses?.anime?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.size })
                    ?: persistentListOf(),
                mangaChart = stats?.statuses?.manga?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.size })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = RoundedCornerShape(4.dp),
                typeTitle = profile_types,
                animeChart = stats?.types?.anime?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.types?.manga?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
            ChartRow(
                shape = VerticalRoundedCornerShape(
                    top = 4.dp,
                    bottom = 20.dp
                ),
                typeTitle = profile_rating,
                animeChart = stats?.ratings?.anime?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf(),
                mangaChart = stats?.ratings?.manga?.toPersistentList()
                    ?.toChartElement({ it.name }, { it.value })
                    ?: persistentListOf()
            )
        }
    }
}

@Composable
private fun ChartRow(
    shape: Shape,
    typeTitle: StringResource,
    animeChart: ImmutableList<ChartElement>,
    mangaChart: ImmutableList<ChartElement>
) {
    var enabled by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors()
            .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary)),
        shape = shape
    ) {

        AnimatedContent(enabled) { isEnabled ->
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(10.dp).noRippleClickable { enabled = enabled.not() }
                    .fillMaxWidth()
            ) {
                Text(
                    "${typeTitle.getLocalizableString()} ${if (!isEnabled) "..." else ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (isEnabled) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        if (animeChart.isNotEmpty()) {
                            ProfileChartElement(
                                anime = animeChart,
                                modifier = Modifier.weight(1f).padding(5.dp),
                                title = search_anime
                            )
                        }
                        if (mangaChart.isNotEmpty()) {
                            ProfileChartElement(
                                anime = mangaChart,
                                modifier = Modifier.weight(1f).padding(5.dp),
                                title = search_manga
                            )
                        }
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
    title: StringResource,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title.getLocalizableString(), style = MaterialTheme.typography.bodyMedium)
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
                            text = (" - ${chartElement.name}: " +
                                    "${(chartElement.percent * 100 * 10).roundToInt() / 10.0}%"
                                    ),
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
            (nameSelector(contentScore)?.toColorAsSeed()
                ?.harmonize(MaterialTheme.colorScheme.primary)
                ?: Color.Transparent)
        )
    }.toPersistentList()
}
