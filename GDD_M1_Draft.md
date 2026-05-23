# Maze Runner - Game Design Document

**Project:** Maze Runner  
**Genre:** 2D top-down stealth maze game  
**Platform:** Desktop PC  
**Engine / Framework:** libGDX, Java, LWJGL3  
**Team:** Ayaulym and Ulan  
**Version:** Final Draft, 2026

---

## 1. Planning

### 1.1 Game Concept

Maze Runner is a 2D top-down pixel-art stealth maze game for desktop, built with libGDX. The player explores four maze levels, collects useful items, unlocks different door types, avoids enemy patrols, and escapes through portals. The main gameplay loop is: explore the maze, find an item, select the correct active item, unlock the path, avoid or eliminate enemies, and reach the exit. The player wins by reaching the final portal on Level 4 and loses when HP reaches 0 after being caught by enemies.

### 1.2 Core Experience

The game is designed around tension and readable stealth decisions. The player must understand where enemies are looking, choose when to move, and decide whether to avoid an enemy or use the Knife from behind. The levels become more complex over time: Level 1 teaches item-door logic, Level 2 introduces patrols, Level 3 focuses on stealth kill and Crowbar progression, and Level 4 combines all mechanics into a final escape route.

### 1.3 Target Audience

Maze Runner is intended for students and casual PC players who enjoy simple stealth mechanics, maze navigation, and short level-based challenges. The controls are intentionally simple so the player can focus on route planning and enemy behavior.

### 1.4 Team Roles

| Role | Responsibility | Member |
| --- | --- | --- |
| Project Lead | Planning, task control, final decisions, documentation | Ayaulym |
| Lead Programmer | Core gameplay, player systems, architecture, patterns | Ayaulym |
| Programmer | UI screens, bug fixing, testing support | Ulan |
| Level Designer | Maze layouts, enemy placement, level difficulty | Ulan |
| Tester | Full playthrough, bug reports, balance feedback | Both |

### 1.5 Tools

| Tool | Purpose |
| --- | --- |
| libGDX | Game framework and desktop rendering |
| Java | Main programming language |
| Gradle | Build system and project tasks |
| GitHub | Version control, branches, repository submission |
| VS Code / IntelliJ IDEA | Code editing and running |
| ClickUp | Task planning and milestone tracking |
| Draw.io / generated PNG diagrams | Class diagram, game flow diagram, level sketch |

### 1.6 Milestones

| Milestone | Goal | Result |
| --- | --- | --- |
| M1 - Design | GDD, diagrams, concept, team plan | GDD and diagrams prepared |
| M2 - Prototype | Movement, camera, collisions, first key-door-exit loop | Implemented |
| M3 - Alpha | Enemies, patrol, chase, stealth kill, all levels | Implemented |
| M4 - Beta | Menus, pause, game over, win screen, HUD, audio | Implemented |
| M5 - Final | Testing, polish, README, final package | In final review |

---

## 2. Game Design

### 2.1 Game Summary

Maze Runner is a tile-based maze escape game with stealth elements. The player controls a small character from a top-down view and moves through dark, stylized maze environments. Each level contains walls, paths, items, doors, enemies, and an exit portal. Some paths are locked by wooden or iron doors. The player must collect the required item, select it from the inventory, open the door, and continue to the portal.

The game uses simple pixel-inspired shapes and a dark maze atmosphere. The visual language is readable: walls are solid and textured, floors are darker paths, items have distinct icons and colors, enemies have direction-based bodies and flashlight-like vision, and the player visually turns depending on movement direction.

### 2.2 Player Goal

The player's goal is to escape all four levels. A level is completed by reaching its exit portal. On Levels 1-3, the portal opens the next level. On Level 4, the final portal opens the Win Screen.

### 2.3 Core Gameplay Loop

1. Spawn at the level start.
2. Explore the maze while avoiding walls and enemies.
3. Pick up items with `E`.
4. Select the needed item with number keys.
5. Open the correct door with `E`.
6. Avoid enemies or stealth kill them from behind with the Knife.
7. Reach the portal.
8. Continue to the next level or finish the game.

