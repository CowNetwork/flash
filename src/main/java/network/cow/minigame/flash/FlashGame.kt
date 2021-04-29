package network.cow.minigame.flash

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.api.phase.EmptyPhaseResult
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.messages.spigot.broadcastInfo
import network.cow.minigame.noma.spigot.phase.VotePhase
import network.cow.minigame.noma.spigot.pool.WorldMeta

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class FlashGame(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase<EmptyPhaseResult>(game, config) {
    override fun onStart() {
        val world = (this.game.getPhase("vote") as VotePhase<WorldMeta>).firstVotedItem()
        val startTime = System.currentTimeMillis()
        this.game.getPlayers().forEach {
            it.setFlashState("speed", world.options["speed"] as Int)
            it.setFlashState("startTime", startTime)
        }
        Bukkit.getServer().broadcastInfo("START!!!")
    }
    override fun onPlayerJoin(player: Player) = Unit

    override fun onPlayerLeave(player: Player) {

    }

    override fun onStop(): EmptyPhaseResult {
        return EmptyPhaseResult()
    }


    override fun onTimeout() {
        //
        TODO("Not yet implemented")
    }
}