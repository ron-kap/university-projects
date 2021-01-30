# mdpAgents.py
# parsons/20-nov-2017
#
# Version 1
#
# The starting point for CW2.
#
# Intended to work with the PacMan AI projects from:
#
# http://ai.berkeley.edu/
#
# These use a simple API that allow us to control Pacman's interaction with
# the environment adding a layer on top of the AI Berkeley code.
#
# As required by the licensing agreement for the PacMan AI we have:
#
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).

# The agent here is was written by Simon Parsons, based on the code in
# pacmanAgents.py


from pacman import Directions
from game import Agent
import api
import random
import game
import util

##  -   NEW Import Statements - From Standard Library
import copy
import math
##  -   NEW Import Statements - From Standard Library


class Grid(Agent):
         
    # Constructor
    #
    # Note that it creates variables:
    #
    # grid:   an array that has one position for each element in the grid.
    # width:  the width of the grid
    # height: the height of the grid
    def __init__(self, width, height):                     ## FROM MapAgent
        self.width = width
        self.height = height
        subgrid = []
        for i in range(self.height):
            row=[]
            for j in range(self.width):
                row.append(0)
            subgrid.append(row)

        self.grid = subgrid
        self.oldGrid = copy.deepcopy(self.grid)             # Create a copy of Grid for MDP calculations
        
    # Set and get the values of specific elements in the grid.
    # Here x and y are indices.
    def setValue(self, x, y, value):                        ## FROM MapAgent
        self.grid[y][x] = value

    def getValue(self, x, y):                               ## FROM MapAgent
        return self.grid[y][x]

    # Return width and height to support functions that manipulate the
    # values stored in the grid.
    def getHeight(self):                                    ## FROM MapAgent
        return self.height

    def getWidth(self):                                     ## FROM MapAgent
        return self.width

    # Method to update the copy/clone of the Grid (re-clone)
    def updateClone(self):
        self.oldGrid = copy.deepcopy(self.grid)

    # Get the specific value of a Grid element
    def getOldValue(self, x, y):
        return self.oldGrid[y][x]




