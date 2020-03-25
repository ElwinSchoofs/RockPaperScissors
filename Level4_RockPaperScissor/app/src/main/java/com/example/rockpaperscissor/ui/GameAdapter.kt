package com.example.rockpaperscissor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissor.R
import com.example.rockpaperscissor.model.Game
import kotlinx.android.synthetic.main.game_item.view.*

class GameAdapter (private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(game: Game) {
            itemView.tvResult.text = game.result
            itemView.ivComputer.setImageDrawable(getDrawable(itemView.context, game.computerAction.drawableId))
            itemView.ivYou.setImageDrawable(getDrawable(itemView.context, game.userAction.drawableId))
            itemView.tvDate.text = game.date.toString()
        }
    }
}