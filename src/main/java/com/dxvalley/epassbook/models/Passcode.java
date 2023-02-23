package com.dxvalley.epassbook.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Passcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String passcode;
    private Long userId;
}
