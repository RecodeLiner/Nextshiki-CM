package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.models.searchobject.SearchCardModel

@Composable
fun SearchScreenCardList(
    state: LazyStaggeredGridState,
    list: List<SearchCardModel>,
    onClick: (id: Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = (8.dp),
    ) {
        items(list) { listItem ->
            SearchCard(
                modifier = Modifier
                    .noRippleClickable {
                        listItem.id?.let { id ->
                            onClick(id)
                        }
                    },
                content = listItem,
            )
        }
    }
}