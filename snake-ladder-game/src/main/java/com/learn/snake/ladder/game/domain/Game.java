package com.learn.snake.ladder.game.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private GameStatus status = GameStatus.WAITING_FOR_PLAYERS;

	@Column(nullable = false)
	private int currentPlayerIndex = 0;

	@Column(nullable = false)
	private OffsetDateTime createdAt = OffsetDateTime.now();

	@Column(nullable = false)
	private OffsetDateTime updatedAt = OffsetDateTime.now();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Player> players = new ArrayList<>();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Snake> snakes = new ArrayList<>();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Ladder> ladders = new ArrayList<>();

	public UUID getId() {
		return id;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void touch() {
		this.updatedAt = OffsetDateTime.now();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Snake> getSnakes() {
		return snakes;
	}

	public List<Ladder> getLadders() {
		return ladders;
	}

	public void addPlayer(Player player) {
		player.setGame(this);
		this.players.add(player);
	}

	public void addSnake(Snake snake) {
		snake.setGame(this);
		this.snakes.add(snake);
	}

	public void addLadder(Ladder ladder) {
		ladder.setGame(this);
		this.ladders.add(ladder);
	}
}


