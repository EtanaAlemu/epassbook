package com.dxvalley.epassbook.models;


import javax.persistence.*;
import com.dxvalley.epassbook.enums.ERole;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,unique=true)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
