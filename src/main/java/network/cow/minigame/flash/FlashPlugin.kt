package network.cow.minigame.flash

import network.cow.messages.spigot.MessagesPlugin
import network.cow.minigame.noma.spigot.NomaGamePlugin
import network.cow.messages.adventure.gradient
import network.cow.messages.core.Gradients
import network.cow.minigame.flash.listener.CancelListener
import network.cow.minigame.flash.listener.PlayerListener
import org.bukkit.Bukkit

class FlashPlugin : NomaGamePlugin() {
    override fun onEnable() {
        super.onEnable()
        MessagesPlugin.PREFIX = "FLASH".gradient(Gradients.MINIGAME)
        Bukkit.getPluginManager().registerEvents(PlayerListener(this), this)
        Bukkit.getPluginManager().registerEvents(CancelListener(), this)
    }
}