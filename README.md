<img src="assets/icon.png" alt="SoundCircle" width="96" align="right"/>

# SoundCircle

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/S42yt/SoundCircle/release.yml?style=for-the-badge) ![GitHub release](https://img.shields.io/github/v/release/S42yt/SoundCircle?style=for-the-badge) ![Minecraft](https://img.shields.io/badge/minecraft-1.21.11%20--%2026.3-5da545?style=for-the-badge) ![Fabric](https://img.shields.io/badge/loader-fabric-dbd0b4?style=for-the-badge)

### A Fabric mod that shows nearby sounds as a circle around your crosshair, made for accessibility. Inspired by Fortnite's *Visualize Sound Effects*.

<hr>

# Download

Download the jar for your Minecraft version from the [latest release](https://github.com/S42yt/SoundCircle/releases/latest).

| Minecraft | Jar                            |
|-----------|--------------------------------|
| 1.21.11   | `soundcircle-<version>+1.21.11.jar` |
| 26.1.x    | `soundcircle-<version>+26.1.jar`    |
| 26.2      | `soundcircle-<version>+26.2.jar`    |
| 26.3      | `soundcircle-<version>+26.3.jar`    |

**Requires:** [Fabric API](https://modrinth.com/mod/fabric-api) and [YetAnotherConfigLib](https://modrinth.com/mod/yacl).
[Mod Menu](https://modrinth.com/mod/modmenu) is optional and adds a settings button.

# Features

Every sound around you shows up as a pixel arc on the circle, pointing where it came from. Closer sounds are brighter and wider, and they fade out after a moment.

- **Mob faces**: sounds made by mobs show the face of that mob, in its real variant (cold cow, black wolf, calico cat…). Mobs without a known texture get their head rendered live, so modded mobs work too. Players show their skin and name.
- **Items for objects**: minecarts, boats, TNT, arrows and dropped items show their item.
- **Action badges**: a small icon tells you what happened, like eating, getting hurt, a slime hopping or an arrow being shot.
- **Danger first**: hissing creepers, lit TNT, explosions, ghast and warden attacks are red, pulse and are never pushed out by quieter sounds.
- **Block colors**: block sounds are tinted in the color of the block that was broken or placed.
- **Height arrows**: an arrow shows if a sound is above or below you.
- **Ignores you**: your own footsteps, attacks, mining and projectiles are filtered out.
- **Ignores noise**: rain, cave ambience, flowing water, lava and fire can be filtered out.
- **Works while muted**: sounds are shown even if your game volume is off.

<img src="assets/icons.png" alt="icons" width="600"/>

### 🎨 The circle is drawn by a custom core shader that snaps to the GUI pixel grid, so it stays crisp at every GUI scale.

# Config

Open the settings through Mod Menu or bind a key under *Controls → SoundCircle*. There is also a key to quickly toggle the circle.

| Option             | Default | Description                                         |
|--------------------|---------|-----------------------------------------------------|
| Circle Radius      | 72 px   | Size of the circle around the crosshair             |
| Icon Size          | 1x      | Pixel scale of the icons                            |
| Opacity            | 85%     | Lower it to make the circle less distracting        |
| Show Mob Faces     | on      | Mob and player faces instead of generic icons       |
| Show Player Names  | on      | Name below a player's face                          |
| Show Guide Ring    | off     | Thin ring while sounds are shown                    |
| Show Sound Names   | off     | Subtitle text below each icon                       |
| Show Distance      | off     | Distance in blocks below each icon                  |
| Show Height Arrows | on      | Arrow for sounds above or below you                 |
| Pulse Danger       | on      | Dangerous sounds pulse                              |
| Range              | 16 m    | Sounds farther away are ignored                     |
| Display Time       | 2.5 s   | How long a sound stays on the circle                |
| Max Indicators     | 10      | Most sounds shown at once                           |
| Show Own Sounds    | off     | Also show sounds made by yourself                   |
| Ignore Ambient     | on      | Skip rain, caves, water, lava, fire and dripping    |

Every sound type (danger, hostile mobs, combat, players, footsteps, animals, blocks, environment, music) can be turned off and recolored on its own.

The config is saved in `config/soundcircle.json5`.

# Building

The project uses [Stonecutter](https://stonecutter.kikugie.dev/) to build every Minecraft version from one source tree. You need Java 25.

```sh
./gradlew build
```

The jars end up in `versions/<minecraft>/build/libs/`. Run a client with `./gradlew :26.3:runClient`.

# Contributing 🩷

Feel free to open an issue or a pull request, I will be happy to help you!
