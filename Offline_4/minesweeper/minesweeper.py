import itertools
import random


class Minesweeper():
    """
    Minesweeper game representation
    """

    def __init__(self, height=8, width=8, mines=8):

        # Set initial width, height, and number of mines
        self.height = height
        self.width = width
        self.mines = set()

        # Initialize an empty field with no mines
        self.board = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                row.append(False)
            self.board.append(row)

        # Add mines randomly
        while len(self.mines) != mines:
            i = random.randrange(height)
            j = random.randrange(width)
            if not self.board[i][j]:
                self.mines.add((i, j))
                self.board[i][j] = True

        # At first, player has found no mines
        self.mines_found = set()

    def print(self):
        """
        Prints a text-based representation
        of where mines are located.
        """
        for i in range(self.height):
            print("--" * self.width + "-")
            for j in range(self.width):
                if self.board[i][j]:
                    print("|X", end="")
                else:
                    print("| ", end="")
            print("|")
        print("--" * self.width + "-")

    def is_mine(self, cell):
        i, j = cell
        return self.board[i][j]

    def nearby_mines(self, cell):
        """
        Returns the number of mines that are
        within one row and column of a given cell,
        not including the cell itself.
        """

        # Keep count of nearby mines
        count = 0

        # Unpack cell
        i, j = cell
        # only consider cells that are not diagonal to the cell 
        # only i-1, i+1, j-1, j+1 are considered
        # for j
        if j == 0:
            # check only the cells to the right
            if self.board[i][j+1]:
                count += 1
        elif j == self.width - 1:
            # check only the cells to the left
            if self.board[i][j-1]:
                count += 1
        else:
            # check the cells to the left and right
            if self.board[i][j-1]:
                count += 1
            if self.board[i][j+1]:
                count += 1
        
        # for i
        if i == 0:
            # check only the cells below
            if self.board[i+1][j]:
                count += 1
        elif i == self.height - 1:
            # check only the cells above
            if self.board[i-1][j]:
                count += 1
        else:
            # check the cells above and below
            if self.board[i-1][j]:
                count += 1
            if self.board[i+1][j]:
                count += 1

        # Loop over all cells within one row and column
        # for i in range(cell[0] - 1, cell[0] + 2):
        #     for j in range(cell[1] - 1, cell[1] + 2):

        #         # Ignore the cell itself
        #         if (i, j) == cell:
        #             continue

        #         # Update count if cell in bounds and is mine
        #         if 0 <= i < self.height and 0 <= j < self.width:
        #             if self.board[i][j]:
        #                 count += 1

        return count

    def won(self):
        """
        Checks if all mines have been flagged.
        """
        return self.mines_found == self.mines


