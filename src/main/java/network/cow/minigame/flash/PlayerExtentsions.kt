package network.cow.minigame.flash

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val HIDE_PLAYER_ITEM = create(Material.BLAZE_ROD, 0, "§c§lSpieler verstecken §r§7§o<Rechtsklick>")
val SHOW_PLAYER_ITEM = create(Material.STICK, 0, "§c§lSpieler anzeigen §r§7§o<Rechtsklick>")
val RESPAWN_ITEM = create(Material.INK_SAC, 1, "§c§lInstant-Tod(TM) §r§7§o<Rechtsklick>")


fun Player.isIngame() = this.gameMode != GameMode.SPECTATOR

fun Player.getCurrentCheckPointLocation() = Location(Bukkit.getWorld("PLACEHOLDER"), 0.0, 0.0, 0.0)

fun Player.getCurrentCheckPointIndex() = 0 //gameData[this]?.checkpoints?.size ?: 0

fun Player.setCurrentCheckpoint(checkpoint: Checkpoint) {

}

fun Player.respawn() {
    this.applyEffects()

    /*
    if (gameData[player]!!.checkpoints.isEmpty()) {
        this.teleport(gameData[player]?.spawn)
    } else {
        this.teleport(this.getCurrentCheckPointLocation())
    }*/

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
    this.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3))
    //this.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, gameData[player]?.mapSpeed ?: 19 ))
}

fun Player.toggleVisibility() {
    if (this.inventory.getItem(5)?.type == Material.BLAZE_ROD) {
        //this.sendMessage("$PREFIX §7Du hast alle Spieler §cversteckt§7.")
        this.playSound(this.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F)
        this.inventory.setItem(5, SHOW_PLAYER_ITEM)
        Bukkit.getOnlinePlayers()
            .filter { it != this }
            .forEach { this.hidePlayer(it) }
    } else {
        this.inventory.setItem(5, HIDE_PLAYER_ITEM)
        //player.sendMessage("$PREFIX §7Du §asiehst §7nun alle Spieler§7.")
        player!!.playSound(player!!.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F)
        Bukkit.getOnlinePlayers()
            .filter { it != player }
            .forEach { player!!.showPlayer(JavaPlugin.getPlugin(FlashPlugin::class.java), it) }
    }
}

fun Player.giveItems() {
    this.inventory.clear()
    this.inventory.setItem(3, RESPAWN_ITEM)
    this.inventory.setItem(5, HIDE_PLAYER_ITEM)
}