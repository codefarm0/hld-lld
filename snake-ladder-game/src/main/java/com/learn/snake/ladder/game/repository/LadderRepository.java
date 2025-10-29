package com.learn.snake.ladder.game.repository;

import com.learn.snake.ladder.game.domain.Ladder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LadderRepository extends JpaRepository<Ladder, UUID> {
	List<Ladder> findAllByGame_Id(UUID gameId);
}


