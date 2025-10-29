package com.learn.snake.ladder.game.repository;

import com.learn.snake.ladder.game.domain.Snake;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SnakeRepository extends JpaRepository<Snake, UUID> {
	List<Snake> findAllByGame_Id(UUID gameId);
}


