package karel

trait KarelTheRobot extends Conditions with Controls with Instructions {

  def run(l:Int,w:Int,map:Map[(Int,Int),Element],defs: => Unit,code: BLOCK) = {
    val k = new Karel(North)
    val world = new World(l,w,map) + (k,0,0)
    defs
    println(world)
    println(code(world,k))
  }
}
