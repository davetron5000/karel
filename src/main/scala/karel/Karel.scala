package karel;

import karel.internal._;

abstract class Instruction {
}
/** Move one step in the direction Karel is facing */
case class Move(w:World) extends Instruction {
/*
  def apply(k:Karel) = {
    w.findKarel(k) match {
      case Some(location) => {
        val newLocation = k.direction.move(location)
        w.inspect(newLocation) {
        }
      }
    }
  }
  */
}

/** Rotate to the left, in place */
case class TurnLeft(w:World) extends Instruction
/** Pick up a beeper at the current location */
case class PickBeeper(w:World) extends Instruction
/** Put down a bepper at the current location */
case class PutBeeper(w:World) extends Instruction


