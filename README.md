## Breakout Clone - Project Readme
### A Bit of Nostalgia
  Breakout is one of the classic arcade games that helped define early video gaming. 
  Its simple premise—bounce a ball with a paddle to clear bricks—made it one of the 
  most iconic skill‑based games of the 70s and 80s. This project recreates that retro
  feeling while adding a few modern conveniences.
 
### Development Timeline
  This project was developed over multiple sessions spanning several days. The total
  active development time worked out to roughly several hours of focused work:
  implementing rendering, physics, input handling, shaders, sound, and gameplay logic.
  Each feature was built iteratively, one piece at a time, collaboration with AI, to avoid unnecessary
  regeneration of existing logic.
 
### Key Technical Components
 - **Shader‑based Rendering:** All visuals are handled through OpenGL shaders rather than immediate mode rendering.
 - **Collision Logic:** Ball‑to‑paddle, ball‑to‑brick, and wall collision helpers keep the gameplay consistent and predictable.
 - **Level System:** The game tracks player progress and loads the next brick layout automatically when a level is cleared.
 - **Paddle Size Changes:** Power‑ups (or penalties) can shrink or expand the paddle to increase challenge.
 - **Sound Engine:** Sound effects for collisions and events give the game more satisfying feedback.
 - **Score Tracking:** Each brick hit increments the score.
 
### How to Play
 - Move the paddle left and right using the keyboard.
 - Press **Spacebar** to release the ball at the beginning of the round.
 - Keep the ball in play and clear all the bricks to advance to the next level.
 - Avoid letting the ball drop below the paddle—it's game over.
 
### Toggling Fullscreen
 Fullscreen mode can be toggled using the standard GLFW approach:
 - Press ALT + ENTER to toggle 

Enjoy the game, and feel free to extend it further! 
