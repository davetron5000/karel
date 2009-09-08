package karel

/** Control Structures */
trait Controls extends Instructions with Conditions {
  var subs:Map[Symbol,BLOCK] = Map()

  implicit def instructionToBlock(i:Instruction) = BLOCK(i)

  /** 
  * Define a subroutine
  * @param name the name of the subroutine
  * @param b a block defining the subroutine
  */
  def DEF(name:Symbol,b:BLOCK) = subs = subs + (name -> b)

  /** 
   * Call a subroutine
   * @param name the name of the subroutine to call
   */
  case class CALL(name:Symbol) extends Instruction {
    def apply(w:World,k:Karel) = subs.get(name) match {
      case Some(b:BLOCK) => b(w,k)
      case None => throw new Explosion
    }
  }

  /** 
    * Define a block of instructions
    */
  case class BLOCK(s:Instruction*) extends KarelStatement[World] {
    def apply(w:World,k:Karel) = {
      var world = w
      s.foreach ( (i) => world = i(world,k) )
      world
    }
  }

  /** Syntactic Sugar */
  case class BEGIN_EXECUTION(program:Instruction*) extends BLOCK(program.toArray: _*)

  /** A no-op */
  object NOOP extends Instruction {
    def apply(w:World,k:Karel) = w
  }

  /**
    * A conditional with an else 
    * @param c the condition
    * @param t the block to execute if c is true
    * @param e the block to execute if c is not true
    */
  case class IF_THEN_ELSE(c:Condition,t:BLOCK,e:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      if (c(w,k))
        t(w,k)
      else
        e(w,k)
    }
  }

  /**
    * a conditional.
    * @param c a Conditiont o evaluate
    * @param t the block to evaluate if condition is true
    */
  case class IF(override val c:Condition,override val t:BLOCK) extends IF_THEN_ELSE(c,t,NOOP)

  /**
    * A loop
    * @param c the condition to evaluate each time through the loop
    * @param d the block to execute each time through the loop
    */
  case class WHILE_DO(c:Condition,d:BLOCK) extends Instruction {
    def apply(w:World,k:Karel) = {
      var world = w
      while (c(world,k)) {
        world = d(world,k)
      }
      world
    }
  }

  /**
    * an iterative loop
    * @param times the number of times to iterate
    * @param d the block to iterate each time through the loop
    */
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
