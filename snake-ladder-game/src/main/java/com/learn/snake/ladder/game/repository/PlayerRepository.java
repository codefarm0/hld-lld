package com.learn.snake.ladder.game.repository;

import com.learn.snake.ladder.game.domain.Player;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, UUID> {}


