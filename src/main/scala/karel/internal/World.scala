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
case class Karel(var direction:Symbol) extends Element {
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


class BadLocation(val location:(Int,Int)) extends RuntimeException

/** The current state of the world.  Immutable */
class World(val width: Int, val height: Int, state: Map[(Int,Int),Element]) {

  object World {
    def apply(w:World,newState:Map[(Int,Int),Element]) = new World(w.width,w.height,newState)
  }

  def +(t:Tuple2[Element,Tuple2[Int,Int]]) = {
    inspect(t._2) match {
      case None => World(this,state + (t._2 -> t._1))
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

  def -(location:(Int,Int)):(World,Option[Element]) = {
    inspect(location) match {
      case None => (this,None)
      case Some(b:Beeper) => (World(this,state - location),Some(b))
      case Some(k:Karel) => (World(this,state - location),Some(k))
      case Some(KarelAndBeeper(k,b)) => (World(this,state - location) + (k,location),Some(b))
      case _ => throw new BadLocation(location)
    }
  }

  def -(k:Karel):World = {
    findKarel(k) match {
      case Some(location) => this.-(location)._1
      case None => this
    }
  }

  def removeKarel(location:(Int,Int)) = World(this,inspect(location) match {
    case Some(k:Karel) => state - location
    case Some(kb:KarelAndBeeper) => (state - location) + (location -> kb.beeper)
    case _ => state
  })

  def addKarel(location:(Int,Int), k:Karel) = World(this,inspect(location) match {
    case None => state + (location -> k)
    case Some(b:Beeper) => state + (location -> new KarelAndBeeper(k,b))
    case _ => throw new BadLocation(location)
  })

  /** Remove a beeper from the world, returning a new world without that beeper */
  def removeBeeper(location:(Int,Int)) = World(this,inspect(location) match {
    case Some(b:Beeper) => state - location
    case Some(kb:KarelAndBeeper) => (state - location) + (location -> kb.karel)
    case _ => state
  })

  /** Adds a new beeper to the world, returning the world with that beeper */
  def addBeeper(b:Beeper, location:(Int,Int)) = World(this,inspect(location) match {
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
  def inspect(location:(Int,Int)) = state.get(location)

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
