# Class Diagram

Short overview
- The domain is persisted with JPA entities (`Game`, `Player`, `Snake`, `Ladder`, `Turn`).
- Application logic lives in `GameService` using `DiceService` and `GameRules` strategies.
- `GameController` orchestrates web flow and prepares lightweight view records.
- Repositories encapsulate persistence; H2 is configured via Spring Boot.

```mermaid
classDiagram
  direction LR

  class Game {
    UUID id
    GameStatus status
    int currentPlayerIndex
    OffsetDateTime createdAt
    OffsetDateTime updatedAt
    +List~Player~ players
    +List~Snake~ snakes
    +List~Ladder~ ladders
    +touch()
  }

  class Player {
    UUID id
    String name
    int position
    int turnsPlayed
  }

  class Snake { UUID id; int head; int tail }
  class Ladder { UUID id; int bottom; int top }

  class Turn {
    UUID id
    Game game
    Player player
    int diceRoll
    int fromSquare
    int toSquare
    OffsetDateTime playedAt
  }

  class GameStatus {
    <<enumeration>>
    WAITING_FOR_PLAYERS
    IN_PROGRESS
    PAUSED
    COMPLETED
    ABANDONED
  }

  class DiceService {
    <<interface>>
    +roll() int
    +getMinValue() int
    +getMaxValue() int
  }

  class SixSidedDice
  DiceService <|.. SixSidedDice

  class GameRules {
    <<interface>>
    +canPlayerMove(Player,int) boolean
    +isValidDiceRoll(int) boolean
    +calculateNewPosition(Game,int,int) int
  }
  class StandardGameRules
  GameRules <|.. StandardGameRules

  class GameService {
    +createStandardGame(names: List~String~) Game
    +startGame(gameId: UUID)
    +playTurn(gameId: UUID) Turn
    +getGame(gameId: UUID) Optional~Game~
    +getSnakes(UUID): List~Snake~
    +getLadders(UUID): List~Ladder~
  }

  class GameController {
    +index()
    +createGame(players: List~String~)
    +viewGame(id: UUID)
    +startGame(id: UUID)
    +playTurn(id: UUID)
  }

  class GameRepository { <<JpaRepository>> }
  class PlayerRepository { <<JpaRepository>> }
  class SnakeRepository { <<JpaRepository>> }
  class LadderRepository { <<JpaRepository>> }
  class TurnRepository { <<JpaRepository>> }

  Game "1" o-- "*" Player
  Game "1" o-- "*" Snake
  Game "1" o-- "*" Ladder
  Turn "*" --> "1" Game
  Turn "*" --> "1" Player

  GameController --> GameService
  GameService --> DiceService
  GameService --> GameRules
  GameService --> GameRepository
  GameService --> PlayerRepository
  GameService --> SnakeRepository
  GameService --> LadderRepository
  GameService --> TurnRepository
```


