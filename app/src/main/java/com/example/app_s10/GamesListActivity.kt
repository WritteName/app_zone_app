package com.example.app_s10

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_s10.databinding.ActivityGamesListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GamesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gameAdapter: GameAdapter
    private lateinit var database: DatabaseReference
    private val gameList = mutableListOf<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Estilo visual del RecyclerView
        gameAdapter = GameAdapter(gameList)
        binding.recyclerViewGames.adapter = gameAdapter
        binding.recyclerViewGames.layoutManager = LinearLayoutManager(this)

        // Listener para eliminar juego con long click
        gameAdapter.onDeleteClick = { game ->
            confirmDeleteGame(game)
        }

        // Inicializar Firebase Database (nodo "games" del usuario actual)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            database = FirebaseDatabase.getInstance().reference.child("games").child(uid)
            loadGamesFromFirebase()
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadGamesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gameList.clear()
                for (gameSnapshot in snapshot.children) {
                    val game = gameSnapshot.getValue(Game::class.java)
                    game?.id = gameSnapshot.key  // Guardar el ID del juego
                    game?.let { gameList.add(it) }
                }
                gameAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GamesListActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmDeleteGame(game: Game) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar juego")
            .setMessage("¿Deseas eliminar \"${game.title}\"?")
            .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                deleteGameFromFirebase(game)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteGameFromFirebase(game: Game) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val gameId = game.id

        if (uid != null && gameId != null) {
            val gameRef = FirebaseDatabase.getInstance().reference
                .child("games")
                .child(uid)
                .child(gameId)

            gameRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Juego eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error de autenticación o ID", Toast.LENGTH_SHORT).show()
        }
    }
}
