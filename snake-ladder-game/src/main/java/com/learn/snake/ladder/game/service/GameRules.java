package com.learn.snake.ladder.game.service;

import com.learn.snake.ladder.game.domain.Game;
import com.learn.snake.ladder.game.domain.Player;

public interface GameRules {
	boolean canPlayerMove(Player player, int diceRoll);
	boolean isValidDiceRoll(int roll);
	int calculateNewPosition(Game game, int currentPosition, int diceRoll);
}


