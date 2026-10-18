# Player Hider — Fabric 1.21.4

Client-side Minecraft mod.

## Features
- Press H to toggle hiding other player models.
- When you attack another player, that player remains visible for 5 seconds while hiding is enabled.
- Change the key in Options → Controls → Key Binds → Miscellaneous.
- Client-side rendering only; this does not change server hitboxes, reach, or combat rules.

## Build
Requires Java 21 and Gradle 8.10.2. Run: gradle build

The mod JAR will be in build/libs/. Install it with Fabric Loader and Fabric API for Minecraft 1.21.4.

GitHub Actions builds the project on pushes and pull requests. Download the JAR from the workflow Artifacts after a successful run.

## Status
The source has not yet been verified in a running Minecraft client. Check the Actions build result before using it.
