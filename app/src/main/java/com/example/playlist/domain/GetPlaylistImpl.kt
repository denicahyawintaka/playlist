package com.example.playlist.domain

import com.example.playlist.model.repository.PlaylistRepository
import com.example.quipper.model.entity.Course
import io.reactivex.Completable
import io.reactivex.Single

class GetPlaylistImpl(private val playlistRepository: PlaylistRepository): GetPlaylist  {
    override fun execute(): Single<List<Course>> {
        return playlistRepository.getPlaylist()
    }

}