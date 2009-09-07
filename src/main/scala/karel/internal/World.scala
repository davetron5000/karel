package karel.internal

/** Internal code made a mistake */
class BadLocation(val location:(Int,Int)) extends RuntimeException {
  override def getMessage = location.toString
}

/** Karel did something he should've have */
class Explosion extends RuntimeException

/** The current state of the world.  Immutable */
class World(val width: Int, val height: Int, state: Map[(Int,Int),Element]) {

  object World {
    def apply(w:World,newState:Map[(Int,Int),Element]) = new World(w.width,w.height,newState)
  }

  /** Add the given element at the given coordinates */
  def +(t:Tuple2[Element,Tuple2[Int,Int]]) = {
    inspect(t._2) match {
      case Empty => World(this,state + (t._2 -> t._1))
      case b:Beeper => t._1 match {
        case k:Karel => World(this,state + (t._2 -> new KarelAndBeeper(k,b)))
        case _ => throw new BadLocation(t._2)
      }
      case k:Karel => t._1 match {
        case b:Beeper => World(this,state + (t._2 -> new KarelAndBeeper(k,b)))
        case _ => throw new BadLocation(t._2)
      }
      case _ => throw new BadLocation(t._2)
    }
  }

  /** Remove whatever is at this location, if it can be so removed.  */
  def -(location:(Int,Int)):(World,Element) = {
    inspect(location) match {
      case Empty => (this,Empty)
      case b:Beeper => (World(this,state - location),b)
      case k:Karel => (World(this,state - location),k)
      case KarelAndBeeper(k,b) => (World(this,state - location) + (k,location),b)
      case _ => throw new BadLocation(location)
    }
  }

  /** Remove karel from whever he is */
  def -(k:Karel):World = {
    findKarel(k) match {
      case Some(location) => this.-(location)._1
      case None => this
    }
  }

  /** Finds karel in the world */
  def findKarel(k:Karel) = state.find( (item) => item._2 match {
      case karel:Karel if karel == k => true
      case KarelAndBeeper(karel,b) if (karel == k) => true
      case _ => false
    }) match {
      case Some((location,element)) => Some(location)
      case _ => None
    }

  /** Inspect what is at the given location */
  def inspect(location:(Int,Int)) = 
    if (inBounds(location)) state.get(location).getOrElse(Empty)
    else OutOfBounds

  /** Inspect what is at the given location */
  def apply(x:Int,y:Int) = inspect((x,y))

  /** Prints a text view of the world */
  override def toString() = {
    val buf = new StringBuilder
    buf.append("  _")
    for (x <- new Range(0,width,1)) if (x < 10) buf.append("_" + x + "_") else buf.append("_" + x)
    buf.append("_\n")
    for ( y <- new Range(0,height,1); x <- new Range(0,width,1)) {
      if (x == 0) if (y < 10) buf.append(" " + y + "|") else buf.append(y + "|")
      inspect((x,y)) match {
        case x:Element => buf.append(x.toString)
      }
      if (x == (width - 1)) buf.append("|\n")
    }
    buf.append("  _")
    for (x <- new Range(0,width,1)) buf.append("___")
    buf.append("_\n")
    buf.toString
  }

  private def inBounds(location:(Int,Int)) = 
    location._1 >= 0 && location._1 < width && location._2 >= 0 && location._2 < height

}
