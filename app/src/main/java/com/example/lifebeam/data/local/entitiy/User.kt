package com.example.lifebeam.data.local.entitiy

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey()
    @ColumnInfo(name = "uid")
    var uid: String = "",

    @ColumnInfo(name = "name")
    var name: String ="",

    @ColumnInfo(name = "email")
    var email: String ="",

    @ColumnInfo(name = "photo")
    var photo: String ="",

    @ColumnInfo(name = "phone")
    var phone: String ="",

    @ColumnInfo(name = "token")
    var token: String ="",

): Parcelable