class Sentence():
    """
    Logical statement about a Minesweeper game
    A sentence consists of a set of board cells,
    and a count of the number of those cells which are mines.
    """

    def __init__(self, cells, count):
        self.cells = set(cells)         # set of board cells
        self.count = count              # number of mines among those cells

    def __eq__(self, other):
        return self.cells == other.cells and self.count == other.count

    def __str__(self):
        return f"{self.cells} = {self.count}"

    def known_mines(self):
        """
        Returns the set of all cells in self.cells known to be mines.
        """
        # if we can deduce that all the cells in a sentence are mines, 
        # then we know that all the cells in that sentence are mines
        # else we return an empty set
        if len(self.cells) == self.count:
            return self.cells
        else:
            return None

    def known_safes(self):
        """
        Returns the set of all cells in self.cells known to be safe.
        """
        # if we can deduce that no cells in a sentence are mines,
        # then we know that all the cells in that sentence are safe
        # else we return an empty set
        if self.count == 0:
            return self.cells
        else:
            return None

    def mark_mine(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be a mine.
        """
        # create a new sentence with the cell removed and the count decremented
        newCells = set()
        for c in self.cells:
            if c != cell:
                newCells.add(c)         # this runs for len(self.cells)-1 times
            else:
                self.count -= 1         # this runs for 1 times
        
        self.cells = newCells

    def mark_safe(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be safe.
        """
        # create a new sentence with the cell removed
        newCells = set()
        for c in self.cells:
            if c != cell:
                newCells.add(c)         # this runs for len(self.cells)-1 times
        self.cells = newCells


class MinesweeperAI():
    """
    Minesweeper game player
    """

    def __init__(self, height=8, width=8):

        # Set initial height and width
        self.height = height
        self.width = width

        # Keep track of which cells have been clicked on
        self.moves_made = set()

        # Keep track of cells known to be safe or mines
        self.mines = set()
        self.safes = set()

        # List of sentences about the game known to be true
        self.knowledge = []

    def mark_mine(self, cell):
        """
        Marks a cell as a mine, and updates all knowledge
        to mark that cell as a mine as well.
        """
        self.mines.add(cell)
        for sentence in self.knowledge:
            sentence.mark_mine(cell)

    def mark_safe(self, cell):
        """
        Marks a cell as safe, and updates all knowledge
        to mark that cell as safe as well.
        """
        self.safes.add(cell)
        for sentence in self.knowledge:
            sentence.mark_safe(cell)

    def add_knowledge(self, cell, count):
        """
        Called when the Minesweeper board tells us, for a given
        safe cell, how many neighboring cells have mines in them.

        This function should:
            1) mark the cell as a move that has been made
            2) mark the cell as safe
            3) add a new sentence to the AI's knowledge base
               based on the value of `cell` and `count`
            4) mark any additional cells as safe or as mines
               if it can be concluded based on the AI's knowledge base
            5) add any new sentences to the AI's knowledge base
               if they can be inferred from existing knowledge
        """
        # print existing knowledge
        # print("Existing knowledge:")
        # for k in self.knowledge:
        #     print("e" , k)

        # mark the cell as a safe and a move that has been made
        self.mark_safe(cell)
        self.moves_made.add(cell)

        # get the neighboring cells
        neighbors , count = self.get_neighbours(cell, count)
        # print(neighbors, count)


        # add a new sentence to the AI's knowledge base
        # the new sentence will have the neighboring cells and the count of mines in them
        # as we have already marked the cell as safe the number of mines are same as the count
        sentence = Sentence(neighbors, count)
        if len (sentence.cells) != 0:
            self.knowledge.append(sentence)


        new_knowledge = []
        for k in self.knowledge:
            if k == sentence:
                continue
            elif sentence.cells.issuperset(k.cells):
                # knowledge is a subset of sentence
                
                # flag to check if we need to update the knowledge
                flag = True

                # if the count of mine in knowledge is same as the count of mine in sentence
                # then all the cells that are not in knowledge but in sentence are safe
                set_difference = sentence.cells - k.cells
                if k.count == sentence.count:
                    flag = False
                    print("some cells are safe " + str(set_difference))
                    for sure_safe in set_difference:
                        self.mark_safe(sure_safe)

                # if the length of the set difference is same as the difference in the count
                # then all the cells that are not in knowledge but in sentence are mines
                if len(set_difference) == sentence.count - k.count:
                    flag = False
                    print("some cells are mines " + str(set_difference))
                    for sure_mine in set_difference:
                        self.mark_mine(sure_mine)
                

                if flag:
                    # lastly we need to update the knowledge 
                    # add a new sentence to the AI's knowledge base
                    # in the set difference there are sentence.count - k.count mines
                    new_knowledge.append(Sentence(set_difference, sentence.count - k.count))
            elif k.cells.issuperset(sentence.cells):
                # sentence is a subset of knowledge


                # flag to check if we need to update the knowledge
                flag = True

                # sure safe cells (See the logic above)
                set_difference = k.cells - sentence.cells
                if k.count == sentence.count:
                    flag = False
                    print("sentence subset: some cells are safe " + str(set_difference))
                    for sure_safe in set_difference:
                        self.mark_safe(sure_safe)
                
                # sure mine cells
                if len(set_difference) == k.count - sentence.count:
                    flag = False
                    print("sentence subset: some cells are mines: " + str(set_difference))
                    for sure_mine in set_difference:
                        self.mark_mine(sure_mine)
                
                if flag:
                    # update the knowledge
                    new_knowledge.append(Sentence(set_difference, k.count - sentence.count))
                
            

        self.knowledge.extend(new_knowledge)

        # some duplicate sentences is added to the knowledge
        # so we need to remove the duplicate sentences
        unique_knowledge = []
        for k in self.knowledge:
            if k not in unique_knowledge:
                unique_knowledge.append(k)
        self.knowledge = unique_knowledge


        # remove the sures from the knowledge
        # if the sentence has no mines then it is not useful (known safe returns a set of cells)
        # if the sentence has no safe cells then it is not useful (known mines returns a set of cells)

        final_knowledge = []
        for k in self.knowledge:
            final_knowledge.append(k)
            
            if k.known_mines():
                for mine in k.known_mines():
                    self.mark_mine(mine)            # mark the mines in the sentence as mines
                                                    # handles the case when the sentence has no mines
                
                final_knowledge.pop()               # remove the sentence from the final knowledge
            
            if k.known_safes():
                for safe in k.known_safes():
                    self.mark_safe(safe)            # mark the safes in the sentence as safes
                                                    # handles the case when the sentence has no safe cells

                final_knowledge.pop()               # remove the sentence from the final knowledge
        

        self.knowledge = final_knowledge
        # print("length of safe: ", len(self.safes - self.moves_made))

        # print the final knowledge
        # print("Final knowledge:")
        # for k in self.knowledge:
        #     print("f", k)




    def make_safe_move(self):
        """
        Returns a safe cell to choose on the Minesweeper board.
        The move must be known to be safe, and not already a move
        that has been made.

        This function may use the knowledge in self.mines, self.safes
        and self.moves_made, but should not modify any of those values.
        """

        safe_moves = self.safes - self.moves_made

        if not safe_moves:
            return None
        
        # print("safe moves: ", safe_moves)
        return safe_moves.pop()

    def make_random_move(self):
        """
        Returns a move to make on the Minesweeper board.
        Should choose randomly among cells that:
            1) have not already been chosen, and
            2) are not known to be mines
        """
        moves = set()                   # set of possible moves
        for i in range(self.height):
            for j in range(self.width):
                if (i, j) not in self.moves_made and (i, j) not in self.mines:
                    moves.add((i, j))                       # if this cell is not clicked then add it to the set of possible moves
        #check len of moves
        if len(moves) == 0:
            # no possible moves
            return None
        # return a random move
        return random.choice(tuple(moves))

    def get_neighbours(self, cell, count):
        i, j = cell
        neighbours = set()

        for x in range(i-1, i+2):
            for y in range(j-1, j+2):
                # if the cell is in diagonal then skip
                # for becoming a diagonal the x and y both should be different than i and j
                if x != i and y != j:
                    continue

                if (x, y) != cell and 0 <= x < self.height and 0 <= y < self.width and (x, y) not in self.safes and (x, y) not in self.mines:
                    neighbours.add((x, y))
                if (x, y) in self.mines:
                    count -= 1
        

        return neighbours, count