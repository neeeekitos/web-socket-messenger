package controller;

import dao.UserRepository;
import domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final UserRepository clientRepository;

    public ClientsController(UserRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<User> getClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getClient(@PathVariable Long id) {
        return clientRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createClient(@RequestBody User client) throws URISyntaxException {
        User savedClient = clientRepository.save(client);
        return ResponseEntity.created(new URI("/clients/" + savedClient.getId())).body(savedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateClient(@PathVariable Long id, @RequestBody User client) {
        User currentClient = clientRepository.findById(id).orElseThrow(RuntimeException::new);
        /*currentClient.setUsername(client.getUsername());*/
        currentClient = clientRepository.save(client);

        return ResponseEntity.ok(currentClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

