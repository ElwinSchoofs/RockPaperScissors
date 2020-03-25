package com.example.rockpaperscissor.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rockpaperscissor.Gesture
import com.example.rockpaperscissor.R
import com.example.rockpaperscissor.database.GameRepository
import com.example.rockpaperscissor.model.Game

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private val requestCode = 1

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        gameRepository = GameRepository(this)
        initViews()
    }

    private fun initViews() {
        initListeners()
        updateViews()
    }

    private fun updateViews() {
        CoroutineScope(Dispatchers.Main).launch {
            val wins = withContext(Dispatchers.IO) {
                gameRepository.getWins()
            }
            val draws = withContext(Dispatchers.IO) {
                gameRepository.getDraws()
            }
            val losses = withContext(Dispatchers.IO) {
                gameRepository.getLosses()
            }
            tvStats.text = getString(
                R.string.win_draw_lose,
                wins.toString(),
                draws.toString(),
                losses.toString()
            )
        }
    }

    private fun initListeners() {
        ibtnHistory.setOnClickListener {
            startActivity()
        }

        ibtnRock.setOnClickListener { onPlayButtonClicked(Gesture.ROCK) }
        ibtnPaper.setOnClickListener { onPlayButtonClicked(Gesture.PAPER) }
        ibtnScissor.setOnClickListener { onPlayButtonClicked(Gesture.SCISSOR) }
    }

    private fun startActivity() {
        val intent = Intent(this, GameHistoryActivity::class.java)
        startActivityForResult(intent, requestCode)
    }

    private fun onPlayButtonClicked(gesture: Gesture) {
        val computerGesture = assignGesture()
        var game = Game(
            null,
            "",
            Date(),
            gesture,
            computerGesture
        )

        game = calculateWinner(game)
        insertGameIntoDatabase(game)
        displayGameResults(game)
        updateViews()
    }


    private fun insertGameIntoDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }

    private fun assignGesture(): Gesture {
        return when ((0..3).shuffled().first()) {
            0 -> Gesture.ROCK
            1 -> Gesture.PAPER
            3 -> Gesture.SCISSOR
            else -> Gesture.ROCK
        }
    }

    private fun calculateWinner(game: Game): Game {
        game.result = when {
            game.computerAction == game.userAction -> getString(R.string.draw)
            game.computerAction == Gesture.ROCK && game.userAction == Gesture.SCISSOR -> getString(
                R.string.computer_win
            )
            game.computerAction == Gesture.SCISSOR && game.userAction == Gesture.PAPER -> getString(
                R.string.computer_win
            )
            game.computerAction == Gesture.PAPER && game.userAction == Gesture.ROCK -> getString(
                R.string.computer_win
            )
            else -> getString(R.string.you_win)
        }
        return game
    }

    private fun displayGameResults(game: Game) {
        tvResult.text = game.result
        ivComputer.setImageDrawable(getDrawable(game.computerAction.drawableId))
        ivYou.setImageDrawable(getDrawable(game.userAction.drawableId))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateViews()
        resetGameResults()
    }

    private fun resetGameResults() {
        tvResult.text = getString(R.string.initial_result)
        ivComputer.setImageDrawable(getDrawable(Gesture.PAPER.drawableId))
        ivYou.setImageDrawable(getDrawable(Gesture.PAPER.drawableId))
    }
}
