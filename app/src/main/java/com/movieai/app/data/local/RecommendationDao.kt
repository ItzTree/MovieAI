package com.movieai.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.movieai.app.data.local.entity.RecommendationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {

    @Query("SELECT * FROM recommendations ORDER BY rank ASC")
    fun observeAll(): Flow<List<RecommendationEntity>>

    @Query("DELETE FROM recommendations")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecommendationEntity>)

    /** Atomic replace — wipes the previous run and inserts the fresh five. */
    @Transaction
    suspend fun replaceAll(items: List<RecommendationEntity>) {
        clear()
        insertAll(items)
    }
}
