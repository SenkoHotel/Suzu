using System.Text;
using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;

namespace Suzu.Commands;

public class ImageCommand : SlashCommand
{
    public override string Name => "image";
    public override string Description => "Shows the image permission embed";
    public override Permissions Permissions => Permissions.ManageMessages;

    public override void Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var content = new StringBuilder();
        content.AppendLine("Hi");
        content.AppendLine("You might ask 'Why cant i send images?'");
        content.AppendLine("or 'Why is that link not making an image appear?'");
        content.AppendLine("Just chat a bit more until you get the **Classic-Membership**<:classicTail:992862741561868388> role!");
        content.AppendLine("You can check your progress with `/rank` in <#843148981939994624>");

        var embed = new DiscordEmbedBuilder()
                    .WithDescription(content.ToString())
                    .WithColor(new DiscordColor(221, 164, 137));

        interaction.Channel.SendMessageAsync(embed);
        interaction.Reply("Message sent!", true);
    }
}
