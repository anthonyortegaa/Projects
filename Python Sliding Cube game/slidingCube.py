import pygame
import sys
import random
from collections import deque

width, height = 400, 400 
grid_size = 3  # Changes grid sizing
cube_size = width // grid_size

def load_and_split_image(image_path):
    image = pygame.image.load(image_path)
    image = pygame.transform.scale(image, (width, width))
    cubes = []
    for row in range(grid_size):
        for col in range(grid_size):
            rect = pygame.Rect(col * cube_size, row * cube_size, cube_size, cube_size)
            cube = image.subsurface(rect)
            cubes.append(cube)
    return cubes

def draw_puzzle(screen, cubes, empty_position):
    for i, cube in enumerate(cubes):
        row, col = divmod(i, grid_size)
        x, y = col * cube_size, row * cube_size
        if i != empty_position:
            screen.blit(cube, (x, y))

def shuffle_puzzle(cubes, empty_position):
    valid_moves = []
    row, col = divmod(empty_position, grid_size)

    if row > 0:
        valid_moves.append(empty_position - grid_size)  # move up
    if row < grid_size - 1:
        valid_moves.append(empty_position + grid_size)  # move down
    if col > 0:
        valid_moves.append(empty_position - 1)          # move left
    if col < grid_size - 1:
        valid_moves.append(empty_position + 1)          # move right

    new_position = random.choice(valid_moves)
    cubes[empty_position], cubes[new_position] = cubes[new_position], cubes[empty_position]
    return new_position

def main():
    pygame.init()
    screen = pygame.display.set_mode((width, height))
    pygame.display.set_caption("Sliding Cube Puzzle")

    image_path = r"Python Sliding Cube game\IMG_8916.jpg"  # paste image path here (Has to be jpg)
    cubes = load_and_split_image(image_path)
    empty_position = len(cubes) - 1

    for _ in range(1000):
        empty_position = shuffle_puzzle(cubes, empty_position)

    clock = pygame.time.Clock()

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_w and empty_position - grid_size >= 0:
                    cubes[empty_position], cubes[empty_position - grid_size] = cubes[empty_position - grid_size], cubes[empty_position]
                    empty_position -= grid_size
                elif event.key == pygame.K_s and empty_position + grid_size < grid_size * grid_size:
                    cubes[empty_position], cubes[empty_position + grid_size] = cubes[empty_position + grid_size], cubes[empty_position]
                    empty_position += grid_size
                elif event.key == pygame.K_a and empty_position % grid_size != 0:
                    cubes[empty_position], cubes[empty_position - 1] = cubes[empty_position - 1], cubes[empty_position]
                    empty_position -= 1
                elif event.key == pygame.K_d and (empty_position + 1) % grid_size != 0:
                    cubes[empty_position], cubes[empty_position + 1] = cubes[empty_position + 1], cubes[empty_position]
                    empty_position += 1

            if event.type == pygame.MOUSEBUTTONDOWN:
                mouse_x, mouse_y = event.pos
                col, row = mouse_x // cube_size, mouse_y // cube_size
                clicked_position = row * grid_size + col
                
                if clicked_position in [
                    empty_position - grid_size,  # move up
                    empty_position + grid_size,  # move down
                    empty_position - 1 if empty_position % grid_size != 0 else -1,  # move left
                    empty_position + 1 if (empty_position + 1) % grid_size != 0 else -1  # move right
                ]:
                    cubes[empty_position], cubes[clicked_position] = cubes[clicked_position], cubes[empty_position]
                    empty_position = clicked_position

        screen.fill("black")
        draw_puzzle(screen, cubes, empty_position)
        pygame.display.flip()
        clock.tick(60)

if __name__ == "__main__":
    main()
