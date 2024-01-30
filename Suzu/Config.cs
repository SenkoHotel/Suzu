using JetBrains.Annotations;

namespace Suzu;

[UsedImplicitly]
public class Config
{
    public string Token { get; set; } = "";
    public string MongoConnectionString { get; set; } = "";
    public string MongoDatabaseName { get; set; } = "";
}
