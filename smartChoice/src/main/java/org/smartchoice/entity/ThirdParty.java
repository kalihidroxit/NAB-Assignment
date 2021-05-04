package org.smartchoice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartchoice.utils.ThirdPartyType;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "THIRD_PARTY")
public class ThirdParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private ThirdPartyType name;

    @Column(name = "URL")
    private String url;

    @Column(name = "X_API_KEY")
    private String xApiKey;
}
