# TFJ's Scoreboard API

[![](https://jitpack.io/v/TFJ5183/scoreboardAPI.svg)](https://jitpack.io/#TFJ5183/scoreboardAPI)

## Introduction

Welcome to the world of Paper plugin development with TFJS's `scoreboardAPI`! This advanced scoreboard management API
allows you to create, manage, and customize scoreboards in your Minecraft servers with ease. Whether you're building a
simple trivia game or an intricate team-based RPG, this library has got you covered.

## Key Features

- **Java & Kotlin support**: Works out of the box with Java and Kotlin
- **Dynamic Content**: Supports both static and dynamic line entries for flexible scoreboard updates.
- **Mini-message Support**: Utilizes Mini-message syntax to enhance the appearance of your scoreboards
- **Extensibility**: Easily extend and customize existing line types or create new ones.

## Getting Started

### Installation

1. **Add JitPack as repository**

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

2. **Add this api as compileOnly dependency**

```groovy
dependencies {
    implementation("com.github.TFJ5183:scoreboardAPI:{Tag}")
}
```

> For more details visit [JitPack Docs](https://jitpack.io/#TFJ5183/scoreboardAPI)

## Usage

### Creating a Scoreboard

To create a scoreboard, you need to define a custom `ScoreboardData` class that extends
`net.tfj.scoreboardAPI.ScoreboardData`. This class should specify the lines and their behavior.

```kotlin
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
            2,
            100
        ),
        OptionalLineEntry(
            { it.isOp },
            20,
            StaticLineEntry("<red>Admin Mode</red>")
        ),
    )
)
```

### Team API

The Team API allows you to manage player nametags (prefix, suffix, color) easily and automatically synchronizes them across all player scoreboards.

#### Creating Team Data

```kotlin
object AdminTeam : TeamData(
    priority = 1,
    id = "admin",
    displayName = "Admin Team",
    prefix = "<red>[Admin] </red>",
    color = NamedTextColor.RED
)
```

#### Using Team API

```kotlin
// Initialize
val teamAPI = TeamAPI(plugin)
val scoreboardAPI = ScoreboardAPI(plugin, defaultScoreboard, teamAPI)

// Set team for a player
teamAPI.setTeam(player, AdminTeam)
```

### Customizing Line Entries

The library provides several types of line entries, including:

- **StaticLineEntry**: Displays a static text that doesn't change.
- **DataLineEntry**: Updates its content based on data, such as player count or time.
- **AnimatedLineEntry**: Animates the line entry over time.
- **EmptyLine**: Adds an empty line for spacing.
- **OptionalLineEntry**: Shows or hides a line based on a condition. 

### Handling Player Joins

To ensure a fresh scoreboard for each player upon joining, reset or set a scoreboard on player on

```kotlin
fun join(event: PlayerJoinEvent) {
    scoreboardAPI.resetScoreboard(event.player)
    // or
    scoreboardAPI.setScoreboard(ExampleScoreboard)
}
```

## Version

The project uses the following dependencies with the specified versions, all of which have been tested and confirmed to work:

- Java: `21.0.9 (SAP-MASCHINE)`
- Kotlin: `2.3.20-RC3`
- Gradle: `8.8.0`
- Paper API: `1.21.11-R0.1-SNAPSHOT`

## Contributing

Contributions to this project are welcome! If you find any issues or have ideas for new features, please open an issue
on the [GitHub repository](https://github.com/TFJ5183/scoreboardAPI).

## License

This project is licensed under the [MIT](https://choosealicense.com/licenses/mit/) License. See the [LICENSE](LICENSE)
file for details.
