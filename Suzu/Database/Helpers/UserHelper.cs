using HotelLib;
using MongoDB.Driver;
using Suzu.Components;

namespace Suzu.Database.Helpers;

public class UserHelper
{
    private static IMongoCollection<User> users => MongoDatabase.GetCollection<User>("users");
    private static IMongoCollection<DisabledRole> disabled => MongoDatabase.GetCollection<DisabledRole>("disabled-roles");

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

    public static void AddDisabled(ulong user, ulong role) => disabled.InsertOne(new DisabledRole(user, role));
    public static void RemoveDisabled(ulong user, ulong role) => disabled.DeleteMany(x => x.UserID == user && x.RoleID == role);

    public static bool HasDisabled(ulong user, ulong role)
    {
        var result = disabled.Find(x => x.UserID == user && x.RoleID == role).FirstOrDefault();
        return result != null;
    }
}
