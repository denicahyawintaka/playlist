package com.example.playlist.domain

import com.example.playlist.model.repository.PlaylistRepository
import io.reactivex.Completable

class FetchPlaylistImpl(private val playlistRepository: PlaylistRepository): FetchPlaylist  {
    override fun execute():Completable {
        return playlistRepository.fetchPlaylist()
    }
}