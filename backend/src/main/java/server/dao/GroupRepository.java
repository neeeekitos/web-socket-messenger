package server.dao;

import common.domain.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupChat, Long> {
}
