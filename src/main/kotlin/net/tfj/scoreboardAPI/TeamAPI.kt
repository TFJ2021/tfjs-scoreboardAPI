package net.tfj.scoreboardAPI

import net.kyori.adventure.text.minimessage.MiniMessage
import net.tfj.scoreboardAPI.entities.TeamData
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard
import java.util.*

/**
 * Team management class. This classes automatically registers itself as listener
 *
 * @param plugin instance of [JavaPlugin]
 * @param miniMessage optional [MiniMessage] instance
 * @since 1.3
 */
class TeamAPI(
    val plugin: JavaPlugin,
    val miniMessage: MiniMessage = MiniMessage.miniMessage()
) : Listener {

    private val playerTeams: HashMap<UUID, TeamData> = hashMapOf()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    /**
     * Sets a team for a player and synchronizes it across all players
     * @param player the player
     * @param teamData the team data
     */
    fun setTeam(player: Player, teamData: TeamData) {
        playerTeams[player.uniqueId] = teamData
        syncPlayerToAll(player)
    }

    /**
     * Removes a player from their team
     * @param player the player
     */
    fun removeTeam(player: Player) {
        playerTeams.remove(player.uniqueId)
        removeFromAll(player)
    }

    /**
     * Synchronizes a player's team to everyone's scoreboard
     */
    private fun syncPlayerToAll(target: Player) {
        val teamData = playerTeams[target.uniqueId] ?: return
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            updateTeamInScoreboard(onlinePlayer.scoreboard, target, teamData)
        }
    }

    /**
     * Synchronizes all existing teams to a specific scoreboard (e.g. when a player joins or switches scoreboards)
     */
    fun syncAllToScoreboard(scoreboard: Scoreboard) {
        for ((uuid, teamData) in playerTeams) {
            val target = Bukkit.getPlayer(uuid) ?: continue
            updateTeamInScoreboard(scoreboard, target, teamData)
        }
    }

    private fun updateTeamInScoreboard(scoreboard: Scoreboard, target: Player, teamData: TeamData) {
        var team = scoreboard.getTeam(teamData.teamName)
        if (team == null) {
            team = scoreboard.registerNewTeam(teamData.teamName)
        }

        team.displayName(miniMessage.deserialize(teamData.displayName))
        team.prefix(miniMessage.deserialize(teamData.prefix))
        team.suffix(miniMessage.deserialize(teamData.suffix))
        team.color(teamData.color)

        if (!team.hasEntry(target.name)) {
            // Remove from other teams in this scoreboard first
            scoreboard.getEntryTeam(target.name)?.removeEntry(target.name)
            team.addEntry(target.name)
        }
    }

    private fun removeFromAll(target: Player) {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            onlinePlayer.scoreboard.getEntryTeam(target.name)?.removeEntry(target.name)
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        syncAllToScoreboard(event.player.scoreboard)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        playerTeams.remove(event.player.uniqueId)
        removeFromAll(event.player)
    }
}
