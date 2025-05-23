package ru.rfma.auth.service;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rfma.auth.dto.JwtRequestReg;
import ru.rfma.auth.entity.Client;
import ru.rfma.auth.repository.ClientRepository;
import ru.rfma.core.util.FieldValidator;

@Service
public class ClientService {

    ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Client getByLogin(@NonNull String login) {
        return clientRepository.findByLogin(login);
    }

    public Client getById(final int id) {
        return clientRepository.getById(id);
    }

    public Client create(final JwtRequestReg regRequest, final char[] password){
        Client client = new Client(regRequest, password);
        clientRepository.save(client);
        return client;
    }

    public void setAdminRole(String id) throws Exception {
        Client client = clientRepository.getById(FieldValidator.validateId(id));
        client.setAdmin();
        clientRepository.save(client);
    }
}
