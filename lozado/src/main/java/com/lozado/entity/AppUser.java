package com.lozado.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "APP_USER")
public class AppUser {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "X_API_KEY")
    private String apiKey;

    @Column(name = "CREATED_AT")
    private LocalDateTime created_at = LocalDateTime.now();
}
