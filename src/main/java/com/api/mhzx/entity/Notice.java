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
@Table(name="notice")
public class Notice {
    public Notice() {
    }

    @Id
    @Setter
    @Getter
    private int id;

    @NotNull
    @Setter
    @Getter
    private String notice;
}
