package network.cow.minigame.flash

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.api.phase.EmptyPhaseResult
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.messages.spigot.broadcastInfo

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class FlashGame(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase<EmptyPhaseResult>(game, config) {

    override fun onStart() {
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