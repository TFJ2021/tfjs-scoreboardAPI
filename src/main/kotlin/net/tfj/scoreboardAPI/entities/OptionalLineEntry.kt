package net.tfj.scoreboardAPI.entities

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

/**
 * Optional line entity. Shows the line based on a condition.
 * Note that even when hidden, the internal logic (ticks) of the line and its content are still processed. Changing the visibility frequently is not recommended because it causes a full scoreboard re-registration to ensure lines are displayed correctly.
 *
 * @param condition lambda to check if the line should be displayed
 * @param updateInterval how often the condition should be checked (in ticks)
 * @param content the actual line content to display if condition is true
 * @since 1.2.0
 */
data class OptionalLineEntry(
    val condition: (Player) -> Boolean,
    val updateInterval: Int,
    val content: LineBaseEntry
) : LineBaseEntry() {

    // Returns text
    override fun getText(player: Player): Component {
        return if (condition(player)) content.getText(player) else Component.empty()
    }

    override fun shouldUpdate(player: Player): Boolean {
        // Condition check interval
        if (tick % updateInterval == 0L) return true

        // Content update if condition is true
        return condition(player) && content.shouldUpdate(player)
    }

    override fun isVisible(player: Player): Boolean {
        // Evaluate condition ONLY when tick matches interval
        // Otherwise return cached/current state
        return condition(player)
    }

    /**
     * @return true when the condition should be evaluated
     */
    fun shouldCheckVisibility(): Boolean {
        return tick % updateInterval == 0L
    }

    override fun updateTick() {
        super.updateTick()
        content.updateTick()
    }
}
