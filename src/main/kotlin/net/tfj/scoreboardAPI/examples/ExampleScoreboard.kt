package net.tfj.scoreboardAPI.examples

import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.tfj.scoreboardAPI.entities.*
import org.bukkit.GameMode
import kotlin.math.roundToInt

// Example scoreboard for demonstrating the possibilities of this API
// This can be ignored
object ExampleScoreboard : ScoreboardData(
    "example",
    "<aqua><bold>Scoreboard</bold></aqua> API",
    NumberFormat.styled(Style.style(NamedTextColor.DARK_GRAY)),
    listOf(
        DataLineEntry({ player -> "<gray>Welcome </gray><dark_aqua>${player.name}</dark_aqua>" }, 200),
        EmptyLine,
        AnimatedLineEntry(
            listOf(
                DataLineEntry({ "<gray>Gamemode: <dark_aqua>${it.gameMode.toString().lowercase()}</dark_aqua></gray>" }, 100),
                DataLineEntry({ "<gray>Health: <red>${it.health.roundToInt()}</red></gray>" }, 100),
            ),
            100,
            5
        ),
        EmptyLine,
        OptionalLineEntry(
            { it.gameMode == GameMode.CREATIVE },
            20,
            StaticLineEntry("<red>Creative Mode Active!</red>")
        ),
        StaticLineEntry("<dark_gray>|</dark_gray> <gray>This is an <gold>DEV</gold> build!</gray>"),
    )
)