package com.learn.snake.ladder.game.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "turns")
public class Turn {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(nullable = false)
	private int diceRoll;

	@Column(nullable = false)
	private int fromSquare;

	@Column(nullable = false)
	private int toSquare;

	@Column(nullable = false)
	private OffsetDateTime playedAt = OffsetDateTime.now();

	public Turn() {}

	public Turn(Game game, Player player, int diceRoll, int fromSquare, int toSquare) {
		this.game = game;
		this.player = player;
		this.diceRoll = diceRoll;
		this.fromSquare = fromSquare;
		this.toSquare = toSquare;
	}

	public UUID getId() {
		return id;
	}

	public Game getGame() {
		return game;
	}

	public Player getPlayer() {
		return player;
	}

	public int getDiceRoll() {
		return diceRoll;
	}

	public int getFromSquare() {
		return fromSquare;
	}

	public int getToSquare() {
		return toSquare;
	}

	public OffsetDateTime getPlayedAt() {
		return playedAt;
	}
}


