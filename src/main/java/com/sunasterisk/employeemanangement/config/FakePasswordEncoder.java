package com.sunasterisk.employeemanangement.config;

/**
 * PasswordEncoder gia lap chi danh cho muc dich demo/testing.
 * KHONG su dung trong moi truong production.
 * Logic: encode = "FAKE{" + rawPassword + "}"
 */
public class FakePasswordEncoder {

    private static final String PREFIX = "FAKE{";
    private static final String SUFFIX = "}";

    public String encode(CharSequence rawPassword) {
        return PREFIX + rawPassword + SUFFIX;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}

