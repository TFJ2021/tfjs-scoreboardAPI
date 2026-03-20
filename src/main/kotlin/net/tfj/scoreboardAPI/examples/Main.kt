package net.tfj.scoreboardAPI.examples

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import net.tfj.scoreboardAPI.ScoreboardAPI
import net.tfj.scoreboardAPI.TeamAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    // Variables
    companion object {
        lateinit var scoreboardAPI: ScoreboardAPI
        lateinit var teamAPI: TeamAPI
    }

    // On enable
    override fun onEnable() {
        // Creates new instance of TeamAPI
        teamAPI = TeamAPI(this)

        // Creates new instance of ScoreboardAPI
        scoreboardAPI = ScoreboardAPI(this, ReadMeScoreboard, teamAPI)

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

        // Sets default team
        if (event.player.isOp) {
            teamAPI.setTeam(event.player, ExampleTeams.AdminTeam)
        } else {
            teamAPI.setTeam(event.player, ExampleTeams.PlayerTeam)
        }
    }

    // Switch the scoreboard when player jumps
    @EventHandler
    fun jump(event: PlayerJumpEvent) {
        val player = event.player
        if (scoreboardAPI.getScoreboard(player) == ExampleScoreboard) {
            //scoreboardAPI.setScoreboard(player, ReadMeScoreboard)
            
            // Example how easy it is to change the team for a player
            teamAPI.setTeam(player, ExampleTeams.PremiumTeam)
        } else {
            scoreboardAPI.setScoreboard(player, ExampleScoreboard)
            
            // Revert team
            if (player.isOp) teamAPI.setTeam(player, ExampleTeams.AdminTeam) else teamAPI.setTeam(player, ExampleTeams.PlayerTeam)
        }
    }
}
