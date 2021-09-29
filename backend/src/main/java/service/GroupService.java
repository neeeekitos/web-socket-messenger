package service;

import common.Group;
import dao.GroupRepository;
import dao.UserRepository;
import domain.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Optional<Group> getGroup(final Long id) {
        return groupRepository.findById(id);
    }

    public Iterable<Group> getGroups() {
        return groupRepository.findAll();
    }

    public void deleteGroup(final Long id) {
        groupRepository.deleteById(id);
    }

    public Group saveUser(Group group) {
        Group savedGroup = groupRepository.save(group);
        return savedGroup;
    }

}