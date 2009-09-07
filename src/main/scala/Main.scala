import karel.internal._
import karel._

object Main extends Application {
  var world = new World(10,10,Map(
    (4,5) -> new Wall,
    (4,6) -> new Wall,
    (4,7) -> new Wall,
    (4,8) -> new Wall,
    (3,8) -> new Beeper,
    (0,0) -> new Beeper
  ))


  val k = new Karel(North)
  world = world + ((k,(1,2)))

  val instructions = List(
    MOVE,
    TURN_LEFT,
    MOVE,
    TURN_LEFT,
    TURN_LEFT,
    TURN_LEFT,
    MOVE,
    PICK_BEEPER,
    TURN_LEFT,
    TURN_LEFT,
    TURN_LEFT,
    MOVE,
    PUT_BEEPER,
    MOVE,MOVE,MOVE)

  instructions.foreach ( (i) => {world = i(world,k); println(world) })
  println(world)
}
