package client.service;

import common.domain.O2o;
import server.dao.O2oRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class ClientO2oService {
//
//    private final O2oRepository o2oRepository;
//
//    @Autowired
//    public ClientO2oService(O2oRepository o2oRepository) {
//        this.o2oRepository = o2oRepository;
//    }
//
//    public Optional<O2o> getO2o(final Long id) {
//        return o2oRepository.findById(id);
//    }
//
//    public Iterable<O2o> getO2os() {
//        return o2oRepository.findAll();
//    }
//
//    public void deleteO2o(final Long id) {
//        o2oRepository.deleteById(id);
//    }
//
//    public O2o saveO2o(O2o O2o) {
//        O2o savedO2o = o2oRepository.save(O2o);
//        return savedO2o;
//    }

}