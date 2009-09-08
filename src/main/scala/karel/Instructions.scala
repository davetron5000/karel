package karel;

abstract class KarelStatement[R] extends Function2[World,Karel,R] 

trait Instructions {
  abstract class Instruction extends Function2[World,Karel,World]

  object MOVE extends Instruction {
    def apply(w:World,k:Karel) = {
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
  object TURN_LEFT extends Instruction {
    def apply(w:World,k:Karel) = { 
      k.direction = k.direction.left 
      w 
    }
  }

  object PICK_BEEPER extends Instruction {
    def apply(w:World,k:Karel) = w.findKarel(k) match {
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
  object PUT_BEEPER extends Instruction {
    def apply(w:World,k:Karel) = w.findKarel(k) match {
      case Some(location) => k.beeper match {
        case Some(b) => w + (b,location)
        case None => throw new Explosion
      }
      case _ => throw new IllegalStateException("No karel on board")
    }
  }
}
