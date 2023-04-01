using System;
using System.Threading.Tasks;
using Discord;

namespace Suzu {
    public class Logger {
        public static void Log(string msg) {
            Log(new LogMessage(LogSeverity.Info, "Suzu", msg));
        }
        
        public static Task Log(LogMessage msg) {
            Console.WriteLine(msg.ToString());
            return Task.CompletedTask;
        }
        
        public static void Error(string msg) {
            Log(new LogMessage(LogSeverity.Error, "Suzu", msg));
        }
        
        public static void Error(Exception e) {
            Log(new LogMessage(LogSeverity.Error, "Suzu", e.Message, e));
        }
        
        public static void Error(string msg, Exception e) {
            Log(new LogMessage(LogSeverity.Error, "Suzu", msg, e));
        }
        
        public static void Warn(string msg) {
            Log(new LogMessage(LogSeverity.Warning, "Suzu", msg));
        }
        
        public static void Debug(string msg) {
            Log(new LogMessage(LogSeverity.Debug, "Suzu", msg));
        }
    }
}