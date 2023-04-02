using System;
using System.Linq;
using System.Threading.Tasks;
using Discord.WebSocket;
using Suzu.Database;
using Suzu.Utils;

namespace Suzu.Listeners; 

public static class MessageListener {
    public static Task MessageReceived(SocketMessage message) {
        if (message.Author.IsBot) return Task.CompletedTask;
            
        RealmAccess.RunWrite(r => {
            var user = XpUtils.GetXpUser(r, message.Author.Id);
            
            if (user.LastMessage + 60 > DateTimeOffset.Now.ToUnixTimeSeconds()) return;
            
            user.Xp += new Random().Next(10, 20);
            user.LastMessage = DateTimeOffset.Now.ToUnixTimeSeconds();
            
            LevelRoles.Roles.Where(x => x.XpRequired <= user.Xp).ToList().ForEach(x => {
                var channel = message.Channel as SocketGuildChannel;
                if (channel == null) return;
                
                var role = channel.Guild.GetRole(x.RoleId);
                if (role == null) return;
                
                var userRole = message.Author as SocketGuildUser;
                if (userRole == null) return;
                
                if (userRole.Roles.Any(y => y.Id == role.Id)) return;
                
                userRole.AddRoleAsync(role);
            });
        });
        
        return Task.CompletedTask;
    }
}