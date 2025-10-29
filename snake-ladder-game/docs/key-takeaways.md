# Key Takeaways: Snake & Ladder – Software Engineering Concepts

### Domain Modeling
- **Aggregates and invariants**: Game as aggregate root; squares 1–100; snake head>tail; ladder bottom<top.
- **Rules as strategies**: Pluggable `GameRules` (e.g., roll 6 to start, extra turn on 6).
- **Configuration validation**: Prevent overlaps/cycles; factories/builders for boards.

### State Machines and Lifecycle
- **Explicit states**: WAITING, IN_PROGRESS, PAUSED, COMPLETED; legal transitions.
- **Turn rotation & fairness**: Next player calc, extra turn edge cases, win mid-turn.

### Randomness and Determinism
- **RNG**: Quality and seeding; deterministic seeds for tests and replay.
- **Test doubles**: Loaded/fixed dice for scenario testing.

### Persistence and Data Design
- **Aggregate boundaries**: `Game` owns `players/snakes/ladders`.
- **Event/audit log**: Append-only `Turn` for replay/time-travel/debugging.

### Concurrency Control
- **Single-writer per game**: Prevent concurrent `playTurn` on same game.
- **Techniques**: Optimistic locking with retries; per-game mutex/actor; idempotency keys.

### Transactions and Consistency
- **Atomicity**: Update position + game status + turn record together.
- **Outbox**: If emitting events, ensure DB→broker consistency.

### Real-Time Updates and Messaging
- **Event-driven UI**: Observer/WebSocket channels keyed by `gameId`.
- **Fan-out & presence**: Handle multiple viewers; subscribe/unsubscribe lifecycle.

### APIs and Security
- **Authorization**: Only current player can act; room membership checks.
- **Anti-cheat**: Server-side dice; throttle/rate limit; input validation.

### Scalability and Deployment
- **Sharding/routing**: Partition by `gameId` (sticky routing) to reduce cross-node contention.
- **Hot paths**: Cache static board config; target <100ms turn latency.

### Fault Tolerance
- **Recovery**: Timeouts for inactive players; pause/abandon flows; retries with idempotency.

### Observability
- **Metrics**: turns/sec, latency, win distribution, snake/ladder hit rates.
- **Tracing & logs**: Correlate by `gameId/turnId`; structured logs.

### Testing Strategies
- **Property-based**: Positions stay within 1..100; legal state transitions.
- **Deterministic replay**: Seeded RNG; golden replays for regressions.

### Frontend/UX Architecture
- **Serpentine grid**: Data-square mapping for accurate overlays.
- **Overlay layers**: SVG for snakes/ladders; independent from tile layout.
- **Non-intrusive animations**: Don’t couple visuals to rules.

### Extensibility
- **Hexagonal design**: Ports/adapters for Dice, Rules, Repos, Events.
- **Platformization**: `BoardGame` interface; factories for multiple games.

---

### Interview Prompts to Practice
- How do you prevent two concurrent `playTurn` calls on the same game? Compare optimistic locking vs per-game lock/actor.
- How do you make dice rolls auditable and replayable while keeping tests deterministic?
- Describe your event model for live updates. Would you use the outbox pattern? Why?
- How do you validate a custom board to avoid illegal/cyclic configurations?
- Scale to 100k concurrent games: routing, contention avoidance, data store choices.
- Implement “extra turn on 6” and “no overshoot of 100” without coupling UI and rules.
- If a client disconnects mid-turn, what idempotent recovery behavior do you design?
