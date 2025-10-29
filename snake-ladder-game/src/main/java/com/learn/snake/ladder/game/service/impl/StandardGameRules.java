package com.learn.snake.ladder.game.service.impl;

import com.learn.snake.ladder.game.domain.Game;
import com.learn.snake.ladder.game.domain.Ladder;
import com.learn.snake.ladder.game.domain.Player;
import com.learn.snake.ladder.game.domain.Snake;
import com.learn.snake.ladder.game.service.GameRules;
import org.springframework.stereotype.Component;

@Component
public class StandardGameRules implements GameRules {

	@Override
	public boolean canPlayerMove(Player player, int diceRoll) {
		return isValidDiceRoll(diceRoll);
	}

	@Override
	public boolean isValidDiceRoll(int roll) {
		return roll >= 1 && roll <= 6;
	}

	@Override
	public int calculateNewPosition(Game game, int currentPosition, int diceRoll) {
		int tentative = Math.min(100, currentPosition + diceRoll);
		int pos = tentative;
		// ladders first
		for (Ladder ladder : game.getLadders()) {
			if (ladder.getBottom() == pos) {
				pos = ladder.getTop();
				break;
			}
		}
		// snakes
		for (Snake snake : game.getSnakes()) {
			if (snake.getHead() == pos) {
				pos = snake.getTail();
				break;
			}
		}
		return pos;
	}
}


