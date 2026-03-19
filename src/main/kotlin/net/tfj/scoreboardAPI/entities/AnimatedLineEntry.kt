package net.tfj.scoreboardAPI.entities

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

/**
 * Animated line entity. The frame switches every [interval] ticks
 *
 * @param frames list of [LineBaseEntry]. Each line entry is a frame entry.
 * @param interval amount of ticks until the frame switches
 * @param updateInterval amount of ticks until the line should be updated. The counter will not be reset on frame change.
 * @since 1.0
 */
data class AnimatedLineEntry(
    val frames: List<LineBaseEntry>,
    val interval: Int,
    val updateInterval: Int = 1
) : LineBaseEntry() {

    // Returns the animated text
    override fun getText(player: Player): Component {
        if (frames.isEmpty()) return Component.empty()
        val index = (tick / interval) % frames.size
        return frames[index.toInt()].getText(player)
    }

    // Updates when interval reached
    override fun shouldUpdate(player: Player): Boolean {
        if (frames.isEmpty()) return false
        val index = (tick / interval) % frames.size
        return tick % interval == 0L || tick % updateInterval == 0L || frames[index.toInt()].shouldUpdate(player)
    }

    override fun updateTick() {
        super.updateTick()
        frames.forEach { it.updateTick() }
    }
}
