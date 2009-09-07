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
  def apply(w:World,k:Karel) = !directionClear(w,k,k.direction)
}
object LEFT_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.left)
}
object LEFT_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !directionClear(w,k,k.direction.left)
}
object RIGHT_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.right)
}
object RIGHT_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !directionClear(w,k,k.direction.right)
}
object BACK_IS_CLEAR extends Condition {
  def apply(w:World,k:Karel) = directionClear(w,k,k.direction.behind)
}
object BACK_IS_BLOCKED extends Condition {
  def apply(w:World,k:Karel) = !directionClear(w,k,k.direction.behind)
}
/*
object NEXT_TO_BEEPER extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NOT_NEXT_TO_BEEPER extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object ANY_BEEPERS_IN_BAG extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NO_BEEPERS_IN_BAG extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object FACING_NORTH extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NOT_FACING_NORTH extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object FACING_SOUTH extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NOT_FACING_SOUTH extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object FACING_EAST extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NOT_FACING_EAST extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object FACING_WEST extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
object NOT_FACING_WEST extends Condition {
  def apply(w:World,k:Karel) = {
  }
}
*/
