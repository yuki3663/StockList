package com.tom.stocktable.database.account

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey
    val email: String,
    @NonNull
    val password: String,
    @NonNull @ColumnInfo(name = "user_name")
    val userName: String
)
