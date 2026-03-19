package net.tfj.scoreboardAPI

import net.kyori.adventure.text.minimessage.MiniMessage
import net.tfj.scoreboardAPI.entities.LineBaseEntry
import net.tfj.scoreboardAPI.entities.OptionalLineEntry
import net.tfj.scoreboardAPI.entities.ScoreboardData
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

/**
 * Main Scoreboard management class
 *
 * @param plugin instance of [JavaPlugin]
 * @param defaultBoard default [ScoreboardData]. This will not be applied automatically
 * @param miniMessage optional [MiniMessage] instance. Falls back to miniMessage default
 * @since 1.0
 */
class ScoreboardAPI(
    val plugin: JavaPlugin,
    val defaultBoard: ScoreboardData,
    val miniMessage: MiniMessage = MiniMessage.miniMessage()
) {

    // Static variables
    companion object {
        private var map: HashMap<UUID, ScoreboardData> = hashMapOf()
        private var visibilityMap: HashMap<UUID, List<Boolean>> = hashMapOf()
        lateinit var miniMessage: MiniMessage
    }

    // On class init
    init {
        Companion.miniMessage = this.miniMessage
        startUpdater()
    }

    /**
     * Sets default scoreboard for [player]
     * @param player the player that should be reset
     * @since 1.0
     */
    fun resetScoreboard(player: Player) {
        setScoreboard(player, defaultBoard)
    }

    /**
     * Sets scoreboard for specified [player]
     * @param player the player that the scoreboard should be set
     * @param scoreboardData data entity containing the data about the scoreboard
     * @since 1.0
     */
    fun setScoreboard(player: Player, scoreboardData: ScoreboardData) {
        map[player.uniqueId] = scoreboardData
        player.scoreboard = createScoreboard(player, scoreboardData)
    }

    /**
     * Gets [ScoreboardData]
     *
     * @return possibly null [ScoreboardData] entity.
     * @since 1.0
     */
    fun getScoreboard(player: Player): ScoreboardData? {
        return map[player.uniqueId]
    }

    // Creates new scoreboard
    private fun createScoreboard(player: Player, data: ScoreboardData): Scoreboard {
        val board = Bukkit.getScoreboardManager().newScoreboard

        // Creates viewboard
        val objective = board.registerNewObjective("viewboard", Criteria.DUMMY, miniMessage.deserialize(data.title))

        // Sets display slot
        objective.displaySlot = DisplaySlot.SIDEBAR

        // Force set/update
        applyData(player, board, data, true)

        // Return
        return board
    }

    /**
     * Applies [data] to specified [scoreboard] for specified [player]
     *
     * @param player is required for data frames
     * @param forceUpdate if everything should be updated regardless of the update frame
     * @since 1.0
     */
    private fun applyData(player: Player, scoreboard: Scoreboard, data: ScoreboardData, forceUpdate: Boolean = false) {
        val objective = scoreboard.getObjective(DisplaySlot.SIDEBAR) ?: return

        // Updates hole board
        if (forceUpdate) {
            objective.displayName(miniMessage.deserialize(data.title))
            objective.numberFormat(data.numberFormat)
        }

        // Current visibility of optional lines
        val previousVisibility = visibilityMap[player.uniqueId]
        val currentVisibility = data.lines.mapIndexed { index, it ->
            if (it is OptionalLineEntry) {
                if (forceUpdate || it.shouldCheckVisibility() || previousVisibility == null) {
                    it.isVisible(player)
                } else {
                    previousVisibility[index]
                }
            } else {
                it.isVisible(player)
            }
        }

        // Detect visibility change
        if (previousVisibility != null && previousVisibility != currentVisibility) {
            // Visibility changed, hard reload
            visibilityMap[player.uniqueId] = currentVisibility
            setScoreboard(player, data)
            return
        }
        visibilityMap[player.uniqueId] = currentVisibility

        // Filter lines to only include visible ones
        val visibleLines = mutableListOf<Pair<LineBaseEntry, Int>>()
        data.lines.forEachIndexed { index, line ->
            if (currentVisibility[index]) {
                visibleLines.add(line to index)
            }
        }

        // Every line
        for (i in visibleLines.indices) {
            val score = visibleLines.size - i - 1
            val entryKey = translate(score)
            val (line, _) = visibleLines[i]
            var team = scoreboard.getTeam(entryKey)

            // Creates team if not exists
            if (team == null) {
                // Creates team
                team = scoreboard.registerNewTeam(entryKey)

                // Connects team and entry
                team.addEntry(entryKey)

                // Sets team for line
                objective.getScore(entryKey).score = score

                // Sets content
                team.prefix(line.getText(player))
            }

            // Updates line
            if (forceUpdate || line.shouldUpdate(player)) {
                team.prefix(line.getText(player))
                // Also update score just in case it changed due to line filtering
                objective.getScore(entryKey).score = score
            }
        }

        // Cleanup old lines/scores
        val maxLines = 15
        for (i in visibleLines.size until maxLines) {
            val entryKey = translate(i)
            if (scoreboard.getEntries().contains(entryKey)) {
                scoreboard.resetScores(entryKey)
                scoreboard.getTeam(entryKey)?.unregister()
            }
        }
    }

    /**
     * Turns line number to invisible entry name
     *
     * @param line line number
     * @return paragraph + number or single letter
     * @since 1.0
     */
    private fun translate(line: Int): String {
        return "§" + line.toString(16).chunked(1).joinToString("§")
    }

    /**
     * Starts internal updater
     *
     * @since 1.0
     */
    private fun startUpdater() {
        object : BukkitRunnable() {
            override fun run() {
                val processedLines = mutableSetOf<LineBaseEntry>()
                for (player in Bukkit.getOnlinePlayers()) {
                    val data = map[player.uniqueId] ?: continue

                    data.lines.forEach {
                        if (processedLines.add(it)) it.updateTick()
                    }

                    applyData(player, player.scoreboard, data)
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

}