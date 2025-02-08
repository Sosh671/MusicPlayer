package com.example.musicplayer.presentation.screens.main.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicplayer.presentation.model.PagerScreen
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import kotlinx.coroutines.launch

@Composable
fun MusicPlayerPager(paddings: PaddingValues, pages: List<PagerScreen>) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings)
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val index = selectedTabIndex.value
            if (index >= 0 && index < pages.size) {
                pages[index].screenContent()
            }
        }
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            pages.forEachIndexed { index, currentTab ->
                Tab(selected = selectedTabIndex.value == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedTabIndex.value == index) currentTab.selectedIcon else currentTab.unselectedIcon,
                            contentDescription = "Pager tab icon"
                        )
                    })
            }
        }
    }
}

@Preview
@Composable
fun PreviewMusicPlayerPager() {
    val content = @Composable {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Preview MusicPlayerPager")
        }
    }
    MusicPlayerTheme {
        Surface {
            MusicPlayerPager(
                paddings = PaddingValues(),
                pages = listOf(
                    PagerScreen(
                        unselectedIcon = Icons.Outlined.ShoppingCart,
                        selectedIcon = Icons.Filled.ShoppingCart,
                        screenContent = content
                    ),
                    PagerScreen(
                        unselectedIcon = Icons.Outlined.FavoriteBorder,
                        selectedIcon = Icons.Filled.Favorite,
                        screenContent = content
                    ),
                    PagerScreen(
                        unselectedIcon = Icons.Outlined.Person,
                        selectedIcon = Icons.Filled.Person,
                        screenContent = content
                    )
                )
            )
        }
    }
}