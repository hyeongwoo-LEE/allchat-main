package com.allchat.allchat.domain.user;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isFromSocial;

}
