package com.learn.snake.ladder.game.web;

import com.learn.snake.ladder.game.domain.Game;
import com.learn.snake.ladder.game.domain.GameStatus;
import com.learn.snake.ladder.game.domain.Player;
import com.learn.snake.ladder.game.domain.Snake;
import com.learn.snake.ladder.game.domain.Ladder;
import com.learn.snake.ladder.game.domain.Turn;
import com.learn.snake.ladder.game.service.GameService;
import com.learn.snake.ladder.game.web.view.SnakeLine;
import com.learn.snake.ladder.game.web.view.LadderLine;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GameController {

	private final GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/create")
	public String createGame(@RequestParam(name = "players") List<String> playerNames) {
		Game game = gameService.createStandardGame(playerNames);
		return "redirect:/game/" + game.getId();
	}

	@GetMapping("/game/{id}")
	public String viewGame(@PathVariable("id") UUID id, Model model) {
		Optional<Game> game = gameService.getGame(id);
		if (game.isEmpty()) return "redirect:/";
		Game g = game.get();
		model.addAttribute("game", g);
		model.addAttribute("players", g.getPlayers());
		model.addAttribute("status", g.getStatus());
		// lightweight data for svg (avoid serializing entities)
		List<Snake> snakes = gameService.getSnakes(g.getId());
		List<Ladder> ladders = gameService.getLadders(g.getId());
		List<SnakeLine> snakeLines = new ArrayList<>();
		for (Snake s : snakes) snakeLines.add(new SnakeLine(s.getHead(), s.getTail()));
		List<LadderLine> ladderLines = new ArrayList<>();
		for (Ladder l : ladders) ladderLines.add(new LadderLine(l.getBottom(), l.getTop()));
		model.addAttribute("snakeLines", snakeLines);
		model.addAttribute("ladderLines", ladderLines);
		// color map for players
		Map<UUID, String> playerColors = new HashMap<>();
		List<String> palette = Arrays.asList(
			"#e76f51", "#2a9d8f", "#e9c46a", "#264653", "#8ab17d", "#f4a261", "#7b2cbf", "#118ab2"
		);
		for (int i = 0; i < g.getPlayers().size(); i++) {
			Player p = g.getPlayers().get(i);
			playerColors.put(p.getId(), palette.get(i % palette.size()));
		}
		model.addAttribute("playerColors", playerColors);
		// winner if completed
		UUID winnerId = null;
		String winnerName = null;
		if (g.getStatus() == GameStatus.COMPLETED) {
			for (Player p : g.getPlayers()) {
				if (p.getPosition() == 100) { winnerId = p.getId(); winnerName = p.getName(); break; }
			}
		}
		model.addAttribute("winnerId", winnerId);
		model.addAttribute("winnerName", winnerName);
		return "game";
	}

	@PostMapping("/game/{id}/start")
	public String startGame(@PathVariable("id") UUID id) {
		gameService.startGame(id);
		return "redirect:/game/" + id;
	}


	@PostMapping("/game/{id}/turn")
	public String playTurn(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
		Turn turn = gameService.playTurn(id);
		int from = turn.getFromSquare();
		int roll = turn.getDiceRoll();
		int landing = Math.min(100, from + roll);
		int to = turn.getToSquare();
		String msg;
		if (to > landing) {
			msg = turn.getPlayer().getName() + " climbed ladder from " + landing + " to " + to + " (roll " + roll + ")";
		} else if (to < landing) {
			msg = turn.getPlayer().getName() + " got bitten by snake from " + landing + " to " + to + " (roll " + roll + ")";
		} else {
			msg = turn.getPlayer().getName() + " moved from " + from + " to " + to + " (roll " + roll + ")";
		}
		redirectAttributes.addFlashAttribute("lastMessage", msg);
		redirectAttributes.addFlashAttribute("lastRoll", roll);
		redirectAttributes.addFlashAttribute("moveFrom", from);
		redirectAttributes.addFlashAttribute("moveTo", to);
		redirectAttributes.addFlashAttribute("movePlayerId", turn.getPlayer().getId().toString());
		return "redirect:/game/" + id;
	}
}


