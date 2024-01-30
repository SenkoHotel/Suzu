using Suzu.Components;
using Suzu.Database.Helpers;

namespace Suzu.Database.Extensions;

public static class UserExtension
{
    public static long GetRank(this User user) => UserHelper.Sorted.FindIndex(u => u.Id == user.Id) + 1;
}
