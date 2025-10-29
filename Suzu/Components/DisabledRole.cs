using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Suzu.Components;

public class DisabledRole
{
    [BsonId]
    public ObjectId ID { get; init; } = new();

    [BsonElement("user")]
    public ulong UserID { get; init; }

    [BsonElement("role")]
    public ulong RoleID { get; init; }

    public DisabledRole(ulong userID, ulong roleID)
    {
        UserID = userID;
        RoleID = roleID;
    }

    [BsonConstructor]
    [Obsolete("Bson constructor.")]
    public DisabledRole()
    {
    }
}
