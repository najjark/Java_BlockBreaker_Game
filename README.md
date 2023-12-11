### Name: Khalid Al Najjar
### Student ID: 20490270

# Game Compilation and Execution Instructions

## Using IntelliJ

1. Start IntelliJ and open the project.
2. Go to the `brickGame` package.
3. Run the code from the main file.

**Note:** Java and JavaFX must be installed for the program to run.

# Implemented Features

1. Press S to save the current game.
2. Press R to restart the game.
3. Added a power-up that makes the ball larger for a short time.
4. Gold ball power-up allows it to go through blocks and destroy them.
5. Added a power-up which makes the paddle smaller for a short time.
6. Press P to pause the game.

# Features Not Working Properly

1. Gold ball awards 2 points for every block destroyed instead of 1.

# New Java Classes Introduced

1. [CreateBoard](src/main/java/brickGame/CreateBoard.java): Used to create blocks in the game.
2. [CreateBall](src/main/java/brickGame/CreateBall.java): Used to create the ball in the game.
3. [Paddle](src/main/java/brickGame/Paddle.java): Used to create the paddle in the game.
4. [LoadGame](src/main/java/brickGame/LoadGame.java): Used to load in the saved game state.
5. [PhysicsEngine](src/main/java/brickGame/PhysicsEngine.java): Used to control the speed of the ball and apply power-ups.
6. [RestartGame](src/main/java/brickGame/RestartGame.java): Used to restart the game.
7. [SaveGame](src/main/java/brickGame/SaveGame.java): Used to save the game.
8. [GameState](src/main/java/brickGame/GameState.java): Used to initialize game variables and their initial values.

# Java Classes Modified

1. Main: Moved some methods out of main to try to make each class have a single responsibility.
2. Block: Adjusted collision physics to make the ball react appropriately with block collisions.
3. Score: Adjusted class to properly print labels in the game, for example when lives are deducted or a bonus is picked up.

# Features Not Implemented and Reasons

1. Special blocks that cannot be destroyed by the ball, as the ball was not detecting that the block existed.
2. Sound effects for certain events in the game.

# Unexpected Problems

1. Issues included moving methods around to other classes.
2. Fixing the collision detection between the ball and blocks.

