package ru.rfma.auth.dto;

import ru.rfma.auth.entity.ClientType;

public record JwtRequestReg(String login, ClientType clientType,
                            String email, String password,
                            String name, String inn, String bankName,
                            String phoneNumber) {
}