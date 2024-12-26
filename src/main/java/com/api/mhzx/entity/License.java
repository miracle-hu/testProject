package com.api.mhzx.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="license")
public class License {

    public License() {
    }

    @Id
    @NotNull
    @Setter
    @Getter
    private String username;

    @NotNull
    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String note;

    @NotNull
    @Setter
    @Getter
    private String effectDate;

    @NotNull
    @Setter
    @Getter
    private String server;

    @NotNull
    @Setter
    @Getter
    private int accountNum;

    @NotNull
    @Setter
    @Getter
    private int supportInstruct;

    @NotNull
    @Setter
    @Getter
    private int supportInstructExtend;

    @NotNull
    @Setter
    @Getter
    private int closeSingleLogin;

    @Setter
    @Getter
    private int showNotice;

    @Setter
    @Getter
    private String loginTimestamp;

    @Setter
    @Getter
    private String extend1;

    @Setter
    @Getter
    private String extend2;

    @Setter
    @Getter
    private int extend3;
}