class MDPAgent(Agent):

    # Constructor: this gets run when we first invoke pacman.py
    def __init__(self):
        self.walls = []
        self.discount = 0.50
        self.foodR = 5
        self.noFoodR = -1
        self.ghostR = -10
        self.itr = 0

    # Gets run after an MDPAgent object is created and once there is
    # game state to access.
    def registerInitialState(self, state):                  ## FROM MapAgent
        self.makeMap(state)
        self.addWallsToMap(state)
        self.updateFoodInMap(state)
        self.updateGhostsInMap(state)
        
    # This is what gets run in between multiple games
    def final(self, state):
        self.makeMap(state)
        self.addWallsToMap(state)
        self.updateFoodInMap(state)
        self.updateGhostsInMap(state)
        self.itr = 0

    # Make a map by creating a grid of the right size
    def makeMap(self,state):                                ## FROM MapAgent
        self.walls = api.walls(state)
        corners = api.corners(state)
        height = api.corners(state)[3][1] + 1
        width  = api.corners(state)[1][0] + 1
        self.map = Grid(width, height)


    # Put every element in the list of wall elements into the map
    def addWallsToMap(self, state):                         ## FROM MapAgent
        for i in range(len(self.walls)):
            self.map.setValue(self.walls[i][0], self.walls[i][1], -1)


    # Create a map with a current picture of the food that exists.
    def updateFoodInMap(self, state):                       ## Revised FROM MapAgent
        food = api.food(state)
        for i in range(len(food)):
            self.map.setValue(food[i][0], food[i][1], self.foodR)


    # Create a map with a current picture of the ghost(s) that exists.
    def updateGhostsInMap(self, state):
        ghosts = api.ghostStates(state)
        food = api.food(state)
        for i in range(len(ghosts)):                        # For each ghost...
            if (ghosts[i][1] == 0):                         # If ghost is in 'ghost state' (hunting pacman)
                # Set position to ghost reward
                self.map.setValue(int(round(ghosts[i][0][0])),int(round(ghosts[i][0][1])), self.ghostR)
            else:                                           # Otherwise ghost is vulnerable
                if ((int(round(ghosts[i][0][0])),int(round(ghosts[i][0][1]))) in food):
                    # Set position to food reward
                    self.map.setValue(int(round(ghosts[i][0][0])),int(round(ghosts[i][0][1])), self.foodR)
                else:
                    # Set position to non-food reward
                    self.map.setValue(int(round(ghosts[i][0][0])),int(round(ghosts[i][0][1])), self.noFoodR)


    # Iterate through map to apply Bellman's value iteration, then choose a move based on MEU
    def getAction(self, state):

        # Internal method
        # 
        # uCalc(loc), Input: Location (x,y), Output: Probabilities of all four directions (List) -
        #           Checks all four directions (North,East,South,West) and records all their
        #           probabilites in a List.
        def uCalc(loc):

            # Pre-calculate four directions coordinates
            NORTH = (loc[0],loc[1] + 1)
            EAST = (loc[0] + 1,loc[1])
            SOUTH = (loc[0],loc[1] - 1)
            WEST = (loc[0] - 1,loc[1])

            # Initialise the List, that will be returned
            neswProbs = [-1,-1,-1,-1]                                                   # [North,East,South,West]

            # Conditional to check/calculate the North postion's probability value
            if NORTH not in self.walls:                                                      # Should be a legal move
                neswProbs[0] = 0.8 * self.map.getOldValue(NORTH[0], NORTH[1])           # MEU calculation, main North position (0.8)

                if EAST not in self.walls:
                    neswProbs[0] += 0.1 * self.map.getOldValue(EAST[0], EAST[1])        # MEU calculation, minor East position (0.1)
                else:
                    neswProbs[0] += 0.1 * self.map.getOldValue(loc[0], loc[1])          # MEU calculation, minor East position (0.1) - illegal move so bounce back from initial position

                if WEST not in self.walls:
                    neswProbs[0] += 0.1 * self.map.getOldValue(WEST[0], WEST[1])        # MEU calculation, minor West position (0.1)
                else:
                    neswProbs[0] += 0.1 * self.map.getOldValue(loc[0], loc[1])          # MEU calculation, minor West position (0.1) - illegal move so bounce back from initial position
            else:                                                                       
                neswProbs[0] = 0.8 * self.map.getOldValue(loc[0], loc[1])               # MEU calculation, main North position (0.8) - illegal move so bounce back from initial position

                if EAST not in self.walls:
                    neswProbs[0] += 0.1 * self.map.getOldValue(EAST[0], EAST[1])
                else:
                    neswProbs[0] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if WEST not in self.walls:
                    neswProbs[0] += 0.1 * self.map.getOldValue(WEST[0], WEST[1])
                else:
                    neswProbs[0] += 0.1 * self.map.getOldValue(loc[0], loc[1])

            # East, South, West follow the same structure as North
            if EAST not in self.walls:
                neswProbs[1] = 0.8 * self.map.getOldValue(EAST[0], EAST[1])

                if NORTH not in self.walls:
                    neswProbs[1] += 0.1 * self.map.getOldValue(NORTH[0], NORTH[1])
                else:
                    neswProbs[1] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if SOUTH not in self.walls:
                    neswProbs[1] += 0.1 * self.map.getOldValue(SOUTH[0], SOUTH[1])
                else:
                    neswProbs[1] += 0.1 * self.map.getOldValue(loc[0], loc[1])
            else:
                neswProbs[1] = 0.8 * self.map.getOldValue(loc[0], loc[1])

                if NORTH not in self.walls:
                    neswProbs[1] += 0.1 * self.map.getOldValue(NORTH[0], NORTH[1])
                else:
                    neswProbs[1] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if SOUTH not in self.walls:
                    neswProbs[1] += 0.1 * self.map.getOldValue(SOUTH[0], SOUTH[1])
                else:
                    neswProbs[1] += 0.1 * self.map.getOldValue(loc[0], loc[1])

            if SOUTH not in self.walls:
                neswProbs[2] = 0.8 * self.map.getOldValue(SOUTH[0], SOUTH[1])

                if EAST not in self.walls:
                    neswProbs[2] += 0.1 * self.map.getOldValue(EAST[0], EAST[1])
                else:
                    neswProbs[2] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if WEST not in self.walls:
                    neswProbs[2] += 0.1 * self.map.getOldValue(WEST[0], WEST[1])
                else:
                    neswProbs[2] += 0.1 * self.map.getOldValue(loc[0], loc[1])
            else:
                neswProbs[2] = 0.8 * self.map.getOldValue(loc[0], loc[1])

                if EAST not in self.walls:
                    neswProbs[2] += 0.1 * self.map.getOldValue(EAST[0], EAST[1])
                else:
                    neswProbs[2] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if WEST not in self.walls:
                    neswProbs[2] += 0.1 * self.map.getOldValue(WEST[0], WEST[1])
                else:
                    neswProbs[2] += 0.1 * self.map.getOldValue(loc[0], loc[1])

            if WEST not in self.walls:
                neswProbs[3] = 0.8 * self.map.getOldValue(WEST[0], WEST[1])

                if NORTH not in self.walls:
                    neswProbs[3] += 0.1 * self.map.getOldValue(NORTH[0], NORTH[1])
                else:
                    neswProbs[3] += 0.1 * self.map.getOldValue(loc[0], loc[1])
                    
                if SOUTH not in self.walls:
                    neswProbs[3] += 0.1 * self.map.getOldValue(SOUTH[0], SOUTH[1])
                else:
                    neswProbs[3] += 0.1 * self.map.getOldValue(loc[0], loc[1])
            else:
                neswProbs[3] = 0.8 * self.map.getOldValue(loc[0], loc[1])

                if NORTH not in self.walls:
                    neswProbs[3] += 0.1 * self.map.getOldValue(NORTH[0], NORTH[1])
                else:
                    neswProbs[3] += 0.1 * self.map.getOldValue(loc[0], loc[1])

                if SOUTH not in self.walls:
                    neswProbs[3] += 0.1 * self.map.getOldValue(SOUTH[0], SOUTH[1])
                else:
                    neswProbs[3] += 0.1 * self.map.getOldValue(loc[0], loc[1])

            return neswProbs

        # Store important game data
        food = api.food(state)
        pacLoc = api.whereAmI(state)
        ghosts = api.ghostStates(state)

        # Get the actions we can try, and remove "STOP" if that is one of them.
        legal = api.legalActions(state)
        if Directions.STOP in legal:
            legal.remove(Directions.STOP)

        # Store ALL Food and Ghost postions in Map
        self.updateFoodInMap(state)
        self.updateGhostsInMap(state)

        # Loop to calculate MDP values until no change*
        while (self.map.grid != self.map.oldGrid):
            # *Or terminate loop after -10- iterations (for efficiency)
            if (self.itr >= 12):
                self.itr = 100                  # Debug
                break
            # Increment counter
            self.itr += 1
            # Re-clone old Map copy, before future updates
            self.map.updateClone()
            ghosts = api.ghostStates(state)
            # Iterate through all elements in Grid
            for y in range(self.map.getHeight()):
                for x in range(self.map.getWidth()):
                    # MDP does NOT apply to walls, so ignore
                    if ((x,y) not in self.walls):
                        # Find this postion's 'neighbours'
                        neswProbs = uCalc((x,y))
                        # Store the highest value (MEU)   
                        highest = max(neswProbs)
                        if ((x,y) in food):
                            # Set position to food reward
                            self.map.setValue(x,y, round(self.foodR + (1 * neswProbs[neswProbs.index(highest)]), 2))               # Bellman update for food elements, rounded to 2dp
                            count = 1
                            for i in range(len(ghosts)):
                                if ((x,y) == ghosts[i][0]):
                                    # Set position to ghost reward                            # If this position is where a ghost is
                                    self.map.setValue(x,y, round(self.ghostR + (pow(self.discount, count) * neswProbs[neswProbs.index(highest)]), 2))
                                    count += 1 
                        else:
                            # Set position to non-food reward
                            self.map.setValue(x,y, round(self.noFoodR + (1 * neswProbs[neswProbs.index(highest)]), 2))              # Bellman update for empty (+ capsule) elements, rounded to 2dp
                            count = 1
                            for i in range(len(ghosts)):
                                if ((x,y) == ghosts[i][0]):                            # If this position is where a ghost is
                                    # Set position to ghost reward
                                    self.map.setValue(x,y, round(self.ghostR + (pow(self.discount, count) * neswProbs[neswProbs.index(highest)]), 2))
                                    count += 1

        # Find current postion's 'neighbours'
        neswProbs = uCalc((pacLoc[0],pacLoc[1]))
        # Store the highest value (MEU)
        highest = max(neswProbs)

        # Reset counter to 0
        self.itr = 0
        
        # Conditional to calculate the direction with max utility, find index of the highest value and return move.
        if (neswProbs.index(highest) == 0):             # NORTH
            return api.makeMove(Directions.NORTH, legal)
        elif (neswProbs.index(highest) == 1):           # EAST
            return api.makeMove(Directions.EAST, legal)
        elif (neswProbs.index(highest) == 2):           # SOUTH
            return api.makeMove(Directions.SOUTH, legal)
        elif (neswProbs.index(highest) == 3):           # WEST
            return api.makeMove(Directions.WEST, legal)
        else:                                           # FALLBACK, SAFE
            return api.makeMove(random.choice(legal), legal)