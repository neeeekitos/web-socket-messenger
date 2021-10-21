package server.service;

import common.domain.GroupChat;
import server.dao.GroupRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.NoSuchElementException;
import java.util.Optional;

@Data
@Service
public class GroupService {

    @Autowired
    private final GroupRepository groupRepository;

    public Optional<GroupChat> getGroup(final Long id) {
        return groupRepository.findById(id);
    }

    public Iterable<GroupChat> getGroups() {
        return groupRepository.findAll();
    }

    public void deleteGroup(final Long id) {
        groupRepository.deleteById(id);
    }

    public GroupChat saveGroup(GroupChat groupChat) {
        GroupChat savedGroupChat = groupRepository.save(groupChat);
        return savedGroupChat;
    }

    public GroupChat addParticipantToGroup(final String username, final Long id) throws NoSuchElementException {
        Optional<GroupChat> groupOptionnal = groupRepository.findById(id);
        // TODO check if original group is not found, manage exception
        GroupChat group = groupOptionnal.get();

        group.getParticipantsUsernames().add(username);
        GroupChat savedGroup = groupRepository.save(group);
        return savedGroup;
    }

    public GroupChat removeParticipantFromGroup(final String username, final Long id) {
        Optional<GroupChat> groupOptionnal = groupRepository.findById(id);
        // TODO check if original group is not found, manage exception
        GroupChat group = groupOptionnal.get();

        group.getParticipantsUsernames().remove(username);
        GroupChat savedGroup = groupRepository.save(group);
        return savedGroup;
    }

}