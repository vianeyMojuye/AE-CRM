package com.aecrm.Models;

public class MenuModel {

    String PermissionId;
    String Permission;
    String Parent;
    String IndexLevel;
    String URL;
    String SpriteURL;

    public  MenuModel(String PermissionId, String Permission, String Parent,String IndexLevel, String URL,String SpriteURL)
    {
        this.PermissionId = PermissionId;
        this.Permission = Permission;
        this.Parent = Parent;
        this.IndexLevel = IndexLevel;
        this.URL = URL;
        this.SpriteURL = SpriteURL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setSpriteURL(String spriteURL) {
        SpriteURL = spriteURL;
    }

    public void setPermissionId(String permissionId) {
        PermissionId = permissionId;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public void setIndexLevel(String indexLevel) {
        IndexLevel = indexLevel;
    }

    public String getSpriteURL() {
        return SpriteURL;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getPermissionId() {
        return PermissionId;
    }

    public String getPermission() {
        return Permission;
    }

    public String getIndexLevel() {
        return IndexLevel;
    }

    public String getURL() {
        return URL;
    }

    public String getParent() {
        return Parent;
    }
}
