package ru.rfma.auth.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.rfma.auth.entity.Client;
import ru.rfma.auth.enums.Role;
import ru.rfma.auth.repository.ClientRepository;
import ru.rfma.core.util.FieldValidator;

@Service
public class ClientService {

    ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public Client getByLogin(@NonNull String login) {
        return clientRepository.findByLogin(login);
    }

    public Client create(@NonNull String login, char[] password, String email, String name){

        Client client = new Client(login, password, Role.USER, email, name);
        clientRepository.save(client);
        return client;
    }

    public void setAdminRole(String id) throws Exception {
        Client client = clientRepository.getById(FieldValidator.validateId(id));
        client.setAdmin();
        clientRepository.save(client);
    }
}
