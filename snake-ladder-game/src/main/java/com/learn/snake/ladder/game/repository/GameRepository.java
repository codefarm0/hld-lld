package com.learn.snake.ladder.game.repository;

import com.learn.snake.ladder.game.domain.Game;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, UUID> {

	@EntityGraph(attributePaths = {"players"})
	Optional<Game> findOneWithDetailsById(UUID id);
}


