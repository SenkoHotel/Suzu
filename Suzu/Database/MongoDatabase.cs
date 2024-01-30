using MongoDB.Driver;

namespace Suzu.Database;

public static class MongoDatabase
{
    private static IMongoDatabase? database;

    public static void Initialize(string connectionString, string databaseName)
    {
        var client = new MongoClient(connectionString);
        database = client.GetDatabase(databaseName);
    }

    public static IMongoCollection<T> GetCollection<T>(string name) => database!.GetCollection<T>(name);
}
