package com.learn.snake.ladder.game.service.impl;

import com.learn.snake.ladder.game.service.DiceService;
import java.util.random.RandomGenerator;
import org.springframework.stereotype.Component;

@Component
public class SixSidedDice implements DiceService {

	private final RandomGenerator random = RandomGenerator.getDefault();

	@Override
	public int roll() {
		return random.nextInt(1, 7);
	}

	@Override
	public int getMinValue() {
		return 1;
	}

	@Override
	public int getMaxValue() {
		return 6;
	}
}


