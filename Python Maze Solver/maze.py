import sys
import pygame
import time

class Node:
    def __init__(self, state, parent, action):
        self.state = state
        self.parent = parent
        self.action = action

class DFSFrontier:
    def __init__(self):
        self.frontier = []

    def add(self, node):
        self.frontier.append(node)

    def containsState(self, state):
        return any(node.state == state for node in self.frontier)

    def isEmpty(self):
        return len(self.frontier) == 0

    def remove(self):
        if self.isEmpty():
            raise Exception("empty frontier")
        else:
            node = self.frontier[-1]
            self.frontier = self.frontier[:-1]
            return node

class BFSFrontier(DFSFrontier):
    def remove(self):
        if self.isEmpty():
            raise Exception("empty frontier")
        else:
            node = self.frontier[0]
            self.frontier = self.frontier[1:]
            return node

class Maze:
    def __init__(self, filename):
        with open(filename) as file:
            contents = file.read()

        if contents.count("A") != 1:
            raise Exception("maze must have exactly one starting position")
        if contents.count("B") != 1:
            raise Exception("maze must have exactly one final position")

        contents = contents.splitlines()
        self.height = len(contents)
        self.width = max(len(line) for line in contents)

        self.walls = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                try:
                    if contents[i][j] == "A":
                        self.start = (i, j)
                        row.append(False)
                    elif contents[i][j] == "B":
                        self.finish = (i, j)
                        row.append(False)
                    elif contents[i][j] == " ":
                        row.append(False)
                    else:
                        row.append(True)
                except IndexError:
                    row.append(False)
            self.walls.append(row)
        self.solution = None

    def neighbors(self, state):
        row, col = state
        candidates = [
            ("up", (row - 1, col)),
            ("down", (row + 1, col)),
            ("left", (row, col - 1)),
            ("right", (row, col + 1))
        ]

        result = []
        for action, (r, c) in candidates:
            if 0 <= r < self.height and 0 <= c < self.width and not self.walls[r][c]:
                result.append((action, (r, c)))
        return result

    def solve(self, frontier_type, screen, cell_size):
        self.num_explored = 0
        start = Node(state=self.start, parent=None, action=None)

        if frontier_type == 'DFS':
            frontier = DFSFrontier()
        elif frontier_type == 'BFS':
            frontier = BFSFrontier()
        else:
            raise ValueError("Invalid frontier type")

        frontier.add(start)
        self.explored = set()

        while True:
            if frontier.isEmpty():
                raise Exception("no solution")

            node = frontier.remove()
            self.num_explored += 1

            if node.state == self.finish:
                actions = []
                cells = []
                while node.parent is not None:
                    actions.append(node.action)
                    cells.append(node.state)
                    node = node.parent
                actions.reverse()
                cells.reverse()
                self.solution = (actions, cells)
                return

            self.explored.add(node.state)

            self.draw(screen, cell_size)

            for action, state in self.neighbors(node.state):
                if not frontier.containsState(state) and state not in self.explored:
                    child = Node(state=state, parent=node, action=action)
                    frontier.add(child)

            # Slow down visualization for better visibility
            pygame.display.flip()
            time.sleep(.1) # make this 0 to go brrrrrr

    def draw(self, screen, cell_size):
        screen.fill((0, 0, 0)) 

        x_offset = (screen.get_width() - self.width * cell_size) // 2
        y_offset = (screen.get_height() - 50 - self.height * cell_size) // 2

        for i, row in enumerate(self.walls):
            for j, col in enumerate(row):
                color = (255, 255, 255)  # default color (white)
                if col:
                    color = (0, 0, 0)  # wall (black)
                elif (i, j) == self.start:
                    color = (0, 255, 0)  # start (green)
                elif (i, j) == self.finish:
                    color = (255, 0, 0)  # finish (red)
                elif self.solution and (i, j) in self.solution[1]:
                    color = (0, 255, 0)  # solution path (green)
                elif (i, j) in self.explored:
                    color = (255, 0, 0)  # explored (red)

                pygame.draw.rect(screen, color, (x_offset + j * cell_size, y_offset + i * cell_size, cell_size, cell_size))

        # Draw 'S' for start and 'E' for finish
        font = pygame.font.Font(None, 36)
        start_text = font.render('S', True, (0, 0, 0))
        finish_text = font.render('E', True, (0, 0, 0))
        screen.blit(start_text, (x_offset + self.start[1] * cell_size + (cell_size - start_text.get_width()) // 2,
                                  y_offset + self.start[0] * cell_size + (cell_size - start_text.get_height()) // 2))
        screen.blit(finish_text, (x_offset + self.finish[1] * cell_size + (cell_size - finish_text.get_width()) // 2,
                                   y_offset + self.finish[0] * cell_size + (cell_size - finish_text.get_height()) // 2))

def draw_text(screen, maze):
    font = pygame.font.Font(None, 30)

    path_length = len(maze.solution[1]) if maze.solution else 0
    text_content = f"Path Length: {path_length}, States Explored: {maze.num_explored}"

    text_surface = font.render(text_content, True, (255, 255, 255))

    text_position = (screen.get_width() // 2 - text_surface.get_width() // 2, screen.get_height() - 40)

    screen.blit(text_surface, text_position)

def draw_button(screen, text, position, size, highlighted=False):
    font = pygame.font.Font(None, 36)
    button_color = (0, 0, 255) if highlighted else (100, 100, 100)
    pygame.draw.rect(screen, button_color, (position[0], position[1], size[0], size[1]))
    text_surface = font.render(text, True, (255, 255, 255))
    text_rect = text_surface.get_rect(center=(position[0] + size[0] // 2, position[1] + size[1] // 2))
    screen.blit(text_surface, text_rect)

def main():
    pygame.init()
    cell_size = 20
    maze_files = {
        '1': r'Python Maze Solver\Text Maze\maze_1.txt',
        '2': r'Python Maze Solver\Text Maze\maze_2.txt',
        '3': r'Python Maze Solver\Text Maze\maze_3.txt',
        '4': r'Python Maze Solver\Text Maze\maze_4.txt',
        '5': r'Python Maze Solver\Text Maze\maze_5.txt',
        '6': r'Python Maze Solver\Text Maze\maze_6.txt'
    }

    window_width = 400
    window_height = 400
    screen = pygame.display.set_mode((window_width, window_height))
    pygame.display.set_caption('Maze Solver Input')

    selected_maze = None
    frontier_type = None
    input_active = True
    run_button_highlighted = False

    while input_active:
        screen.fill((0, 0, 0)) 

        button_width = 140
        button_height = 50

        x_offset_col1 = (window_width // 4) - (button_width // 2)
        x_offset_col2 = (3 * window_width // 4) - (button_width // 2)

        y_offset = 50
        y_gap = 60

        # Draw maze buttons in two columns
        for i in range(3):
            draw_button(screen, f'Maze {i + 1}', 
                        (x_offset_col1, y_offset + i * y_gap), 
                        (button_width, button_height), highlighted=(selected_maze == str(i + 1)))
            draw_button(screen, f'Maze {i + 4}', 
                        (x_offset_col2, y_offset + i * y_gap), 
                        (button_width, button_height), highlighted=(selected_maze == str(i + 4)))

        # Draw frontier type buttons
        draw_button(screen, 'DFS', 
                    (x_offset_col1, y_offset + 3 * y_gap), 
                    (button_width, button_height), highlighted=(frontier_type == 'DFS'))
        draw_button(screen, 'BFS', 
                    (x_offset_col2, y_offset + 3 * y_gap), 
                    (button_width, button_height), highlighted=(frontier_type == 'BFS'))

        # Draw the Run button
        draw_button(screen, 'Run', 
                    ((window_width - button_width) // 2, y_offset + 4 * y_gap), 
                    (button_width, button_height), highlighted=run_button_highlighted)

        pygame.display.flip()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == pygame.MOUSEBUTTONDOWN:
                mouse_pos = pygame.mouse.get_pos()

                for i in range(3):
                    if x_offset_col1 <= mouse_pos[0] <= x_offset_col1 + button_width and y_offset + i * y_gap <= mouse_pos[1] <= y_offset + i * y_gap + button_height:
                        selected_maze = str(i + 1)
                    elif x_offset_col2 <= mouse_pos[0] <= x_offset_col2 + button_width and y_offset + i * y_gap <= mouse_pos[1] <= y_offset + i * y_gap + button_height:
                        selected_maze = str(i + 4)

                if x_offset_col1 <= mouse_pos[0] <= x_offset_col1 + button_width and y_offset + 3 * y_gap <= mouse_pos[1] <= y_offset + 3 * y_gap + button_height:
                    frontier_type = 'DFS'
                elif x_offset_col2 <= mouse_pos[0] <= x_offset_col2 + button_width and y_offset + 3 * y_gap <= mouse_pos[1] <= y_offset + 3 * y_gap + button_height:
                    frontier_type = 'BFS'

                if (window_width - button_width) // 2 <= mouse_pos[0] <= (window_width - button_width) // 2 + button_width and y_offset + 4 * y_gap <= mouse_pos[1] <= y_offset + 4 * y_gap + button_height:
                    run_button_highlighted = True

            elif event.type == pygame.MOUSEBUTTONUP:
                if run_button_highlighted:
                    run_button_highlighted = False
                    if selected_maze is not None and frontier_type is not None:
                        input_active = False

    # Load the selected maze
    maze_file = maze_files[selected_maze]
    maze = Maze(maze_file)

    min_window_width = 600 
    window_width = max(maze.width * cell_size, min_window_width)
    window_height = maze.height * cell_size + 50 
    screen = pygame.display.set_mode((window_width, window_height))

    maze.solve(frontier_type, screen, cell_size)

    # Main loop for visualizing the solution
    while True:
        maze.draw(screen, cell_size)
        draw_text(screen, maze)
        pygame.display.flip()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

if __name__ == "__main__":
    main()