using Realms;

namespace Suzu.Database.Components {
    public class XpUser : RealmObject {
        [PrimaryKey]
        public string Id { get; set; }
        public int Xp { get; set; }
        public long LastMessage { get; set; }
        
        public XpUser() { }
    }
}