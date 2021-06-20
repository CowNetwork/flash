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

import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.EndPhase
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.noma.spigot.phase.VotePhase
import network.cow.minigame.noma.spigot.pool.WorldMeta
import org.bukkit.GameRule
import org.bukkit.entity.Player

class FlashPhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : SpigotPhase(game, config) {

    val winners = mutableListOf<SpigotActor>()
    var maxCheckpoints: Int = 0

    override fun onStart() {
        val startTime = System.currentTimeMillis()
        val result: VotePhase.Result<WorldMeta> = this.game.store.get("map") ?: error("no map meta")
        val worldMeta: WorldMeta = result.items.first().value

        maxCheckpoints = worldMeta.options["checkpoints"] as Int

        // TODO: set in map config
        this.game.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
        this.game.getIngamePlayers().forEach {
            it.setFlashState(StateKey.SPEED, worldMeta.options["speedLevel"] as Int)
            it.setFlashState(StateKey.START_TIME, startTime)
            it.setRespawnLocation(worldMeta.globalSpawnLocations.first().toLocation(this.game.world) )
            it.applyEffects()
            it.giveItems()
        }
    }

    override fun onStop() {
        this.storeMiddleware.store.set(EndPhase.STORE_KEY, EndPhase.Result(this.winners.map { setOf(it) }))
    }
    override fun onPlayerJoin(player: Player) = Unit
    override fun onPlayerLeave(player: Player) = Unit
    override fun onTimeout() = Unit
}