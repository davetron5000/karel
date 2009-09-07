package karel;

import karel.internal._;

abstract class Instruction {
  def apply(k:Karel):World
}
/** Move one step in the direction Karel is facing */
case class Move(w:World) extends Instruction {
  def apply(k:Karel) = {
    w.findKarel(k) match {
      case Some(location) => {
        val newLocation = k.direction.move(location)
        if (w.inspect(newLocation).occupiable)
          (w - k) + (k,newLocation)
        else
          throw new BadLocation(newLocation)
      }
      case _ => throw new IllegalStateException("No Karel on the board")
    }
  }
}

/** Rotate to the left, in place */
case class TurnLeft(w:World) extends Instruction {
  def apply(k:Karel) = { 
    k.direction = k.direction.left 
    w 
  }
}
/** Pick up a beeper at the current location */
case class PickBeeper(w:World) extends Instruction {
  def apply(k:Karel) = w.findKarel(k) match {
    case Some(location) => w.inspect(location) match {
        case KarelAndBeeper(_,b) => {
          k += b 
          val (newWorld,beeper) = w - location
          newWorld
        }
        case _ => throw new Explosion
      }
    case _ => throw new IllegalStateException("No karel on board")
  }
}
/** Put down a bepper at the current location */
case class PutBeeper(w:World) extends Instruction {
  def apply(k:Karel) = w.findKarel(k) match {
    case Some(location) => k.beeper match {
      case Some(b) => w + (b,location)
      case None => throw new Explosion
    }
    case _ => throw new IllegalStateException("No karel on board")
  }
}


