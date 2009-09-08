import karel._

object Main extends Application with KarelTheRobot {
  var world = new World(10,10,Map(
    (4,5) -> new Wall,
    (4,6) -> new Wall,
    (4,7) -> new Wall,
    (4,8) -> new Wall,
    (5,5) -> new Wall,
    (6,5) -> new Wall,
    (7,5) -> new Wall,
    (7,4) -> new Wall,
    (7,3) -> new Wall,
    (3,8) -> new Beeper,
    (0,0) -> new Beeper
  ))


  val k = new Karel(North)
  world = world + ((k,(1,2)))

  DEF('TURN_RIGHT,BLOCK(TURN_LEFT,TURN_LEFT,TURN_LEFT))
  DEF('TURN_AROUND,BLOCK(TURN_LEFT,TURN_LEFT))
  DEF('THROW_BEEPER,BLOCK(
    IF(NEXT_TO_BEEPER,BLOCK(
      PICK_BEEPER,
      ITERATE(5,BLOCK(IF(FRONT_IS_CLEAR,MOVE))),
      PUT_BEEPER,
      CALL('TURN_AROUND),
      ITERATE(5,BLOCK(IF(FRONT_IS_CLEAR,MOVE))),
      CALL('TURN_AROUND)
    ))
  ))

  DEF ('RUN,BLOCK(
    WHILE_DO(FRONT_IS_CLEAR,MOVE),
    CALL('TURN_AROUND)
  ))

  println(world)

  world = BLOCK(
    MOVE,
    MOVE,
    TURN_LEFT,
    MOVE,
    CALL('TURN_AROUND),
    CALL('THROW_BEEPER),
    CALL('RUN)
  )(world,k)

  println(world)
}
