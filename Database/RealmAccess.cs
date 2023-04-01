using System;
using System.IO;
using Realms;

namespace Suzu.Database {
    public static class RealmAccess {
        private static RealmConfiguration Config => new RealmConfiguration(Directory.GetCurrentDirectory() + Path.DirectorySeparatorChar +  Program.Config.RealmPath) {
            SchemaVersion = 1
        };
    
        private static Realm Realm => Realm.GetInstance(Config);

        public static void Run(Action<Realm> action) => action(Realm);
        public static void RunWrite(Action<Realm> action) => Write(Realm, action);
        
        private static void Write(Realm realm, Action<Realm> func) {
            Transaction transaction = null;

            try
            {
                if (!realm.IsInTransaction)
                    transaction = realm.BeginWrite();

                func(realm);
                transaction?.Commit();
            }
            finally
            {
                transaction?.Dispose();
            }
        }
    }
}