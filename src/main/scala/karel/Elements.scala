package karel

/** A direction that Karel may face.
* @param x the delta x when moving this direction
* @param y the detal y when moving this direction 
*/
abstract class Direction(x:Int,y:Int) {
  /** If moved in this direction from the given location, where do we end up? */
  def move(location:(Int,Int)) = (location._1 + x, location._2 + y)
  /** If we turn left, what direction are we facing? */
  def left:Direction
  def right:Direction
  def behind:Direction
}

object North extends Direction(0,-1) {
  def left = West 
  def right = East
  def behind = South
}
object South extends Direction(0,1) {
  def left = East 
  def right = West
  def behind = North
}
object East extends Direction(1,0) {
  def left = North 
  def right = South
  def behind = West
}
object West extends Direction(-1,0) {
  def left = South 
  def right = North
  def behind = East
}


/** Base of things that can be in the world on a square */
sealed abstract class Element {
  /** True if Karel can occupy this element */
  def occupiable:Boolean
}

/** A beeper, which Karel can pick up or put down */
class Beeper extends Element {
  override def toString = "(*)"
  def occupiable = true
}

/** A wall, which Karel cannot move through */
class Wall extends Element {
  override def toString = "|=|"
  def occupiable = false
}

/** An empty square */
object Empty extends Element {
  override def toString = "   "
  def occupiable = true
}

/** A square outside of the known world */
object OutOfBounds extends Element {
  def occupiable = false
}

/** Karel, our hero */
case class Karel(var direction:Direction) extends Element {
  var beepers:List[Beeper] = Nil

  /** Add a beeper to our beeper bag */
  def +=(b:Beeper) = beepers = b :: beepers

  /** Get a beeper from our bag, if there is one */
  def beeper = beepers match {
    case b :: rest => { beepers = rest; Some(b) }
    case _ => None
  }

  override def toString = direction match {
    case North => " ^ "
    case South => " v "
    case East => " > "
    case West => " < "
  }
  def occupiable = false
}

/** Karel and a bepper can occupy the same square.
 * This could be done away with if we restructure our world data structure.
 */
case class KarelAndBeeper(karel:Karel,beeper:Beeper) extends Element {
  def occupiable = false
  override def toString = karel.direction match {
    case North => " ^*"
    case South => " v*"
    case East => " >*"
    case West => " <*"
  }
}


