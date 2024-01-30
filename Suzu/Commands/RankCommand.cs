using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Suzu.Components;
using Suzu.Database.Extensions;
using Suzu.Database.Helpers;

namespace Suzu.Commands;

public class RankCommand : SlashCommand
{
    public override string Name => "rank";
    public override string Description => "Get your rank in the server.";

    public override void Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var user = UserHelper.GetUser(interaction.User.Id);

        var next = LevelRole.GetNext(user.Xp);
        var xpLeft = next?.XpRequired - user.Xp ?? 0;
        var percent = (float)user.Xp / next?.XpRequired ?? 1;

        var nextText = next != null
            ? $"{next.Icon} {next.Name}\n{percent:P2} ({xpLeft}XP left)"
            : "All roles claimed!";

        var embed = new DiscordEmbedBuilder
        {
            Author = new DiscordEmbedBuilder.EmbedAuthor
            {
                Name = interaction.User.Username,
                IconUrl = interaction.User.AvatarUrl
            },
            Color = next?.Color ?? LevelRole.Roles[^1].Color
        };

        embed.AddField("<:SK_mangaLaugh:792020838990872586> XP", $"{user.Xp}", true)
             .AddField(":arrow_up: Rank", $"#{user.GetRank()}", true)
             .AddField(":fast_forward: Next Role", nextText.Replace(",", "."));

        interaction.ReplyEmbed(embed);
    }
}