### 2.4 Controls

| Key / Input | Action |
| --- | --- |
| W / A / S / D | Move up, left, down, right |
| Arrow Keys | Alternative movement |
| E | Interact: pick up item, open selected door, use active item near object |
| F | Stealth kill nearby enemy with selected Knife |
| 1 | Select Key |
| 2 | Select Crowbar |
| 3 | Select Knife |
| 4 | Select Disguise |
| F4 | Quick-use Disguise if available |
| M | Toggle background music |
| Escape | Pause or resume gameplay |
| Mouse Click | Select menu and overlay buttons |

### 2.5 Player Stats and Abilities

| Attribute | Value / Description |
| --- | --- |
| HP | 3 HP. Enemy catch removes 1 HP. At 0 HP, Game Over opens. |
| Movement Speed | 250 units per second. Player is responsive and faster than patrol enemies. |
| Collision | Player cannot walk through walls or closed doors. |
| Inventory | Player can carry several items at once. |
| Active Item | Some actions require selecting the correct item first. |
| Direction | Player sprite changes facing direction based on movement. |
| Respawn | After being caught, player returns to current level spawn. |
| Stealth Kill | Requires Knife, rear position, close distance, and timing before enemy reaction. |
| Disguise | Temporarily prevents enemies from detecting the player. |
| Healing | Medkit restores HP if the player is damaged. |

### 2.6 Items

| Item | Visual Meaning | Gameplay Purpose |
| --- | --- | --- |
| Key | Golden key icon | Opens wooden doors when selected. Consumed on use. |
| Crowbar | Metal/orange tool icon | Opens iron doors when selected. Consumed on use. |
| Knife | Clear blade icon | Allows stealth kill from behind with `F`. |
| Medkit | Healing item | Restores HP when picked up if HP is not full. |
| Disguise | Mask-like item | Temporarily prevents enemy detection. |

### 2.7 Doors and Exit

| Object | Required Item | Behaviour |
| --- | --- | --- |
| Wooden Door | Key | Blocks route until selected Key is used. |
| Iron Door | Crowbar | Blocks route until selected Crowbar is used. |
| Exit Portal | None directly, but may be behind doors | Completes the level or wins the game on Level 4. |

Door interaction is intentionally strict: having the item is not enough. The player must select the correct active item and press `E`. This supports the inventory mechanic and makes item choice visible in the HUD.

### 2.8 Enemy Behaviour

Enemies use a state-based AI system. They patrol, detect the player, chase, search, return, or die.

| State | Trigger | Behaviour |
| --- | --- | --- |
| PATROL | Default enemy state | Enemy follows a patrol path. |
| ALERT | Detection begins | Enemy reacts to the player and prepares chase behavior. |
| CHASE | Player visible or recently detected | Enemy pathfinds toward the player or last known position. |
| SEARCH | Player escapes direct vision | Enemy searches around the last known position. |
| RETURN | Search ends or path fails | Enemy returns to its patrol route. |
| DEAD | Successful stealth kill | Enemy stops updating and cannot damage the player. |

### 2.9 Enemy Detection Rules

Enemies use direction-based vision. The player is detected when:

- the player is in front of the enemy;
- the player is inside the enemy's visible range;
- there is no wall between the enemy and player;
- the player is not disguised.

Walls block line of sight. If the player hides behind walls or escapes far enough, the enemy searches and later returns to patrol.

### 2.10 Stealth Kill Rules

Stealth kill is possible only when all conditions are true:

- player has the Knife;
- Knife is selected as active item;
- player is behind the enemy;
- player is close enough;
- enemy has not reacted yet;
- player presses `F`.

If the player waits behind an enemy for too long, the enemy turns and starts chasing. This creates a fair risk-reward timing window.

### 2.11 Screens and UI

