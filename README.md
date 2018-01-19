# Gradius
A simple Gradius game (Java Version)
by Steven Chen 	(C)2017
Langara College, CPSC 1181-003 Fall 2017, Instructor: Jeremy Hilliker.
--------------------------------------------------------------------------------------------------------
			INTRODUCTION
--------------------------------------------------------------------------------------------------------
Gradius game is a game desgin with a ship flys around the space
and try to avoid the asteroids or destroy them by shooting the cannons.
--------------------------------------------------------------------------------------------------------
			INSTRUCTION
--------------------------------------------------------------------------------------------------------
This game starts at time 0, a player has 5 lives and energy 0.
This game will start at level 1 and every 30 secs the level will goes up.
The ship can move up, down, left and right controlled by the keyboard.
The asteroids will spawn on the right side of the screen.
If the ship hits an asteroid, lives will decrease by 1.
If lives are lower and equal to 2, the lives(display as shape) will change color to red.
And, if the player losts the game, a "Game Over" message will display.
Player can choose to press F12 to restart the game, all status will resets.
--------------------------------------------------------------------------------------------------------
			CONTROLS
--------------------------------------------------------------------------------------------------------
//Control the ship
up arrow key / w key			-Move the ship up
down arrow key / s key		-Move the ship down
left arrow key / a key			-Move the ship left
right arrow key / d key		-Move the ship right
shift key while pressed and hold	-Move ship 2 times faster

//Cannons
//All cannons gain 2 energy if hits the asteroids
z key				-Shoots a single cannon(no energy needed)
x key				-Shoots cannon 360 degree around the ship(need 25+ energy and energy will be 0 after used)
c key				-Shoots cannon 90 degree(arc shape)(need at least 1 energy and energy will decrease 1 after used)

//Game
F12				-Restarts the game
