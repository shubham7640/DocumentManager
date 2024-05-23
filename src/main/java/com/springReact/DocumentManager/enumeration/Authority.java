package com.springReact.DocumentManager.enumeration;

import static com.springReact.DocumentManager.constants.Constants.*;

public enum Authority {

    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES);


    private final String value;

    Authority(String value) {this.value = value;}

    public String getValue() {
        return this.value;
    }
}
