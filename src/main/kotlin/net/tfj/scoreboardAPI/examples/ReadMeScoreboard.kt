package net.tfj.scoreboardAPI.examples

import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.tfj.scoreboardAPI.entities.*
import org.bukkit.Bukkit

// Scoreboard example of readme
object ReadMeScoreboard : ScoreboardData(
    "readme",
    "ReadMe <aqua><bold>Scoreboard</bold></aqua>",
    NumberFormat.styled(Style.style(NamedTextColor.DARK_GRAY)),
    listOf(
        StaticLineEntry("Welcome to My Server"),
        DataLineEntry({ "Player count: ${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}" }, 10),
        EmptyLine,
        AnimatedLineEntry(
            listOf(
                StaticLineEntry("0oo"),
                StaticLineEntry("o0o"),
                StaticLineEntry("oo0"),
                StaticLineEntry("o0o"),
            ),
            5,
            100
        ),
    )
)