| Screen | Contents / Purpose |
| --- | --- |
| Main Menu | Game title, Play, Quit, music indicator |
| Game Screen | Maze, player, enemies, items, doors, portal, HUD |
| Pause Overlay | Resume, Restart Level, Main Menu |
| Level Complete Overlay | Continue to next level, Main Menu |
| Game Over Screen | Retry current level, Main Menu |
| Win Screen | Completion message, Main Menu |

### 2.12 HUD

The HUD displays:

- HP hearts;
- current level number;
- inventory slots;
- active item highlight;
- control hints;
- music on/off status;
- status messages such as item pickup, locked door, or stealth kill feedback.

### 2.13 Audio

The game includes an audio system with background music and sound effects.

| Audio Type | Description |
| --- | --- |
| Menu Music | Loops in the Main Menu. |
| Gameplay Music | Loops during gameplay. |
| Item Pickup SFX | Plays when collecting items. |
| Door SFX | Plays when opening doors. |
| Damage SFX | Plays when enemy catches the player. |
| Stealth Kill SFX | Plays on successful stealth kill. |
| Button Click SFX | Plays when pressing menu/overlay buttons. |
| Win / Game Over SFX | Plays on end states. |

Music can be toggled with `M`. Sound effects remain audible over music.

---

## 3. Level Design

### 3.1 Level Progression

The four levels gradually introduce and combine mechanics.

| Level | Main Goal | New / Main Mechanics | Enemy Count |
| --- | --- | --- | --- |
| Level 1 | Find Key, open wooden door, reach portal | Movement, collision, item pickup, wooden door | 0 |
| Level 2 | Navigate patrol maze and escape | Enemy patrols, chase, Medkit, Disguise | 3 |
| Level 3 | Use Crowbar and Knife to escape | Iron door, stealth kill, stronger patrol pressure | 4 |
| Level 4 | Final escape with all mechanics | Key, Crowbar, Knife, Disguise, Medkit, final route | 5 |

### 3.2 Level 1 - First Escape Route

Level 1 teaches the basic loop without enemies. The player starts near the bottom-left area, explores the maze, picks up the Key, returns toward the locked wooden door, opens it, and enters the portal. The purpose is to make the player understand walls, interaction, inventory selection, and door logic.

### 3.3 Level 2 - Guard Patrol Maze

Level 2 introduces enemy patrols. The maze is longer and contains multiple enemies with patrol paths. The player must observe enemy movement, avoid direct vision, use a Medkit if damaged, and optionally use Disguise. The door logic remains familiar: collect Key, select it, open wooden door, reach portal.

### 3.4 Level 3 - Locked Vault Route

Level 3 introduces Crowbar progression and stronger stealth play. The player can pick up Knife and use stealth kill to eliminate enemies from behind. The Iron Door requires Crowbar, making the level more complex than a simple key-door route.

### 3.5 Level 4 - Final Escape Route

Level 4 combines all major systems. It includes multiple enemies, Crowbar, Key, Knife, Medkit, Disguise, iron door, wooden door, and final portal. The player must use knowledge from previous levels to escape. This level is intended to feel more tense but still fair after testing.

---

## 4. Art and Visual Style

### 4.1 Art Direction

Maze Runner uses a dark pixel-art inspired style. Instead of external sprite sheets, the current implementation uses shape-rendered pixel art. This makes every visual element code-driven and consistent with the libGDX prototype.

### 4.2 Environment Style

Each level has a different wall and floor design:

- Level 1: cold industrial stone;
- Level 2: darker security dungeon tones;
- Level 3: ruin / warehouse-like brown tones;
- Level 4: final maze with laboratory-like teal highlights.

Floors and walls include decorative details such as grooves, cracks, panels, highlights, shadows, and small objects. This prevents the game from looking like plain rectangles.

### 4.3 Character Visuals

The player is not a static square. The character has a cloak, head, arms, legs, held item, shadow, and directional rendering. When the player moves left, right, up, or down, the visual orientation changes.

Enemies also have directional visuals. They have masks, cloaks, different colors for states, and flashlight-like vision. The enemy's direction is readable through body orientation and light cone, not through a simple debug marker.

### 4.4 Item and Door Visuals

