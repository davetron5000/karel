import karel.internal._
import karel._

object TestMain extends Application {
  def assert(exp: => Boolean, message:String) = if (!exp) throw new RuntimeException(message)

  def assertEmpty(w:World,x:Int,y:Int) = w(x,y) match {
    case Empty => ;
    case x:Element => throw new RuntimeException("Expected Empty, but was a " + x.getClass);
  }
  def assertOutOfBounds(w:World,x:Int,y:Int) = w(x,y) match {
    case OutOfBounds => ;
    case x:Element => throw new RuntimeException("Expected OutOfBounds, but was a " + x.getClass);
  }
  def assertWall(w:World,x:Int,y:Int) = w(x,y) match {
    case w:Wall => ;
    case x:Element => throw new RuntimeException("Expected Wall, but was a " + x.getClass);
  }
  def assertBeeper(w:World,x:Int,y:Int) = w(x,y) match {
    case w:Beeper => ;
    case x:Element => throw new RuntimeException("Expected Beeper, but was a " + x.getClass);
  }
  def assertKarel(w:World,x:Int,y:Int) = w(x,y) match {
    case w:Karel => ;
    case x:Element => throw new RuntimeException("Expected Karel, but was a " + x.getClass);
  }
  def assertKarelAndBeeper(w:World,x:Int,y:Int) = w(x,y) match {
    case w:KarelAndBeeper => ;
    case x:Element => throw new RuntimeException("Expected Karel, but was a " + x.getClass);
  }


  var world = new World(10,10,Map(
    (4,5) -> new Wall,
    (4,6) -> new Wall,
    (4,7) -> new Wall,
    (4,8) -> new Wall,
    (3,8) -> new Beeper,
    (0,0) -> new Beeper
  ))

  assertWall(world,4,5)
  assertBeeper(world,3,8)
  val k = new Karel(North)
  world = world + ((k,(1,2)))
  assertKarel(world,1,2)
  assert(world.findKarel(k) == Some(1,2),"No karel a1 1,2, was at " + world.findKarel(k))
  assertEmpty(world,1,3)
  assertEmpty(world,0,1)
  assertEmpty(world,9,9)
  assertOutOfBounds(world,-1,9)
  assertOutOfBounds(world,-1,-1)
  assertOutOfBounds(world,10,1)
  assertOutOfBounds(world,1,10)

  val k2 = new Karel(East)
  world = world + ((k2,(3,8)))
  assertKarelAndBeeper(world,3,8)
  val (world2,e) = world - ((3,8))
  world = world2
  assertKarel(world,3,8)
  val (world3,k3) = world - ((3,8))
  world = world3
  assertEmpty(world,3,8)
  world = world + ((k3,(3,8)))
  world = world + ((e,(3,8)))
  assertKarelAndBeeper(world,3,8)
  world = world - k2
  assertBeeper(world,3,8)
  world = world + ((k2,(2,8)))
  assertBeeper(world,3,8)
  assertKarel(world,2,8)

  val karel = new Karel(North)
  var clearWorld = new World(3,3,Map(
    (0,1) -> new Wall, // E
    (2,1) -> new Wall, // W
    (1,2) -> new Wall)) // S
  clearWorld = clearWorld + ( (karel,(1,1)) ) 
  assert(FRONT_IS_CLEAR(clearWorld,karel),"FRONT is not clear")
  assert(!FRONT_IS_BLOCKED(clearWorld,karel),"FRONT is not blocked")
  assert(!LEFT_IS_CLEAR(clearWorld,karel),"LEFT is not clear")
  assert(LEFT_IS_BLOCKED(clearWorld,karel),"LEFT is not blocked")
  assert(!RIGHT_IS_CLEAR(clearWorld,karel),"RIGHT is not clear")
  assert(RIGHT_IS_BLOCKED(clearWorld,karel),"RIGHT is not blocked")
  assert(!BACK_IS_CLEAR(clearWorld,karel),"BACK is not clear")
  assert(BACK_IS_BLOCKED(clearWorld,karel),"BACK is not blocked")

  clearWorld = TURN_LEFT(clearWorld,karel)
  assert(!FRONT_IS_CLEAR(clearWorld,karel),"FRONT is not clear")
  assert(FRONT_IS_BLOCKED(clearWorld,karel),"FRONT is not blocked")
  assert(!LEFT_IS_CLEAR(clearWorld,karel),"LEFT is not clear")
  assert(LEFT_IS_BLOCKED(clearWorld,karel),"LEFT is not blocked")
  assert(RIGHT_IS_CLEAR(clearWorld,karel),"RIGHT is not clear")
  assert(!RIGHT_IS_BLOCKED(clearWorld,karel),"RIGHT is not blocked")
  assert(!BACK_IS_CLEAR(clearWorld,karel),"BACK is not clear")
  assert(BACK_IS_BLOCKED(clearWorld,karel),"BACK is not blocked")

  assert(NOT_NEXT_TO_BEEPER(clearWorld,karel),"Expected not next to beeper\n" + clearWorld)
  clearWorld = clearWorld + ((new Beeper,(1,1)))
  assert(NEXT_TO_BEEPER(clearWorld,karel),"Expected next to beeper\n" + clearWorld)
  assert(NO_BEEPERS_IN_BAG(clearWorld,karel),"Expected not to have beepers")
  clearWorld = PICK_BEEPER(clearWorld,karel)
  assert(ANY_BEEPERS_IN_BAG(clearWorld,karel),"Expected to have beepers")
  assert(ANY_BEEPERS_IN_BAG(clearWorld,karel),"Expected to have beepers")

  assert(FACING_WEST(clearWorld,karel),"Expected to face west : " + karel.direction)
  assert(NOT_FACING_EAST(clearWorld,karel),"Expected to not face east : " + karel.direction)
  assert(NOT_FACING_NORTH(clearWorld,karel),"Expected to not face north : " + karel.direction)
  assert(NOT_FACING_SOUTH(clearWorld,karel),"Expected to not face south : " + karel.direction)

  clearWorld = TURN_LEFT(clearWorld,karel)


  assert(FACING_SOUTH(clearWorld,karel),"Expected to face west : " + karel.direction)
  assert(NOT_FACING_EAST(clearWorld,karel),"Expected to not face east : " + karel.direction)
  assert(NOT_FACING_NORTH(clearWorld,karel),"Expected to not face north : " + karel.direction)
  assert(NOT_FACING_WEST(clearWorld,karel),"Expected to not face south : " + karel.direction)

}
