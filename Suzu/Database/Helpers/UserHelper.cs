using MongoDB.Driver;
using Suzu.Components;

namespace Suzu.Database.Helpers;

public class UserHelper
{
    private static IMongoCollection<User> users => MongoDatabase.GetCollection<User>("users");

    public static List<User> Sorted => users.Find(u => true).ToList().OrderByDescending(u => u.Xp).ToList();

    public static User GetUser(ulong id)
    {
        var u = users.Find(u => u.Id == id).FirstOrDefault();

        if (u != null) return u;

        u = new User { Id = id };
        users.InsertOne(u);
        return u;
    }

    public static void Update(User user) => users.ReplaceOne(u => u.Id == user.Id, user);
}
