package com.premium.tvbrowser.data

import androidx.room.*

@Entity
data class Bookmark(@PrimaryKey val url: String, val title: String, val folder: String = "Favorites", val created: Long = System.currentTimeMillis())

@Dao
interface BookmarkDao { @Query("SELECT * FROM Bookmark") fun getAll(): List<Bookmark>; @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(b: Bookmark); @Delete fun delete(b: Bookmark) }

@Database(entities = [Bookmark::class], version = 1)
abstract class BrowserDatabase : RoomDatabase(){ abstract fun bookmarkDao(): BookmarkDao }
