package org.smartchoice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "HISTORY_SEARCH")
public class HistorySearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "SEARCH_AT")
    private LocalDateTime searchAt = LocalDateTime.now();

}
