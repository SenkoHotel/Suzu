using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Suzu.Database.Helpers;

namespace Suzu.Commands;

public class TopCommand : SlashCommand
{
    public override string Name => "top";
    public override string Description => "Get the top 10 users in the server.";
    public override Permissions Permissions => Permissions.None;

    public override void Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var users = UserHelper.Sorted.Take(10).ToList();

        var embed = new DiscordEmbedBuilder
        {
            Title = "Top Users",
            Thumbnail = new DiscordEmbedBuilder.EmbedThumbnail { Url = interaction.Guild.IconUrl },
            Description = string.Join("\n", users.Select((u, i) => $"#{i + 1}. <@{u.Id}> - {u.Xp}XP")),
            Color = new DiscordColor(221, 164, 137)
        };

        interaction.ReplyEmbed(embed);
    }
}
