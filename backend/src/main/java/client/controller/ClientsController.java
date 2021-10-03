package client.controller;

import client.service.ClientMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final ClientMessageService clientMessageService;

    @Autowired
    public ClientsController(ClientMessageService clientMessageService) {
        this.clientMessageService = clientMessageService;
    }

/*    @GetMapping
    public List<User> getClients() {
        //return clientRepository.findAll();
        return clientMessageService.get
        return null;
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
        *//*currentClient.setUsername(client.getUsername());*//*
        currentClient = clientRepository.save(client);

        return ResponseEntity.ok(currentClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }*/
}

