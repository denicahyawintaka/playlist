package com.example.playlist.main

sealed class MainIntent {
    object LoadPlaylistIntent : MainIntent()
    object RefreshPlaylistIntent : MainIntent()
}