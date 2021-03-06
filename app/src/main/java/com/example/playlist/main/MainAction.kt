package com.example.playlist.main

sealed class MainAction {
    object LoadPlaylistAction : MainAction()
    object RefreshPlaylistAction : MainAction()
}