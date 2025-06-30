package com.example.app_s10

import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.RatingBar
import android.view.ViewGroup
import android.view.View

class GameAdapter(private var games: List<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {
    var onDeleteClick: ((Game) -> Unit)? = null
    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvGameTitle)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGameGenre)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title
        holder.tvGenre.text = game.genre
        holder.ratingBar.rating = game.rating
        holder.itemView.setOnLongClickListener {
            onDeleteClick?.invoke(games[position])
            true
        }
    }

    override fun getItemCount() = games.size

    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}