using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using HotelLib;
using HotelLib.Commands;
using Suzu.Commands;
using Suzu.Components;
using Suzu.Database;
using Suzu.Database.Helpers;

namespace Suzu;

public static class Program
{
    public static async Task Main()
    {
        var config = HotelBot.LoadConfig<Config>();
        MongoDatabase.Initialize(config.MongoConnectionString, config.MongoDatabaseName);

        var bot = new HotelBot(config.Token)
        {
            Commands = new List<SlashCommand>
            {
                new RankCommand(),
                new TopCommand(),
                new ImageCommand()
            }
        };

        bot.Client.MessageCreated += onMessage;
        await bot.Start();
    }

    private static async Task onMessage(DiscordClient sender, MessageCreateEventArgs args)
    {
        if (args.Author.IsBot)
            return;

        var user = UserHelper.GetUser(args.Author.Id);

        if (user.LastMessage + 60 > DateTimeOffset.Now.ToUnixTimeSeconds())
            return;

        user.Xp += new Random().Next(10, 20);
        user.LastMessage = DateTimeOffset.Now.ToUnixTimeSeconds();
        UserHelper.Update(user);

        LevelRole? highestRewarded = null;

        LevelRole.Roles.Where(x => x.XpRequired <= user.Xp).ToList().ForEach(x =>
        {
            var channel = args.Channel;

            var role = channel.Guild.GetRole(x.RoleId);

            if (role == null)
                return;

            var member = args.Author as DiscordMember;

            if (member == null)
                return;

            if (member.Roles.Any(y => y.Id == role.Id))
                return;

            highestRewarded = x;
            member.GrantRoleAsync(role, "Level Role");
        });

        if (highestRewarded != null)
        {
            var embed = new DiscordEmbedBuilder()
                        .WithAuthor(args.Author.Username, iconUrl: args.Author.AvatarUrl)
                        .WithTitle("New milestone reached!")
                        .WithDescription($"You leveled up to {highestRewarded.Icon} **{highestRewarded.Name}**!");

            if (highestRewarded != LevelRole.Roles.Last())
            {
                var next = LevelRole.Roles[LevelRole.Roles.IndexOf(highestRewarded) + 1];
                embed.AddField("Next up", $"{next.Icon} **{next.Name}**");
            }
            else
                embed.AddField("Next up", "Nothing, you're done... <:SR_sip:858784374199287828>");

            await args.Channel.SendMessageAsync(embed.Build());
        }
    }
}
