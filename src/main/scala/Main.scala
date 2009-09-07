import karel.internal._

object Main extends Application {
  var world = new World(10,10,Map(
    (4,5) -> new Wall,
    (4,6) -> new Wall,
    (4,7) -> new Wall,
    (4,8) -> new Wall,
    (3,8) -> new Beeper
  ))

  val k = new Karel('south)
  world = world + (k,(4,9))
  println(world.toString)
  k.direction = 'east
  println(world.toString)
  world = world - k + (k,(5,9))
  println(world.toString)

  /*
  world = world.addBeeper(new Beeper,(2,2))
  println(world.toString)
  println(world.findKarel(k))
  k.direction = 'west

  world = world.addBeeper(new Beeper,(2,4))
  println(world.toString)

  world = world.removeBeeper((2,2))
  println(world.toString)
  */

}
