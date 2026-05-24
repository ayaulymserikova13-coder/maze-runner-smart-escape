# Maze Runner

Maze Runner is a 2D top-down pixel-art stealth maze game built with **Java** and **libGDX**. The player explores four maze levels, collects items, opens locked doors, avoids enemy patrols, and escapes through the final portal.

The project was created for a Game Development / Software Design Patterns course and demonstrates multiple design patterns inside a working game.

## Team

| Member | Role |
| --- | --- |
| Ayaulym | Project lead, lead programmer, gameplay systems |
| Ulan | Programmer, level designer, testing support |

## Features

- 4 playable maze levels
- Player movement with wall and door collision
- Item-based progression
- Key, Crowbar, Knife, Medkit, and Disguise
- Wooden and iron locked doors
- Enemy patrol, detection, chase, search, return, and death states
- Stealth kill from behind with reaction timing
- HP system, damage, respawn, Game Over
- Main Menu, Pause, Level Complete, Game Over, and Win screens
- Pixel-style UI and inventory HUD
- Background music and sound effects
- Music toggle with `M`
- Project diagrams in `docs/`

## How To Run

Requirements:

- Java 17 or newer
- Desktop OS: Windows, macOS, or Linux

Run on Windows:

```bash
.\gradlew.bat lwjgl3:run
```

Run on macOS/Linux:

```bash
./gradlew lwjgl3:run
```

Build the project:

```bash
.\gradlew.bat build
```

## Controls

| Key / Input | Action |
| --- | --- |
| W / A / S / D | Move |
| Arrow Keys | Alternative movement |
| E | Pick up items, open doors, interact |
| F | Stealth kill with selected Knife |
| 1 | Select Key |
| 2 | Select Crowbar |
| 3 | Select Knife |
| 4 | Select Disguise |
| F4 | Quick-use Disguise |
| M | Toggle music |
| Escape | Pause / resume |
| Mouse Click | Press menu buttons |

## Gameplay

The player starts each run with 3 HP. If an enemy catches the player, HP decreases by 1 and the player respawns at the current level start. At 0 HP, the Game Over screen appears.

Items:

- **Key** opens wooden doors.
- **Crowbar** opens iron doors.
- **Knife** allows stealth kill from behind before an enemy reacts.
- **Medkit** restores HP.
- **Disguise** temporarily prevents enemy detection.

Enemies use line of sight and direction-based vision. If the player is seen, enemies chase and search around the last known position. Walls block enemy vision.

## Level Progression

| Level | Focus |
| --- | --- |
| Level 1 | Basic movement, Key, wooden door, portal |
| Level 2 | Enemy patrols, Medkit, Disguise |
| Level 3 | Crowbar, Knife, iron door, stealth kill |
| Level 4 | Final escape route with all mechanics combined |

## Design Patterns

| Pattern | Where It Is Used |
| --- | --- |
| Command | `ICommand`, `MoveCommand`, `InteractCommand`, `StealthKillCommand` |
| Factory | `ItemFactory` creates item objects |
| Decorator | `LockedDoor` wraps `BaseDoor` |
| Strategy | Enemy movement strategies |
| State | `EnemyState` controls enemy behaviour |
| Observer | Player HP notifies screen logic |
| Facade | `GameFacade` handles screen transitions |
| Adapter | `ILevelMap`, `ArrayLevelMapAdapter` |
| Singleton | `AudioManager` |

## Diagrams

The `docs/` folder contains the required project diagrams:

- `docs/class-diagram.png`
- `docs/game-flow-diagram.png`
- `docs/level-1-sketch.png`

## Project Structure

```text
assets/
  audio/              Music and sound effects
core/
  src/main/java/com/team/mazerunner/
    audio/            AudioManager
    enemies/          Enemy AI, states, strategies
    entities/         Player and HP observer
    input/            Command pattern and input handler
    items/            Items and ItemFactory
    screens/          Menu, game, win, game over, facade
    world/            LevelMap, doors, map adapter
docs/                 Diagrams
lwjgl3/               Desktop launcher
```

## Testing Status

Final manual testing was completed:

- all 4 levels are playable;
- movement and collision work;
- items and doors work;
- enemy patrol/chase/search works;
- stealth kill works from behind;
- Game Over and Win screens work;
- music and sound effects work;
- Gradle build passes.

## Notes

The current visual style uses shape-rendered pixel art instead of external sprite sheets. This keeps the project lightweight while still giving the game readable characters, items, doors, portals, walls, and UI.
