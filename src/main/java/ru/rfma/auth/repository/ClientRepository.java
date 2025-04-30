package ru.rfma.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rfma.auth.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByLogin(String login);
}
