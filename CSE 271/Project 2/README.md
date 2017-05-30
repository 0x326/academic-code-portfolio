# CSE 271: Gameplay Simulator of Eight Queens Puzzle

## Specifications

- Create a Java GUI where the user can solve the [eight queens problem](https://en.wikipedia.org/wiki/Eight_queens_puzzle).
  - Start with a blank chess board.
  - Allow the user to fill the board with queen pieces such that no two queens can attack each other.
- The user can only place up to 8 queens.
- The user can press a button to check his solution. The solution is either complete, partially complete, or invalid.
- When there is less than 8 queens on the board, the user can press a tip button to see a suggestion that would place the next queen in a safe spot.
- When there is less than 8 queens on the board, the user can press a separate button for a smart tip. This suggestion leads the user in a path that is most probable to result in a complete solution.
- Provide a 3-minute presentation on:
  - Your design/programming decisions
  - Challenges in this project
  - What you learned
  - Any interesting features of your project

## Features

- Chess Board is customizable
  - Variable chess board size (10 X 10 is possible)
    - Solution checking, blind tip, and smart tip work for all sizes
  - Custom board colors
  - Custom queen image
- Chess Board is scalable
  - Queens can be resized to many times their size
