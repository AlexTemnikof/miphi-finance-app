package ru.rfma.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.rfma.auth.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Client {
    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;

    private String login;
    private char[] password;
    private String email;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(String login, char[] password, Role role, String email, String name) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
        this.name = name;
    }

    public void setAdmin(){
        this.role = Role.ADMIN;
    }
}