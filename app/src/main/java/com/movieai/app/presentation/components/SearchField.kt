package com.movieai.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

/**
 * Dark search field used at the top of the Search screen.
 * Debouncing belongs in the ViewModel — this component just emits raw changes.
 */
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "제목, 배우, 장르",
) {
    Surface(
        modifier = modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(14.dp),
        color = MovieAiColors.surface,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Spacer(Modifier.width(14.dp))
            Icon(Icons.Default.Search, null, tint = MovieAiColors.textDim, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MovieAiColors.text),
                cursorBrush = SolidColor(MovieAiColors.primary),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(placeholder, color = MovieAiColors.textDim, style = MaterialTheme.typography.bodyLarge)
                    }
                    inner()
                },
            )
            if (query.isNotEmpty()) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "검색어 지우기",
                    tint = MovieAiColors.textDim,
                    modifier = Modifier
                        .clickable { onQueryChange("") }
                        .padding(4.dp)
                        .size(18.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewSearchFieldEmpty() {
    MovieAiTheme {
        val q = remember { mutableStateOf("") }
        SearchField(q.value, { q.value = it }, modifier = Modifier.padding(20.dp))
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewSearchFieldFilled() {
    MovieAiTheme {
        val q = remember { mutableStateOf("기생충") }
        SearchField(q.value, { q.value = it }, modifier = Modifier.padding(20.dp))
    }
}
