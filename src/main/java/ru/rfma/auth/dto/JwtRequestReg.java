package ru.rfma.auth.dto;

public record JwtRequestReg(String login, String email, String password, String name) {
}