package com.locked.shingranicommunity.tutorials

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE _id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username LIKE :username AND " +
            "name LIKE :name LIMIT 1")
    fun findByName(username: String, name: String): User

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}