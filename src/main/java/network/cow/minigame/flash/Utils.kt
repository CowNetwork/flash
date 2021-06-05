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

import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.*

fun create(material: Material, name: String): ItemStack {
    val item = ItemStack(material, 1)
    val meta = item.itemMeta
    meta.displayName(Component.text(name))
    item.setItemMeta(meta)
    return item
}

/*
fun createTwitterLink(click: String, tweet: String, vararg hashtags: String): Array<BaseComponent>  {
    val url = StringBuilder()
    url.append("http://twitter.com/intent/tweet?text=")
    url.append(tweet)
    if (hashtags.isNotEmpty()) {
        url.append("&hashtags=")
        for (i in hashtags.indices) {
            url.append(hashtags[i])
            if (i != hashtags.size - 1) {
                url.append(",")
            }
        }
    }
    val builder = ComponentBuilder(" $click")
        .event(ClickEvent(ClickEvent.Action.OPEN_URL, url.toString().replace(" ", "%20")))
        .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Öffnet ein Fenster zu Twitter.")));
    return builder.create();
}*/

fun spawnRandomFirework(plugin: Plugin, loc: Location) {
    val fw = loc.world.spawnEntity(loc, EntityType.FIREWORK) as Firework
    val fwm = fw.fireworkMeta
    val r = Random()
    val rt: Int = r.nextInt(4) + 1
    var type = FireworkEffect.Type.BALL
    if (rt == 1) type = FireworkEffect.Type.BALL
    if (rt == 2) type = FireworkEffect.Type.BALL_LARGE
    if (rt == 3) type = FireworkEffect.Type.BURST
    if (rt == 4) type = FireworkEffect.Type.CREEPER
    if (rt == 5) type = FireworkEffect.Type.STAR
    val r1i: Int = r.nextInt(17) + 1
    val r2i: Int = r.nextInt(17) + 1
    val c1 = getColor(r1i)
    val c2 = getColor(r2i)
    val effect: FireworkEffect = FireworkEffect.builder()
        .flicker(r.nextBoolean())
        .withColor(c1)
        .withFade(c2)
        .with(type)
        .trail(r.nextBoolean())
        .build()
    fwm.addEffect(effect)
    val rp: Int = r.nextInt(2) + 1
    fwm.power = rp
    fw.fireworkMeta = fwm
    Bukkit.getScheduler().runTaskLater(plugin, Runnable { fw.detonate() }, 10)
}

private fun getColor(i: Int): Color {
    var c = Color.AQUA
    if (i == 1) c = Color.AQUA
    if (i == 2) c = Color.BLACK
    if (i == 3) c = Color.BLUE
    if (i == 4) c = Color.FUCHSIA
    if (i == 5) c = Color.GRAY
    if (i == 6) c = Color.GREEN
    if (i == 7) c = Color.LIME
    if (i == 8) c = Color.MAROON
    if (i == 9) c = Color.NAVY
    if (i == 10) c = Color.OLIVE
    if (i == 11) c = Color.ORANGE
    if (i == 12) c = Color.PURPLE
    if (i == 13) c = Color.RED
    if (i == 14) c = Color.SILVER
    if (i == 15) c = Color.TEAL
    if (i == 16) c = Color.WHITE
    if (i == 17) c = Color.YELLOW
    return c
}