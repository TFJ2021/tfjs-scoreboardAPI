package net.tfj.scoreboardAPI.examples

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import net.tfj.scoreboardAPI.ScoreboardAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    // Variables
    companion object {
        lateinit var scoreboardAPI: ScoreboardAPI
    }

    // On enable
    override fun onEnable() {
        // Creates new instance of ScoreboardAPI
        scoreboardAPI = ScoreboardAPI(this, ReadMeScoreboard)

        // Registers this class as listener
        server.pluginManager.registerEvents(this, this)
    }

    // On disable
    override fun onDisable() {
        // Plugin shutdown logic
    }

    // On player join
    @EventHandler
    fun join(event: PlayerJoinEvent) {
        scoreboardAPI.resetScoreboard(event.player)
    }

    // Not and good example but you can switch the scoreboard
    @EventHandler
    fun join(event: PlayerJumpEvent) {
        val player = event.player
        if (scoreboardAPI.getScoreboard(player) == ExampleScoreboard) {
            scoreboardAPI.setScoreboard(player, ReadMeScoreboard)
        } else {
            scoreboardAPI.setScoreboard(player, ExampleScoreboard)
        }
    }
}
