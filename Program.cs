﻿using Discord;
using Discord.WebSocket;
using Newtonsoft.Json;
using Suzu.Config;
using Suzu.Listeners;
using static System.IO.File;

namespace Suzu; 

internal static class Program {
    private static DiscordSocketClient _client;
        
    public static DiscordSocketClient Client => _client;
    public static BotConfig Config { get; set; }
        
    public static async Task Main(string[] args) => await Run();

    private static async Task Run() {
        Config = JsonConvert.DeserializeObject<BotConfig>(await ReadAllTextAsync("config.json"));
        
        _client = new DiscordSocketClient();
        _client.Log += Logger.Log;
        _client.Ready += Ready;
        _client.SlashCommandExecuted += SlashListener.SlashCommandExecuted;
        _client.MessageReceived += MessageListener.MessageReceived;
        await _client.LoginAsync(TokenType.Bot, Config.Token);
        await _client.StartAsync();
        await Task.Delay(-1);
    }
        
    private static async Task Ready() {
           Logger.Log($"Logged in as {_client.CurrentUser.Username}#{_client.CurrentUser.Discriminator}");
           
           var commands = new List<SlashCommandBuilder> {
               new() {
                   Name = "ping",
                   Description = "Pong!"
               },
               new() {
                   Name = "rank",
                   Description = "Get your rank in the server."
               }
           };
           
           foreach (var command in commands) {
               await Client.Rest.CreateGlobalCommand(command.Build());
           }
    }
}