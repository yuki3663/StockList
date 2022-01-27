package com.tom.stocktable.database.account

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Query("SELECT * from account WHERE email = :email")
    fun getAccount(email: String): Flow<Account>
}