Items use distinct readable icons:

- Key looks like a golden key;
- Crowbar looks like a tool;
- Knife has a clear blade and handle;
- Disguise resembles a mask;
- Medkit indicates healing.

Doors have separate designs:

- wooden doors use brown boards and keyhole;
- iron doors use metal plates and rivets;
- portals use glow and rune-like details.

---

## 5. Architecture and Design Patterns

The project uses design patterns as part of the real implementation, not only as theory.

### 5.1 Pattern Map

| Pattern | Implementation | Purpose |
| --- | --- | --- |
| Command | `ICommand`, `MoveCommand`, `InteractCommand`, `StealthKillCommand` | Separates keyboard input from gameplay actions. |
| Factory | `ItemFactory` | Creates items by type string for level setup. |
| Decorator | `Door`, `BaseDoor`, `LockedDoor` | Adds locked behavior to base door opening. |
| Strategy | `MovementStrategy`, `PatrolStrategy`, `ChaseStrategy`, `SearchStrategy`, `ReturnStrategy` | Allows enemies to change movement logic by state. |
| State | `EnemyState` and enemy state transitions | Controls patrol, chase, search, return, and death logic. |
| Observer | `HPObserver`, `Player`, `GameScreen` | Notifies screen logic when HP changes. |
| Facade | `GameFacade` | Simplifies screen transitions and restart/menu actions. |
| Adapter | `ILevelMap`, `ArrayLevelMapAdapter` | Separates game logic from raw map array storage. |
| Singleton | `AudioManager` | Provides one shared audio system for music and sound effects. |

### 5.2 Main Classes

| Class | Responsibility |
| --- | --- |
| `Main` | libGDX Game entry point, loads audio, starts Main Menu. |
| `MainMenuScreen` | Main menu UI, Play/Quit/music toggle. |
| `GameScreen` | Main game loop, rendering, update, HUD, pause, level complete overlay. |
| `GameOverScreen` | Retry and Main Menu after HP reaches 0. |
| `WinScreen` | Final completion screen. |
| `GameFacade` | Provides clean screen transition methods. |
| `Player` | Player position, HP, facing, inventory, active item, rendering. |
| `PlayerInputHandler` | Reads keyboard input and creates commands. |
| `LevelMap` | Map data, items, doors, enemies, collision, line of sight, level objects. |
| `Enemy` | Enemy state, detection, movement strategy, rendering, stealth kill rules. |
| `ItemFactory` | Creates item instances. |
| `DoorEntity` | Door rendering, bounds, and interaction. |
| `AudioManager` | Loads, plays, toggles, and disposes music and SFX. |

### 5.3 SOLID Principles

| Principle | Application |
| --- | --- |
| Single Responsibility | Player, Enemy, LevelMap, AudioManager, and Screens have separate jobs. |
| Open / Closed | New item types can be added through Item and ItemFactory. |
| Liskov Substitution | All item implementations can be used through the Item interface. |
| Interface Segregation | Small interfaces are used for commands, items, map access, and HP observing. |
| Dependency Inversion | LevelMap works through `ILevelMap` rather than directly depending only on raw arrays. |

---

## 6. Implementation Details

### 6.1 Technology

| Technology | Usage |
| --- | --- |
| Java | Game code |
| libGDX | Rendering, input, audio, game loop |
| LWJGL3 | Desktop launcher |
| Gradle | Build and run |
| GitHub | Version control and collaboration |

### 6.2 Rendering

The game uses `ShapeRenderer` for pixel-style shapes and `SpriteBatch` / `BitmapFont` for UI text. The camera follows the player and clamps to map boundaries. A separate HUD camera keeps UI elements fixed on screen.

### 6.3 Map System

Levels are represented with integer tile arrays wrapped by `ArrayLevelMapAdapter`. A tile value of `1` represents a wall and `0` represents a walkable floor. This adapter approach allows the game to later support another map format without rewriting collision and pathfinding logic.

### 6.4 Collision

Collision checks prevent movement through:

