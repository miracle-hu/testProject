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
@Table(name="secret_key")
public class SecretKey {
    public SecretKey(){}

    @Id
    @NotNull
    @Setter
    @Getter
    private String bkmKey;

    @NotNull
    @Setter
    @Getter
    private int status;


    @Setter
    @Getter
    private String note;
}
