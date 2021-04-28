package network.cow.minigame.flash.listener

import net.kyori.adventure.text.Component
import network.cow.minigame.flash.*
import network.cow.minigame.flash.event.PlayerCheckpointEvent
import network.cow.minigame.flash.event.PlayerFinishedEvent
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
import org.bukkit.plugin.Plugin

class PlayerListener(val plugin: Plugin) : Listener {

    @EventHandler
    private fun onFinishTriggered(event: PlayerInteractEvent) {
        if (event.player.gameMode == GameMode.SPECTATOR) return
        if (event.action != Action.PHYSICAL) return
        if (event.clickedBlock?.type != Material.OAK_PRESSURE_PLATE) return
        val type = event.clickedBlock?.location?.subtract(0.0, 1.0, 0.0)?.block?.type
        if (type != Material.WHITE_WOOL) return
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
            //player.sendMessage("$PREFIX §cDu hast ein Checkpoint übersprungen! Du wurdest zurück teleportiert!")
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

        if (type == Material.INK_SAC) {
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
        //event.entity.player?.spigot().respawn()
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { event.entity.respawn() }, 1)
        event.deathMessage(Component.empty())
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

        // Show all players again, since if previously hidden
        // the player cannot use the teleport functionality
        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { player.showPlayer(this.plugin, it) }

        val needed = event.finished - startTime
        val minutes = needed / (1000 * 60)
        val seconds = needed / 1000 % 60
        val millis = needed % 1000

        val formatted = String.format("%02d:%02d.%03d", minutes, seconds, millis)
        //player.sendMessage("$PREFIX §7Du hast insgesamt §b$formatted §7benötigt.")
        //player.sendTweetLink(this.mapConfig!!.name, formatted)

        //Bukkit.broadcastMessage("$PREFIX §a${event.player.name} §bhat das Ziel erreicht.")
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f) }
    }

    @EventHandler
    private fun onPlayerReachCheckpoint(event: PlayerCheckpointEvent) {
        val player = event.player
        player.setCurrentCheckpoint(event.checkpoint)
        player.applyEffects()

        val index = player.getCurrentCheckPointIndex()
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F)
        //player.sendMessage("$PREFIX §7Du hast einen Checkpoint erreicht! §b[${index}/${mapConfig?.checkpoints}]")

        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { /*it.sendMessage("$PREFIX §7Der Spieler §a${player.name} §7hat den §b${index}. §7Checkpoint erreicht.")*/ }

        spawnRandomFirework(this.plugin, player.location.clone())
    }
}