- wall tiles;
- closed doors;
- map boundaries.

The player and enemies use rectangle bounds. Door and wall checks happen before movement is applied.

### 6.5 Enemy Pathfinding

Enemies use simple tile-based pathfinding through `LevelMap.findNextStep`. This lets chasing and searching enemies move around walls instead of walking directly into them. If a path fails, the enemy changes behavior to SEARCH or RETURN.

### 6.6 Audio Resource Management

Audio files are stored in `assets/audio`. `AudioManager` loads music and sound effects once, reuses them, and disposes them when the game exits. Background music uses libGDX `Music`, and short effects use libGDX `Sound`.

---

## 7. Diagrams

The project includes three required diagrams in the `docs/` folder:

| Diagram | File | Description |
| --- | --- | --- |
| UML Class Diagram | `docs/class-diagram.png` | Main classes and relationships. |
| Game Flow Diagram | `docs/game-flow-diagram.png` | Screens and transitions. |
| Level 1 Sketch | `docs/level-1-sketch.png` | Level 1 layout with spawn, key, door, and exit. |

---

## 8. Testing

### 8.1 Test Environment

| Item | Description |
| --- | --- |
| Platform | Desktop PC |
| Build Command | `.\gradlew.bat build` |
| Run Command | `.\gradlew.bat lwjgl3:run` |
| Test Type | Manual playthrough and feature checklist |

### 8.2 Final Test Checklist

| Test | Expected Result | Status |
| --- | --- | --- |
| Main Menu Play | Starts Level 1 | Pass |
| Music Toggle | `M` toggles music | Pass |
| Movement | WASD and arrows move player | Pass |
| Wall Collision | Player cannot pass through walls | Pass |
| Level 1 Key | Key can be collected | Pass |
| Wooden Door | Opens only with selected Key | Pass |
| Portal Transition | Portal opens Level Complete overlay / next level | Pass |
| Level 2 Patrols | Enemies patrol correctly | Pass |
| Enemy Detection | Enemy chases visible player | Pass |
| Enemy Line of Sight | Walls block enemy vision | Pass |
| Enemy Search / Return | Enemy stops chasing after losing player | Pass |
| Medkit | Restores HP when damaged | Pass |
| Disguise | Temporarily prevents detection | Pass |
| Knife Selection | `3` selects Knife | Pass |
| Stealth Kill Success | Enemy dies when player attacks from behind in time | Pass |
| Stealth Kill Fail | Enemy is not killed from front / after reaction | Pass |
| Iron Door | Opens only with selected Crowbar | Pass |
| Level 4 Full Route | No random death; mechanics work together | Pass |
| Game Over | HP 0 opens Game Over screen | Pass |
| Win Screen | Final portal opens Win Screen | Pass |
| Pause | Escape pauses and overlay buttons work | Pass |
| Build | Gradle build completes successfully | Pass |

### 8.3 Known Issues

No critical gameplay blockers were found during the final manual test. Minor balance changes may still be made if additional playtesting shows that a patrol route is too easy or too difficult.

---

## 9. Deployment and Submission

### 9.1 Build Checklist

| Task | Status |
| --- | --- |
| Project builds successfully | Done |
| Game can run from Gradle | Done |
| All 4 levels playable | Done |
| Diagrams prepared | Done |
| README prepared for review | In review |
| GDD prepared for review | Done |
| Final GitHub merge to master | To do |
| Final release/tag | To do |

### 9.2 Submission Package

The final submission package should include:

- GitHub repository link;
- final GDD exported to PDF;
- diagrams from `docs/`;
- README with controls and run instructions;
- runnable build or Gradle run instructions;
- confirmation that the game was tested from Main Menu to Win Screen.

---

## 10. Conclusion

Maze Runner meets the core goals of the project: it has a complete gameplay loop, multiple levels, enemies, stealth mechanics, item-based progression, win/lose states, audio, UI, documentation diagrams, and multiple design patterns implemented in code. The game demonstrates both game development fundamentals and object-oriented design patterns in a working libGDX project.
