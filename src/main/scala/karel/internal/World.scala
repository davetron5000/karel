package karel.internal

import scala.collection.mutable.{Map => MutableMap}

sealed abstract class Element

/** A beeper, which Karel can pick up or put down */
class Beeper extends Element {
  override def toString = "(*)"
}

/** A wall, which Karel cannot move through */
class Wall extends Element {
  override def toString = "|=|"
}

/** Karel, our hero */
case class Karel(direction:Symbol) extends Element {
  override def toString = direction match {
    case 'north => " ^ "
    case 'south => " v "
    case 'east => " > "
    case 'west => " < "
  }
}

/** Karel and a bepper can occupy the same square */
case class KarelAndBeeper(karel:Karel,beeper:Beeper) extends Element {
  override def toString = karel.direction match {
    case 'north => " ^*"
    case 'south => " v*"
    case 'east => " >*"
    case 'west => " <*"
  }
}


class BadLocation(val location:Tuple2[Int,Int]) extends RuntimeException

object World {
  def apply(w:World,newState:Map[Tuple2[Int,Int],Element]) = new World(w.width,w.height,newState)
}

/** The current state of the world.  Immutable */
class World(val width: Int, val height: Int, state: Map[Tuple2[Int,Int],Element]) {

  /** Insert Karel into the world; this returns a new World */
  def insertKarel(location:Tuple2[Int,Int], k:Karel) = World(this,inspect(location) match {
    case None => state + (location -> k)
    case Some(b:Beeper) => state + (location -> new KarelAndBeeper(k,b))
    case _ => throw new BadLocation(location)
  })

  /** Remove a beeper from the world, returning a new world without that beeper */
  def removeBeeper(location:Tuple2[Int,Int]) = World(this,inspect(location) match {
    case Some(b:Beeper) => state - location
    case Some(kb:KarelAndBeeper) => (state - location) + (location -> kb.karel)
    case _ => state
  })

  /** Adds a new beeper to the world, returning the world with that beeper */
  def addBeeper(b:Beeper, location:Tuple2[Int,Int]) = World(this,inspect(location) match {
    case Some(k:Karel) => state + (location -> new KarelAndBeeper(k,b))
    case None => state + (location -> b)
    case _ => throw new BadLocation(location)
  })

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
  def inspect(location:Tuple2[Int,Int]) = state.get(location)

  override def toString() = {
    val buf = new StringBuilder
    buf.append("  _")
    for (x <- new Range(0,width,1)) if (x < 10) buf.append("_" + x + "_") else buf.append("_" + x)
    buf.append("_\n")
    for ( y <- new Range(0,height,1); x <- new Range(0,width,1)) {
      if (x == 0) if (y < 10) buf.append(" " + y + "|") else buf.append(y + "|")
      inspect((x,y)) match {
        case Some(x:Element) => buf.append(x.toString)
        case _ => buf.append("   ")
      }
      if (x == (width - 1)) buf.append("|\n")
    }
    buf.append("  _")
    for (x <- new Range(0,width,1)) buf.append("___")
    buf.append("_\n")
    buf.toString
  }
}
