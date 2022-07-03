package senkohotel.suzu.xp;

public class XPRole {
    public int reqXP = 0;
    public String roleID = "";
    public String roleIcon = "";

    public XPRole(int xp, String id, String icon) {
        reqXP = xp;
        roleID = id;
        roleIcon = icon;
    }
}
