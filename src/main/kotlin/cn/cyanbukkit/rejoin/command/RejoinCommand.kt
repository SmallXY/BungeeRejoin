package cn.cyanbukkit.rejoin.command

import cn.cyanbukkit.rejoin.Rejoin.Companion.instance
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class RejoinCommand : Command("rejoin") {
    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        //
        if (sender == null) return
        if (sender is ProxiedPlayer) {
            val server = instance.player.getString(sender.uniqueId.toString())
            if (server == null) {
                sender.sendMessage(net.md_5.bungee.api.chat.TextComponent(
                    instance.config.getString("message.rejoinError")))
                return
            }
            sender.sendMessage(net.md_5.bungee.api.chat.TextComponent(
                instance.config.getString("message.rejoined")))
            sender.connect(instance.proxy.getServerInfo(server))
            return
        } else {
            sender.sendMessage("§c§l[Rejoin]§r§c 该指令只能由玩家执行")
        }
    }
}