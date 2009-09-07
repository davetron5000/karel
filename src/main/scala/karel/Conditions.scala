package karel

import karel.internal._;

abstract class Condition extends Function2[World,Karel,Boolean] {

  implicit def optionToLocation(o:Option[(Int,Int)]) = o.getOrElse(null)

  protected def directionClear(w:World,k:Karel,d:Direction) = w.inspect(d.move(w.findKarel(k))).occupiable
}
object FRONT_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction)
}
object FRONT_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !FRONT_IS_CLEAR(w,k)
}
object LEFT_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.left)
}
object LEFT_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !LEFT_IS_CLEAR(w,k)
}
object RIGHT_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.right)
}
object RIGHT_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !RIGHT_IS_CLEAR(w,k)
}
object BACK_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.behind)
}
object BACK_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !BACK_IS_CLEAR(w,k)
}
object NEXT_TO_BEEPER extends Condition {
  def apply(w:World,k:Karel) = w.inspect(w.findKarel(k)) match {
    case _:KarelAndBeeper => true
    case _ => false
  }
}
object NOT_NEXT_TO_BEEPER extends Condition {
  def apply(w:World,k:Karel) = !NEXT_TO_BEEPER(w,k)
}

object ANY_BEEPERS_IN_BAG extends Condition {
  def apply(w:World,k:Karel) = !k.beepers.isEmpty
}
object NO_BEEPERS_IN_BAG extends Condition {
  def apply(w:World,k:Karel) = !ANY_BEEPERS_IN_BAG(w,k)
}

object FACING_NORTH extends Condition {
  def apply(w:World,k:Karel) = k.direction == North
}
object NOT_FACING_NORTH extends Condition {
  def apply(w:World,k:Karel) = !FACING_NORTH(w,k)
}
object FACING_SOUTH extends Condition {
  def apply(w:World,k:Karel) = k.direction == South
}
object NOT_FACING_SOUTH extends Condition {
  def apply(w:World,k:Karel) = !FACING_SOUTH(w,k)
}
object FACING_EAST extends Condition {
  def apply(w:World,k:Karel) = k.direction == East
}
object NOT_FACING_EAST extends Condition {
  def apply(w:World,k:Karel) = !FACING_EAST(w,k)
}
object FACING_WEST extends Condition {
  def apply(w:World,k:Karel) = k.direction == West
}
object NOT_FACING_WEST extends Condition {
  def apply(w:World,k:Karel) = !FACING_WEST(w,k)
}
