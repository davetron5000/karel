# Karel The Robot in Scala

Finding myself with my laptop and without net access for a day I thought it would be fun to implement a DSL for [Karel The Robot](http://mormegil.wz.cz/prog/karel/prog_doc.htm) in Scala.

The idea was to get as close as possible to the Scala code looking like Karel code.

# WTF is Karel the Robot?

Karel the Robot is a programming language designed for extreme beginners.  The programs you write are control programs for a robot, named Karel, that has the ability to move around a grid picking up and putting down "beepers".  The grid contains walls, through which Karel cannot pass.

Further, Karel has a very limited command set.  Karel can turn left, move forward, pick up a beeper, and put down a beeper.  This leads to some very simple subroutines that a beginning programmer can construct.  For example, the "turn right" subroutine can be created by three "turn left" commands.

# How to Use It

Karel's programming language doesn't define a way to create the world in which Karel lives, so you have to do that first.  After that, you can define subroutines, and then enter your program.
    
    > cat Program.scala
    
    import karel._
    object Program extends Application with KarelTheRobot {
      BEGIN_PROGRAM(10,10, // a 10x10 World
          Map(
            (4,5) -> new Wall, // a wall will appear at 4x5
            (4,6) -> new Wall,
            (4,7) -> new Wall,
            (4,8) -> new Wall,
            (5,5) -> new Wall,
            (6,5) -> new Wall,
            (7,5) -> new Wall,
            (7,4) -> new Wall,
            (7,3) -> new Wall,
            (3,8) -> new Beeper, // a beeper will appear at 3x8
            (0,0) -> new Beeper
          ),
          {
            DEF('TURN_RIGHT,BLOCK(ITERATE(3,TURN_LEFT)))
            DEF('TURN_AROUND,BLOCK(TURN_LEFT,TURN_LEFT))
            DEF('RUN,BLOCK(
              WHILE_DO(FRONT_IS_CLEAR,MOVE),
              CALL('TURN_AROUND)
            ))
          },
          BEGIN_EXECUTION(
            WHILE_DO(NOT_FACING_EAST,TURN_LEFT),
            PICK_BEEPER,
            CALL('RUN),
            PUT_BEEPER
          ))
    }

    > ./make.sh
    > ./run.sh Program

# API

Karel's language is made up of control structures, conditions, and instructions.

## Instructions
  - `MOVE` - move forward in the direction Karel is facing
  - `TURN_LEFT` - turn to the left, in place
  - `PICK_BEEPER` - pick up a beeper located at the current position 
  - `PUT_BEEPER` - put down a beeper at the current position

## Conditions

These are all fairly explanatory

  - `FRONT_IS_CLEAR`
  - `FRONT_IS_BLOCKED`
  - `LEFT_IS_CLEAR`
  - `LEFT_IS_BLOCKED`
  - `RIGHT_IS_CLEAR`
  - `RIGHT_IS_BLOCKED`
  - `BACK_IS_CLEAR`
  - `BACK_IS_BLOCKED`
  - `NEXT_TO_BEEPER`
  - `NOT_NEXT_TO_BEEPER`
  - `ANY_BEEPERS_IN_BAG`
  - `NO_BEEPERS_IN_BAG`
  - `FACING_NORTH`
  - `NOT_FACING_NORTH`
  - `FACING_SOUTH`
  - `NOT_FACING_SOUTH`
  - `FACING_EAST`
  - `NOT_FACING_EAST`
  - `FACING_WEST`
  - `NOT_FACING_WEST`

## Control Structures
 
  - `BLOCK` - define a block of commands
  - `IF_THEN_ELSE` - test for a condition and execute one of two blocks, depending on its outcome
  - `IF` - test for a condition and execute a block true (doing nothing if false; Syntactic Sugar!)
  - `WHILE_DO` - repeatedly perform a block of actions while a condition holds
  - `ITERATE` - perform a block of actions a fixed number of times

## Subroutines

  - `DEF('SUB_NAME,BLOCK)` - define a subroutine named `'SUB_NAME` described by the commands in `BLOCK`.  This is not executable and must be called before the program is defined.
  - `CALL('SUB_NAME)` - call the subroutine named `'SUB_NAME`

# Similarities to "real" Karel code

The overall structure, as well as the reserved words, are very similar to Karel code, however Scala didn't make it easy to mimic the "BEGIN/END" block definition syntax (Karel took this from Pascal).  

I omitted Karel's two superfluous commands `TURN_ON` and `TURN_OFF`, as they seemed kindof pointless.

The one area where Scala made this somewhat tedious is in the conditionals definition.  Part of the "simplicity" of Karel is that there are no operators and no functions that take arguments.  This makes conditions like `BACK_IS_CLEAR` and `BACK_IS_BLOCKED` very clear and easy to come up with (you just choose the condition from a list).  While experienced programmers might prefer `isClear(BACK)` and `!isClear(BACK)`, this would complicate Karel beyond it's purpose as a learning tool.

Given this, we can see that the definition of the conditions is *very* repetitive.  Since I wanted the resulting DSL to be executable Scala code (and not a script that I parsed at runtime), there's really no simple way around it that I could see.  Ruby, however, would've made this task much simpler and required much less repeated code.  For example, the 8 conditions regarding the direction in which Karel is facing, could've been implemented in one method:

    def method_missing(sym,args*)
      karel = args[1]
      parts = sym.split(/_/,3)
      if parts[-2] == "FACING"
        direction = parts[-1]
        if parts[0] == "NOT"
          return karel.direction == direction
        else
          return karel.direction != direction
      end
    end

(This could probably be tightened up a bit)

Also, the Scala means of calling defined subroutines that I came up with (`CALL('SUB_NAME))`) is kinda lame; the subs don't look like real calls (as they would in proper Karel).  Ruby, via the ever-useful `method_missing`, would've made this dead simple.

I plan to work up a Ruby version of Karel as a comparison, but my suspicion is that Ruby would afford a much cleaner DSL in the end.

# What's up with `./make.sh` dude?

I was offline entirely, so Maven was out (plus I cannot stand Maven).  I also didn't have SBT available, so shell scripts to the rescue.
