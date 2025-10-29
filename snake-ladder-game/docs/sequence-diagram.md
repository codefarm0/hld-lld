# Sequence Diagram

Short overview
- A user creates a game from the UI; controller delegates to service.
- On play turn: service rolls the dice, applies rules, updates entities, persists a Turn, and computes winner.
- Controller redirects back with flash attributes for UI messaging/animations.

```mermaid
sequenceDiagram
  autonumber
  actor U as User
  participant C as GameController
  participant S as GameService
  participant D as DiceService
  participant Rg as GameRules
  participant RG as GameRepository
  participant RP as PlayerRepository
  participant RT as TurnRepository

  U->>C: POST /create (players)
  C->>S: createStandardGame(names)
  S->>RG: save(Game + snakes + ladders + players)
  RG-->>S: Game
  S-->>C: Game
  C-->>U: 302 redirect /game/{id}

  U->>C: POST /game/{id}/start
  C->>S: startGame(id)
  S->>RG: save(status=IN_PROGRESS)
  C-->>U: 302 redirect /game/{id}

  U->>C: POST /game/{id}/turn
  C->>S: playTurn(id)
  S->>RG: findById(id)
  RG-->>S: Game
  S->>D: roll()
  D-->>S: diceRoll
  S->>Rg: canPlayerMove(player, roll)
  Rg-->>S: boolean
  S->>Rg: calculateNewPosition(game, pos, roll)
  Rg-->>S: newPos
  S->>RP: save(player position/turns)
  S->>RG: save(game status/index)
  S->>RT: save(Turn)
  S-->>C: Turn
  C-->>U: 302 redirect /game/{id} (flash: lastMessage, lastRoll, moveFrom, moveTo)
```



