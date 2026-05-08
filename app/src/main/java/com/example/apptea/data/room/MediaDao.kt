package com.example.apptea.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items WHERE categoryId = :categoryId")
    fun getMediaByCategory(categoryId: String): Flow<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: MediaEntity): Unit

    @Update
    suspend fun updateMedia(media: MediaEntity): Unit

    @Delete
    suspend fun deleteMedia(media: MediaEntity): Unit

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deleteById(id: String): Unit
}
