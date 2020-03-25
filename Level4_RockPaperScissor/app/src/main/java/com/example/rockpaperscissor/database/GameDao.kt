package com.example.rockpaperscissor.database

import androidx.room.*
import com.example.rockpaperscissor.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable ORDER BY date DESC")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'You win!'")
    suspend fun getWins(): Int

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'Draw'")
    suspend fun getDraws(): Int

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'Computer wins!'")
    suspend fun getLosses(): Int
}
