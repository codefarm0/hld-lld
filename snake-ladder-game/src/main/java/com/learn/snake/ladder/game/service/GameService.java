package com.learn.snake.ladder.game.service;

import com.learn.snake.ladder.game.domain.Game;
import com.learn.snake.ladder.game.domain.GameStatus;
import com.learn.snake.ladder.game.domain.Ladder;
import com.learn.snake.ladder.game.domain.Player;
import com.learn.snake.ladder.game.domain.Snake;
import com.learn.snake.ladder.game.domain.Turn;
import com.learn.snake.ladder.game.repository.GameRepository;
import com.learn.snake.ladder.game.repository.PlayerRepository;
import com.learn.snake.ladder.game.repository.TurnRepository;
import com.learn.snake.ladder.game.repository.SnakeRepository;
import com.learn.snake.ladder.game.repository.LadderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final PlayerRepository playerRepository;
	private final TurnRepository turnRepository;
	private final SnakeRepository snakeRepository;
	private final LadderRepository ladderRepository;
	private final DiceService diceService;
	private final GameRules gameRules;

	public GameService(
		GameRepository gameRepository,
		PlayerRepository playerRepository,
		TurnRepository turnRepository,
		SnakeRepository snakeRepository,
		LadderRepository ladderRepository,
		DiceService diceService,
		GameRules gameRules
	) {
		this.gameRepository = gameRepository;
		this.playerRepository = playerRepository;
		this.turnRepository = turnRepository;
		this.snakeRepository = snakeRepository;
		this.ladderRepository = ladderRepository;
		this.diceService = diceService;
		this.gameRules = gameRules;
	}

	@Transactional
	public Game createStandardGame(List<String> playerNames) {
		Game game = new Game();
		// standard snakes/ladders
		for (Snake s : standardSnakes()) game.addSnake(s);
		for (Ladder l : standardLadders()) game.addLadder(l);
		for (String name : playerNames) game.addPlayer(new Player(name));
		return gameRepository.save(game);
	}

	@Transactional
	public void startGame(UUID gameId) {
		Game game = requireGame(gameId);
		if (game.getPlayers().size() < 2) throw new IllegalStateException("Need at least 2 players");
		game.setStatus(GameStatus.IN_PROGRESS);
		game.touch();
		gameRepository.save(game);
	}


	@Transactional(readOnly = true)
	public Optional<Game> getGame(UUID gameId) {
		return gameRepository.findOneWithDetailsById(gameId);
	}

	@Transactional(readOnly = true)
	public List<Snake> getSnakes(UUID gameId) {
		return snakeRepository.findAllByGame_Id(gameId);
	}

	@Transactional(readOnly = true)
	public List<Ladder> getLadders(UUID gameId) {
		return ladderRepository.findAllByGame_Id(gameId);
	}

	@Transactional
	public Turn playTurn(UUID gameId) {
		Game game = requireGame(gameId);
		if (game.getStatus() != GameStatus.IN_PROGRESS) throw new IllegalStateException("Game not in progress");
		if (game.getPlayers().isEmpty()) throw new IllegalStateException("No players");

		int idx = game.getCurrentPlayerIndex();
		Player current = game.getPlayers().get(idx);
		int roll = diceService.roll();
		if (!gameRules.canPlayerMove(current, roll)) {
			advanceTurn(game);
			return turnRepository.save(new Turn(game, current, roll, current.getPosition(), current.getPosition()));
		}
		int newPos = gameRules.calculateNewPosition(game, current.getPosition(), roll);
		Turn turn = new Turn(game, current, roll, current.getPosition(), newPos);
		current.setPosition(newPos);
		current.incrementTurns();

		if (newPos == 100) {
			game.setStatus(GameStatus.COMPLETED);
		} else {
			advanceTurn(game);
		}
		game.touch();
		playerRepository.save(current);
		gameRepository.save(game);
		return turnRepository.save(turn);
	}

	private void advanceTurn(Game game) {
		int next = (game.getCurrentPlayerIndex() + 1) % game.getPlayers().size();
		game.setCurrentPlayerIndex(next);
	}

	private Game requireGame(UUID gameId) {
		return gameRepository.findById(gameId)
			.orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
	}

	private static List<Snake> standardSnakes() {
		List<Snake> snakes = new ArrayList<>();
		snakes.add(new Snake(98, 60));
		snakes.add(new Snake(95, 56));
		snakes.add(new Snake(87, 24));
		snakes.add(new Snake(62, 19));
		snakes.add(new Snake(51, 7));
		snakes.add(new Snake(47, 26));
		snakes.add(new Snake(36, 6));
		return snakes;
	}

	private static List<Ladder> standardLadders() {
		List<Ladder> ladders = new ArrayList<>();
		ladders.add(new Ladder(2, 23));
		ladders.add(new Ladder(8, 34));
		ladders.add(new Ladder(28, 77));
		ladders.add(new Ladder(21, 42));
		ladders.add(new Ladder(50, 93));
		ladders.add(new Ladder(71, 92));
		ladders.add(new Ladder(80, 100));
		return ladders;
	}
}


