package com.example.playlist.domain

import io.reactivex.Completable

interface FetchPlaylist {
    fun execute():Completable
}