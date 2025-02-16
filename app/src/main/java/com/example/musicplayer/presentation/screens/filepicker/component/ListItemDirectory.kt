package com.example.musicplayer.presentation.screens.filepicker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import java.io.File

@Composable
fun ListItemDirectory(directory: File, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = {
            Text(
                text = directory.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Image(
                painter = painterResource(R.drawable.outline_folder_24),
                contentDescription = "Directory icon",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        })
}

@Preview
@Composable
private fun ListItemDirectoryPreview() {
    MusicPlayerTheme {
        Surface {
            ListItemDirectory(File("Music")) {}
        }
    }
}