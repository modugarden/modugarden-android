package com.example.modugarden.data

import androidx.room.*

@Entity
data class RecentSearch(
    val text: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recentSearch")
    fun getAll(): List<RecentSearch>

    @Insert
    fun insert(recentSearch: RecentSearch)

    @Insert
    fun insertAll(recentData: List<RecentSearch>)

    @Delete
    fun delete(recentData: RecentSearch)
}

@Database(entities = [RecentSearch::class], version = 1)
abstract class RecentSearchDatabase: RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}