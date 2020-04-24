package com.example.quipper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey
    val title: String,
    val presenter_name:String,
    val description: String,
    val thumbnail_url: String,
    val video_url: String,
    val video_duration: Int
)