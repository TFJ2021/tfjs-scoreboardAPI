package net.tfj.scoreboardAPI.examples

import net.kyori.adventure.text.format.NamedTextColor
import net.tfj.scoreboardAPI.entities.TeamData

object ExampleTeams {
    object AdminTeam : TeamData(
        priority = 1,
        id = "admin",
        displayName = "Admin Team",
        prefix = "<red>[Admin] </red>",
        color = NamedTextColor.RED
    )

    object PlayerTeam : TeamData(
        priority = 2,
        id = "player",
        displayName = "Player Team",
        prefix = "<gray>[Player] </gray>",
        color = NamedTextColor.GRAY
    )

    object PremiumTeam : TeamData(
        priority = 1,
        id = "premium",
        displayName = "Premium Team",
        prefix = "<gold>[Premium] </gold>",
        color = NamedTextColor.GOLD
    )

}

