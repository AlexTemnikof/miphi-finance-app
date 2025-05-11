package ru.rfma.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.rfma.auth.dto.JwtRequestReg;
import ru.rfma.auth.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;
    private String login;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    private char[] password;
    private String email;
    private String name;

    /**
     * Инн клиента
     */
    private String inn;
    private String bankName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(final JwtRequestReg regRequest, final char[] password) {
        this.login = regRequest.login();
        this.clientType = regRequest.clientType();
        this.password = password;
        this.email = regRequest.email();
        this.name = regRequest.name();
        this.inn = regRequest.inn();
        this.bankName = regRequest.bankName();
        this.phoneNumber = regRequest.phoneNumber();
        this.role = Role.USER;
    }

    public void setAdmin(){
        this.role = Role.ADMIN;
    }
}