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

    private static Task onMessage(DiscordClient sender, MessageCreateEventArgs args)
    {
        if (args.Author.IsBot) return Task.CompletedTask;

        var user = UserHelper.GetUser(args.Author.Id);

        if (user.LastMessage + 60 > DateTimeOffset.Now.ToUnixTimeSeconds())
            return Task.CompletedTask;

        user.Xp += new Random().Next(10, 20);
        user.LastMessage = DateTimeOffset.Now.ToUnixTimeSeconds();
        UserHelper.Update(user);

        LevelRole.Roles.Where(x => x.XpRequired <= user.Xp).ToList().ForEach(x =>
        {
            var channel = args.Channel;

            var role = channel.Guild.GetRole(x.RoleId);
            if (role == null) return;

            var member = args.Author as DiscordMember;
            if (member == null) return;

            if (member.Roles.Any(y => y.Id == role.Id))
                return;

            member.GrantRoleAsync(role, "Level Role");
        });

        return Task.CompletedTask;
    }
}
