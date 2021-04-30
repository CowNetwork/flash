package network.cow.minigame.flash

import net.kyori.adventure.text.Component
import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.api.phase.EmptyPhaseResult
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.messages.spigot.broadcastInfo
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.VotePhase
import network.cow.minigame.noma.spigot.pool.WorldMeta

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.entity.Player
import kotlin.io.path.name

class FlashGame(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase<EmptyPhaseResult>(game, config) {
    override fun onStart() {
        val startTime = System.currentTimeMillis()
        val worldMeta = (this.game.getPhase("vote") as VotePhase<WorldMeta>).firstVotedItem()
        val world = (this.game as SpigotGame).world
        // TODO: set in map config
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
        this.game.getPlayers().forEach {
            it.setFlashState(StateKey.SPEED, worldMeta.options["speedLevel"] as Int)
            it.setFlashState(StateKey.CHECKPOINTS, startTime)
            it.setRespawnLocation(worldMeta.globalSpawnLocations.first().toLocation(world) )
            it.applyEffects()
            it.giveItems()
        }
    }
    override fun onPlayerJoin(player: Player) = Unit

    override fun onPlayerLeave(player: Player) {

    }

    override fun onStop() = EmptyPhaseResult()


    override fun onTimeout() {
        TODO("Not yet implemented")
    }
}