package com.example.musicplayer.presentation._utils

import kotlinx.serialization.Serializable

private const val ROUTE_FILE_PICKER = "file_picker"
private const val ROUTE_PLAYER = "player/{itemId}"
private const val ROUTE_PLAYER_ITEM = "player/"
private const val ROUTE_QUEUE = "queue"

@Serializable
sealed class Screens(val route: String) {

    @Serializable
    data object FilePicker : Screens(ROUTE_FILE_PICKER)

    @Serializable
    data object Player : Screens(ROUTE_PLAYER) {
        const val ARGUMENT = "itemId"
    }

    @Serializable
    data class PlayerItem(val itemId: Int) : Screens(ROUTE_PLAYER_ITEM + itemId)

    @Serializable
    data object Queue : Screens(ROUTE_QUEUE)
}