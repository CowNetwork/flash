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
package network.cow.minigame.flash.listener

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import net.kyori.adventure.text.Component
import network.cow.messages.adventure.highlight
import network.cow.messages.spigot.broadcastTranslatedInfo
import network.cow.messages.spigot.sendTranslatedError
import network.cow.messages.spigot.sendTranslatedInfo
import network.cow.minigame.flash.*
import network.cow.minigame.flash.event.PlayerCheckpointEvent
import network.cow.minigame.flash.event.PlayerFinishedEvent
import network.cow.minigame.noma.spigot.SpigotGame
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Furnace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin

class PlayerListener(val game: SpigotGame, val plugin: Plugin) : Listener {

    @EventHandler
    private fun onFinishTriggered(event: PlayerInteractEvent) {
        if (event.player.gameMode == GameMode.SPECTATOR) return
        if (event.action != Action.PHYSICAL) return
        if (event.clickedBlock?.type != Material.OAK_PRESSURE_PLATE) return
        val type = event.clickedBlock?.location?.subtract(0.0, 1.0, 0.0)?.block?.type

        // ghetto isWool check
        if (!type.toString().endsWith("WOOL")) return
        Bukkit.getPluginManager().callEvent(PlayerFinishedEvent(event.player, System.currentTimeMillis()))
    }

    @EventHandler
    private fun onCheckpointTriggered(event: PlayerInteractEvent) {
        if (!event.player.isIngame()) {
            event.isCancelled = true
            return
        }
        if (event.action != Action.PHYSICAL) return
        if (event.clickedBlock?.type != Material.STONE_PRESSURE_PLATE) return

        // Legacy checkpoint format
        val player = event.player
        val state = event.clickedBlock!!.location.subtract(0.0, 1.0, 0.0).block.state

        if (state !is Furnace) return

        val number = state.customName!!.toInt()

        if (number == player.getCurrentCheckPointIndex() + 1) {
            Bukkit.getPluginManager().callEvent(
                PlayerCheckpointEvent(player, Checkpoint(player.location.clone(), System.currentTimeMillis()))
            )
            return
        }

        if (number > player.getCurrentCheckPointIndex() + 1) {
            //player.sendMessage("§cDu hast ein Checkpoint übersprungen! Du wurdest zurück teleportiert!")
            player.sendTranslatedError(Translations.CHECKPOINT_SKIPPED)
            player.respawn()
            player.playSound(player.location, Sound.ENTITY_ENDERMAN_DEATH, 1.0F, 1.0F)
            return
        }
    }

    @EventHandler
    private fun onItemInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.item == null) return

        val type = event.item!!.type
        val player = event.player

        if (type == Material.RED_DYE) {
            player.respawn()
            return
        }

        if (type == Material.BLAZE_ROD || type == Material.STICK) {
            event.player.toggleVisibility()
            return
        }
    }

    @EventHandler
    private fun onPlayerDeath(event: PlayerDeathEvent) {
        event.keepInventory = true
        event.deathMessage(Component.empty())
    }

    @EventHandler
    private fun onPlayerRespawn(event: PlayerRespawnEvent) {
        event.player.getRespawnLocation()?.let {
            event.respawnLocation = it
        }
    }

    @EventHandler
    private fun onPlayerRespawn(event: PlayerPostRespawnEvent) {
        event.player.respawn()
    }

    @EventHandler
    private fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player

        if (!player.isIngame()) event.isCancelled = true
        if (event.cause != EntityDamageEvent.DamageCause.VOID) return

        player.respawn()
    }

    @EventHandler
    private fun onPlayerFinished(event: PlayerFinishedEvent) {
        val player = event.player
        player.gameMode = GameMode.SPECTATOR

        (this.game.getPhase("game") as FlashPhase).winners.add(this.game.getSpigotActor(player)!!)

        // Show all players again, since if previously hidden
        // the player cannot use the teleport functionality
        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { player.showPlayer(this.plugin, it) }

        val needed = event.finished - player.getFlashState<Long>(StateKey.START_TIME)!!
        val minutes = needed / (1000 * 60)
        val seconds = needed / 1000 % 60
        val millis = needed % 1000

        val formatted = String.format("%02d:%02d.%03d", minutes, seconds, millis)
        player.sendTranslatedInfo(Translations.TIME_NEEDED, formatted.highlight())
        //player.sendTweetLink(this.mapConfig!!.name, formatted)

        Bukkit.getServer().broadcastTranslatedInfo(Translations.PLAYER_FINISHED, event.player.displayName())

        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f) }
        this.game.nextPhase(false) // stop the flash phase
    }

    @EventHandler
    private fun onPlayerReachCheckpoint(event: PlayerCheckpointEvent) {
        val player = event.player
        player.setCurrentCheckpoint(event.checkpoint)
        player.applyEffects()

        val index = player.getCurrentCheckPointIndex()
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F)

        val maxCheckpoints = this.game.getTypedPhase<FlashPhase>("game")!!.maxCheckpoints
        player.sendTranslatedInfo(Translations.CHECKPOINT_REACHED, "[$index/$maxCheckpoints]".highlight())

        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { it.sendTranslatedInfo(Translations.CHECKPOINT_REACHED_BROADCAST, player.displayName(), "$index".highlight()) }

        spawnRandomFirework(this.plugin, player.location.clone())
    }
}