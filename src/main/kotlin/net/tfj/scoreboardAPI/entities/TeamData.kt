package net.tfj.scoreboardAPI.entities

import net.kyori.adventure.text.format.NamedTextColor

/**
 * Data entity for a team
 *
 * @param priority hierarchy (used for sorting)
 * @param id Unique identifier for the team
 * @param displayName display name
 * @param prefix Prefix for team members
 * @param suffix Suffix for team members
 * @param color Team color
 * @since 1.3.0
 */
open class TeamData(
    val priority: Int = 0,
    val id: String,
    val displayName: String = "",
    val prefix: String = "",
    val suffix: String = "",
    val color: NamedTextColor = NamedTextColor.WHITE
) {
    /**
     * Unique team name for internal use
     */
    val teamName: String get() = priority.toString().padStart(4, '0') + id
}
