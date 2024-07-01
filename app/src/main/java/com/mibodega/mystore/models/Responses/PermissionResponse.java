package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class PermissionResponse {
    private ArrayList<String> permissions = new ArrayList<>();

    public PermissionResponse(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
