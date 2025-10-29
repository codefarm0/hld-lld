package com.learn.snake.ladder.game.repository;

import com.learn.snake.ladder.game.domain.Turn;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnRepository extends JpaRepository<Turn, UUID> {}


