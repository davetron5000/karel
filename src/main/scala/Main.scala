import karel.internal._
import karel._

object Main extends Application {
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

  println(world)
  world = new Move(world)(k)
  println(world)
  world = new TurnLeft(world)(k)
  println(world)
  world = new Move(world)(k)
  println(world)
  world = new TurnLeft(world)(k)
  world = new TurnLeft(world)(k)
  world = new TurnLeft(world)(k)
  world = new Move(world)(k)
  world = new PickBeeper(world)(k)
  println(world)
  world = new TurnLeft(world)(k)
  world = new TurnLeft(world)(k)
  world = new TurnLeft(world)(k)
  world = new Move(world)(k)
  println(world)
  world = new PutBeeper(world)(k)
  println(world)
}
