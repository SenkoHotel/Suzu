using System.Collections.Generic;
using System.Threading.Tasks;
using Discord;
using Discord.WebSocket;
using Suzu.Database;
using Suzu.Database.Components;
using Suzu.Utils;

namespace Suzu.Listeners; 

public static class SlashListener {
    public static async Task SlashCommandExecuted(SocketSlashCommand command) {
           switch (command.Data.Name) {
               case "ping":
                   await command.RespondAsync("", new[] {
                       new EmbedBuilder {
                           Title = "Pong!",
                           Description = $"{Program.Client.Latency}ms",
                           Color = Color.Green,
                           ImageUrl = "https://media.tenor.com/l_lkXGz-DroAAAAC/rumia-rumia-fumo.gif"
                       }.Build()
                   });
                   break;
               
               case "rank":
                   RealmAccess.RunWrite(r => {
                       var user = XpUtils.GetXpUser(r, command.User.Id);
                       var rank = XpUtils.GetRank(r, command.User.Id);
                       var next = XpUtils.GetNextLevelRole(user.Xp);
                       int xpLeft = next?.XpRequired - user.Xp ?? 0;
                       float xpPercent = (float)user.Xp / next?.XpRequired ?? 1;
                       string nextText = next != null
                           ? $"{next.Icon} {next.Name}\n{xpPercent:P2} ({xpLeft}XP left)"
                           : "All roles claimed!";
                       command.RespondAsync("", new[] {
                           new EmbedBuilder {
                               Author = new EmbedAuthorBuilder {
                                   Name = command.User.Username,
                                   IconUrl = command.User.GetAvatarUrl()
                               },
                               Fields = new List<EmbedFieldBuilder> {
                                   new() {
                                       Name = "<:SK_mangaLaugh:792020838990872586> XP",
                                       Value = $"{user.Xp}",
                                       IsInline = true
                                   },
                                   new() {
                                       Name = ":arrow_up: Rank",
                                       Value = $"#{rank}",
                                       IsInline = true
                                   },
                                   new() {
                                       Name = ":fast_forward: Next Role",
                                       Value = nextText.Replace(",", "."),
                                       IsInline = false
                                   }
                               },
                               Color = next?.Color ?? LevelRoles.Roles[LevelRoles.Roles.Length - 1].Color,
                           }.Build()
                       });
                   });
                   break;
               
                case "top":
                    RealmAccess.Run(r => {
                        var users = r.All<XpUser>().OrderByDescending(u => u.Xp).ToList();
                        var page = command.Data.Options.FirstOrDefault(o => o.Name == "page")?.Value as int? ?? 1;
                        int maxPage = (int)Math.Ceiling(users.Count / 10f);
                        
                        if (page < 1 || page > maxPage) {
                            command.RespondAsync("", new[] {
                                new EmbedBuilder {
                                    Title = "Invalid page",
                                    Description = $"The page must be between 1 and {maxPage}.",
                                    Color = Color.Red,
                                    ImageUrl =
                                        "https://media.discordapp.net/attachments/328453138665439232/1066616268406407168/gyFdA6F.gif"
                                }.Build()
                            });
                            return;
                        }

                        var embed = new EmbedBuilder {
                            Title = $"Top Users - Page {page}/{maxPage}",
                            Color = new Color(221, 164, 137)
                        };
                        
                        for (int i = (page - 1) * 10; i < page * 10 && i < users.Count; i++) {
                            var user = users[i];
                            var rank = XpUtils.GetRank(r, user.Id);
                            embed.Description += $"#{rank} <@{user.Id}> - {user.Xp}XP\n";
                        }
                        
                        command.RespondAsync("", new[] {embed.Build()});
                    });
                    break;
               
               default:
                   await command.RespondAsync("", new[] {
                       new EmbedBuilder {
                           Title = "Unknown command",
                           Description = "This command is not implemented yet.",
                           Color = Color.Red,
                           ImageUrl =
                               "https://media.discordapp.net/attachments/328453138665439232/1066616268406407168/gyFdA6F.gif"
                       }.Build()
                   });
                   break;
           }
    }
}