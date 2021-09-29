package service;

import common.O2o;
import dao.O2oRepository;
import dao.UserRepository;
import domain.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class O2oService {

    private final O2oRepository o2oRepository;

    @Autowired
    public O2oService(O2oRepository o2oRepository) {
        this.o2oRepository = o2oRepository;
    }

    public Optional<O2o> getO2o(final Long id) {
        return o2oRepository.findById(id);
    }

    public Iterable<O2o> getO2os() {
        return o2oRepository.findAll();
    }

    public void deleteO2o(final Long id) {
        o2oRepository.deleteById(id);
    }

    public O2o saveO2o(O2o O2o) {
        O2o savedO2o = o2oRepository.save(O2o);
        return savedO2o;
    }

}