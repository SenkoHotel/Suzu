using System.Linq;
using Realms;
using Suzu.Database.Components;

namespace Suzu.Utils {
    public static class XpUtils {
        public static XpUser GetXpUser(Realm realm, ulong userId) {
            var user = realm.All<XpUser>().FirstOrDefault(x => x.Id == userId.ToString());
            
            if (user == null) {
                user = new XpUser {
                    Id = userId.ToString()
                };
                realm.Add(user);
            }
            
            return user;
        }
        
        public static int GetRank(Realm realm, ulong userId) {
            var users = realm.All<XpUser>().OrderByDescending(x => x.Xp).ToList();
            var user = users.FirstOrDefault(x => x.Id == userId.ToString());
            
            if (user == null) return -1;
            
            return users.IndexOf(user) + 1;
        }
        
        public static LevelRoles GetNextLevelRole(int xp) {
            var roles = LevelRoles.Roles.OrderBy(x => x.XpRequired).ToList();
            var role = roles.FirstOrDefault(x => x.XpRequired > xp);

            return role;
        }
    }
}