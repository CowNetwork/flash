/*
    Flash mini game spigot plugin
    Copyright (C) 2021  Yannic Rieger

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package network.cow.mc.minigame.flash

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

enum class StateKey(val key: String) {
    SPEED("speed"),
    START_TIME("startTime"),
    CHECKPOINTS("checkpoints"),
    RESPAWN_LOCATION("respawnLocation")
}
