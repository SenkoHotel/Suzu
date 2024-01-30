using DSharpPlus.Entities;

namespace Suzu.Components;

public class LevelRole
{
    public static LevelRole[] Roles => new[]
    {
        new LevelRole
        {
            Name = "Classic-Membership",
            RoleId = 792185558863970325,
            XpRequired = 1000,
            Icon = "<:classicTail:992862741561868388>",
            Color = new DiscordColor(0, 136, 255)
        },
        new LevelRole
        {
            Name = "Bronze-Membership",
            RoleId = 792220150874243072,
            XpRequired = 2500,
            Icon = "<:bronzeTail:992862740249055252>",
            Color = new DiscordColor(187, 129, 42)
        },
        new LevelRole
        {
            Name = "Silver-Membership",
            RoleId = 792220517285101598,
            XpRequired = 5000,
            Icon = "<:silverTail:992862742790811758>",
            Color = new DiscordColor(185, 187, 190)
        },
        new LevelRole
        {
            Name = "Gold-Membership",
            RoleId = 792221386751868929,
            XpRequired = 10000,
            Icon = "<:goldTail:992862738634244146>",
            Color = new DiscordColor(255, 215, 0)
        },
        new LevelRole
        {
            Name = "Platinum-Membership",
            RoleId = 815158447031975967,
            XpRequired = 25000,
            Icon = "<:platinumTail:992862743793246261>",
            Color = new DiscordColor(128, 0, 255)
        },
        new LevelRole
        {
            Name = "Ruby-Membership",
            RoleId = 815162865359126528,
            XpRequired = 50000,
            Icon = "<:rubyTail:992862737476632616>",
            Color = new DiscordColor(255, 0, 0)
        },
        new LevelRole
        {
            Name = "Senko Overlord",
            RoleId = 815158741703720961,
            XpRequired = 100000,
            Icon = "<:SK_stronk:792073244156231710>",
            Color = new DiscordColor(255, 165, 0)
        }
    };

    public string Name { get; init; } = "";
    public ulong RoleId { get; init; }
    public int XpRequired { get; init; }
    public string Icon { get; init; } = "";
    public DiscordColor Color { get; init; }

    public static LevelRole? GetNext(long xp)
    {
        var roles = Roles.OrderBy(x => x.XpRequired).ToList();
        var role = roles.FirstOrDefault(x => x.XpRequired > xp);
        return role;
    }
}
