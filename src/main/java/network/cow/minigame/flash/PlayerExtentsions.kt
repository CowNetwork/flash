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
package network.cow.minigame.flash

import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.translateToComponent
import network.cow.messages.spigot.sendTranslatedInfo
import network.cow.spigot.extensions.ItemBuilder
import network.cow.spigot.extensions.state.getState
import network.cow.spigot.extensions.state.setState
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun Player.isIngame() = this.gameMode != GameMode.SPECTATOR

fun Player.getRespawnLocation(): Location? {
    return this.getFlashState(StateKey.RESPAWN_LOCATION)
}

fun Player.setRespawnLocation(location: Location) {
    return this.setFlashState(StateKey.RESPAWN_LOCATION, location)
}

fun Player.getCurrentCheckPointIndex() = this.getFlashState(StateKey.CHECKPOINTS, mutableListOf<Checkpoint>()).size

fun Player.setCurrentCheckpoint(checkpoint: Checkpoint) {
    val checkpoints = this.getFlashState(StateKey.CHECKPOINTS, mutableListOf<Checkpoint>())
    checkpoints.add(checkpoint)
    this.setFlashState(StateKey.CHECKPOINTS, checkpoints)
    this.setRespawnLocation(checkpoint.location)
}

fun Player.respawn() {
    this.applyEffects()
    this.getRespawnLocation()?.let { this.teleport(it) }
    this.playSound(this.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F)

    this.health = 20.0
    this.fireTicks = 0
    this.fallDistance = 0.0F
}

fun Player.sendTweetLink(map: String, time: String) {
   /*
    val link = createTwitterLink(
        "§b§l>§r §eTEILE DEINEN REKORD §b§l<",
        "Ich habe einen neuen FLASH-Rekord auf $map erreicht: $time Minuten",
        "FLASHRecords",
        "IntoTheLABS"
    )
    this.spigot().sendMessage(*link)*/
}

fun Player.applyEffects() {
    this.activePotionEffects.forEach { this.removePotionEffect(it.type) }
    this.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Int.MAX_VALUE, 3))
    this.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, this.getFlashState(StateKey.SPEED, 19)))
}

fun Player.toggleVisibility() {
    if (this.inventory.getItem(5)?.type == Material.BLAZE_ROD) {
        this.sendTranslatedInfo(Translations.PLAYERS_HIDDEN)
        this.playSound(this.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F)

        val item = ItemBuilder(Material.STICK)
            .name(Translations.SHOW_PLAYERS_ITEM.translateToComponent(this).corporate())
            .build()

        this.inventory.setItem(5, item)
        Bukkit.getOnlinePlayers()
            .filter { it != this }
            .forEach { player!!.showPlayer(JavaPlugin.getPlugin(FlashPlugin::class.java), it) }
    } else {
        val item = ItemBuilder(Material.BLAZE_ROD)
            .name(Translations.HIDE_PLAYERS_ITEM.translateToComponent(this).corporate())
            .build()

        this.inventory.setItem(5, item)
        this.sendTranslatedInfo(Translations.PLAYERS_SHOWN)
        player!!.playSound(player!!.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F)
        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { player!!.showPlayer(JavaPlugin.getPlugin(FlashPlugin::class.java), it) }
    }
}

fun Player.giveItems() {
    this.inventory.clear()
    val respawnItem = ItemBuilder(Material.RED_DYE)
        .name(Translations.RESET_ITEM.translateToComponent(this).corporate())
        .build()
    val hideItem = ItemBuilder(Material.BLAZE_ROD)
        .name(Translations.HIDE_PLAYERS_ITEM.translateToComponent(this).corporate())
        .build()
    this.inventory.setItem(3, respawnItem)
    this.inventory.setItem(5, hideItem)
}

fun Player.setFlashState(key: StateKey, value: Any) {
    this.setState(FlashPlugin::class.java, key.key, value)
}

fun <T> Player.getFlashState(key: StateKey): T? {
    return this.getState(FlashPlugin::class.java, key.key)
}

fun <T> Player.getFlashState(key: StateKey, default: T): T {
    return this.getState(FlashPlugin::class.java, key.key, default = default!!)
}