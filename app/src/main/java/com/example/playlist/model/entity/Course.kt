package com.example.quipper.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Course(
    @PrimaryKey
    val title: String,
    val presenter_name:String,
    val description: String,
    val thumbnail_url: String,
    val video_url: String,
    val video_duration: Int
): Parcelable