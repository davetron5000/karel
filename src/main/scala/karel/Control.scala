package karel

import karel.internal._;

object Control {
  var subs:Map[Symbol,BLOCK] = Map()

  implicit def instructionToBlock(i:Instruction) = BLOCK(i)

  def DEF(name:Symbol,b:BLOCK) = subs = subs + (name -> b)

  case class CALL(name:Symbol) extends Instruction {
    def apply(w:World,k:Karel) = subs.get(name).getOrElse(null)(w,k)
  }

  case class BLOCK(s:Instruction*) extends KarelStatement[World] {
    def apply(w:World,k:Karel) = {
      var world = w
      s.foreach ( (i) => world = i(world,k) )
      world
    }
  }

  case class IF(c:Condition,t:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      if (c(w,k))
        t(w,k)
      else
        w
    }
  }

  case class IF_THEN(c:Condition,t:BLOCK,e:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      if (c(w,k))
        t(w,k)
      else
        e(w,k)
    }
  }

  case class WHILE_DO(c:Condition,d:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      var world = w
      while (c(world,k)) {
        world = d(world,k)
      }
      world
    }
  }

  case class ITERATE(times:Int,d:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      var world = w
      for (i <- 0 until times) {
        world = d(world,k)
      }
      world
    }
  }

}
