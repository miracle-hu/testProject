package com.api.mhzx.entity;

import lombok.Getter;
import lombok.Setter;


public class LicenseDto  extends License{

    public LicenseDto() {
    }

    @Setter
    @Getter
    private String notice;
}
