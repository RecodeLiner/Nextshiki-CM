package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object SelectableRowElementObject {
    @Composable
    fun SelectableRowElement(
        selected: Boolean,
        text: String,
        modifier: Modifier = Modifier
    ){
        Card(
            modifier = Modifier.then(modifier),
            colors = CardDefaults.cardColors(
                containerColor = if (selected)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp).padding(horizontal = 8.dp),
                text = text
            )
        }
    }
}