package ru.rfma.auth.entity;

public enum ClientType {
    PERSON("Физическое"),
    ORGANISATION("Юридическое");
    final String desc;

    ClientType(final String desc) {
        this.desc = desc;
    }
}
