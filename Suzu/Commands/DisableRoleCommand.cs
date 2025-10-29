using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Suzu.Components;
using Suzu.Database.Helpers;

namespace Suzu.Commands;

public class DisableRoleCommand : SlashCommand
{
    public override string Name => "disable-role";
    public override string Description => "Disables a level role from appearing in your profile.";

    public override List<SlashOption> Options => new()
    {
        new SlashOption("role", "The level role to disable.", ApplicationCommandOptionType.Role, true)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var role = interaction.GetRole("role");
        if (role is null) return;

        var lvl = LevelRole.Roles.FirstOrDefault(x => x.RoleId == role.Id);

        if (lvl is null)
        {
            await interaction.Reply("This isn't a level role...", true);
            return;
        }

        var member = await interaction.Guild.GetMemberAsync(interaction.User.Id);
        if (member is null) throw new InvalidOperationException("Member is not on guild.");

        if (UserHelper.HasDisabled(interaction.User.Id, lvl.RoleId))
        {
            UserHelper.RemoveDisabled(interaction.User.Id, lvl.RoleId);

            var xp = UserHelper.GetUser(member.Id);

            if (xp.Xp >= lvl.XpRequired)
            {
                var grt = interaction.Guild.GetRole(lvl.RoleId);
                if (grt != null) await member.GrantRoleAsync(grt, "Re-enabled by user.");
            }

            await interaction.Reply("Re-enabled!", true);
            return;
        }

        UserHelper.AddDisabled(interaction.User.Id, lvl.RoleId);

        var eqp = member.Roles.FirstOrDefault(x => x.Id == lvl.RoleId);

        if (eqp != null)
            await member.RevokeRoleAsync(eqp, "Disabled by user.");

        await interaction.Reply("Done!", true);
    }
}
