package network.cow.minigame.flash

import org.bukkit.Location
import java.util.*

data class Checkpoint(val location: Location, val timeReached: Long)

data class FlashPlayer(
    val uuid: UUID,
    val globalStats: GlobalStats,
    val mapScore: MapScore
)

data class GlobalStats(
    val wins: Int,
    val deaths: Int,
    val checkpoints: Int,
    val gamesPlayed: Int,
    val points: Int,
)

data class MapScore(
    val map: String,
    val timeNeeded: Int,
    val accomplishedAt: Long
)
