using MongoDB.Bson.Serialization.Attributes;

namespace Suzu.Components;

public class User
{
    [BsonElement("ID")]
    public ulong Id { get; init; }

    [BsonElement("XP")]
    public int Xp { get; set; }

    [BsonElement("LastMessage")]
    public long LastMessage { get; set; }
}
