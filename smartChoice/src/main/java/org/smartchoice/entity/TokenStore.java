package org.smartchoice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TOKEN_STORE")
public class TokenStore {
    @Id
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "USER")
    private String user;

    @Column(name = "IS_EXP")
    private Boolean isExp = Boolean.FALSE;
}
