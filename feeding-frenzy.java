import javalib.worldimages.*;
import tester.Tester;
import javalib.funworld.*;
import java.util.Random;
import java.awt.*;

// main game class that handles the game state, rendering, and interactions
class FishWorld extends World {
  AFish player;
  ILoFish fishes;
  WorldScene background;

  FishWorld(AFish player, ILoFish fishes, WorldScene background) {
    this.player = player;
    this.fishes = fishes;
    this.background = background;
  }

  /* fields:
   *  this.player ... PlayerFish
   *  this.fishes ... ILoFish
   *  this.rand ... Random
   *  this.background ... WorldScene
   * methods:
   *  this.makeScene() ... WorldScene
   *  this.onKeyEvent(String) ... WorldScene
   *  this.onTick() ... FishWorld
   *  this.makeGameOverScene() ... WorldScene
   *  this.makeYouWinScene() ... WorldScene
   *  this.worldEnds() ... WorldEnd
   * methods of fields:
   *  this.player.move(String) ... AFish
   *  this.player.move() ... AFish
   *  this.player.collidesWith(AFish) ... boolean
   *  this.player.draw() ... WorldScene
   *  this.player.canEat(AFish) ... boolean
   *  this.player.canEatAndResult(AFish) .. AFish
   *  this.fishes.moveAll() ... ILoFish
   *  this.fishes.drawAll(WorldScene) ... WorldScene
   *  this.fishes.removeEaten(AFish) ... ILoFish
   *  this.fishes.anyEatPlayer(AFish) ... boolean
   *  this.fishes.addRandom(AFish) ... ILoFish
   *  this.fishes.addRandomForTesting(AFish, Random) ... ILoFish
   *  this.fishes.isLargest(AFish) ... boolean
   *  this.fishes.ifEatThenGrow(AFish) ... AFish
   */

  // creates the current scene of the game
  public WorldScene makeScene() {
    return this.player.draw(this.fishes.drawAll(background.placeImageXY(
        new RectangleImage(800, 500, OutlineMode.SOLID, Color.LIGHT_GRAY), 400, 250)));
  }

  // handles key events for controlling the player fish
  public FishWorld onKeyEvent(String key) {
    if (key.equals("right")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("left")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("up")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("down")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("r")) {
      return new FishWorld(new PlayerFish(new Random()), 
          new ConsLoFish(new BackgroundFish(new Random()), new MtLoFish()), this.background);
    }
    else {
      return this;
    }
  }

  //handles key events for controlling the player fish
  public FishWorld onKeyEventForTesting(String key, Random rand) {
    if (key.equals("right")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("left")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("up")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("down")) {
      return new FishWorld(this.player.move(key), this.fishes, this.background);
    }
    else if (key.equals("r")) {
      return new FishWorld(new PlayerFish(rand), 
          new ConsLoFish(new BackgroundFish(rand), new MtLoFish()), this.background);
    }
    else {
      return this;
    }
  }

  // updates the game state on each tick
  public FishWorld onTick() {
    return new FishWorld(this.fishes.ifEatThenGrow(this.player), 
        this.fishes.removeEaten(this.player).addRandom(this.player).moveAll(), this.background);
  }

  //updates the game state on each tick
  public FishWorld onTickForTesting(Random rand) {
    return new FishWorld(this.fishes.ifEatThenGrow(this.player), 
        this.fishes.removeEaten(this.player).addRandomForTesting(this.player, rand).moveAll(), 
        this.background);
  }

  // determines if the game has ended and returns the final scene
  public WorldEnd worldEnds() {
    if (player.size > 70 && this.fishes.isLargest(this.player)) {
      return new WorldEnd(true, this.makeYouWinScene());
    }
    else if (this.fishes.anyEatPlayer(player)) {
      return new WorldEnd(true, this.makeGameOverScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // creates the final scene when the user loses the game
  public WorldScene makeGameOverScene() {
    return this.background.placeImageXY(
        new TextImage("GAME OVER", 50, FontStyle.BOLD, Color.BLACK), 400, 250);
  }

  // creates the final scene when the use wins the game
  public WorldScene makeYouWinScene() {
    return this.background.placeImageXY(
        new TextImage("YOU WIN!", 50, FontStyle.BOLD, Color.YELLOW), 400, 250);
  }
}

// represents a fish
interface IFish {

  // moves a fish
  AFish move(String key);

  // checks if a fish collides with another fish
  boolean collidesWith(AFish other);

  // draws a fish
  WorldScene draw(WorldScene scene);

  // can a fish eat another fish
  boolean canEat(AFish other);

  // changes a fish based on if it can eat it
  AFish canEatAndResult(AFish other);
}

// a generic fish
abstract class AFish implements IFish {
  Random rand;
  int x; // x coordinate of this fish
  int y; // y coordinate of this fish
  Color color; // color of this fish
  boolean movingRight; // is this fish moving right
  int speed; // speed of this fish
  int size; // size of this fish


  // constructor with all field inputs
  AFish(Random rand, int x, int y, Color color, boolean movingRight, int speed, int size) {
    this.rand = rand;
    this.x = x;
    this.y = y;
    this.color = color;
    this.movingRight = movingRight;
    this.speed = speed;
    this.size = size;
  }

  /* fields:
   *  this.x ... int
   *  this.y ... int
   *  this.color ... Color
   *  this.movingRight ... boolean
   *  this.speed ... int
   *  this.size ... int
   *  this.rand ... Random
   * methods:
   *  this.move(String) ... AFish
   *  this.move() ... AFish
   *  this.collidesWith(AFish) ... boolean
   *  this.draw() ... WorldScene
   *  this.canEat(AFish) ... boolean
   *  this.canEatAndResult(AFish) .. AFish
   * methods of fields: none 
   */

  // abstract method to move a fish
  public abstract AFish move(String key);

  // abstract method to move a fish
  public abstract AFish move();

  // checks if a fish collides with another fish
  public boolean collidesWith(AFish other) {  
    return (Math.abs(this.x - other.x) < 4) && (Math.abs(this.y - other.y) < 4);
  }

  // draws a fish
  public WorldScene draw(WorldScene scene) {
    return scene.placeImageXY(new CircleImage(this.size, OutlineMode.SOLID, this.color), 
        this.x, this.y);
  }

  // checks if a fish can eat another fish
  public boolean canEat(AFish other) {
    return (this.size > other.size);
  }

  // checks if a fish can eat another fish and increases its size proportionally
  public abstract AFish canEatAndResult(AFish other);

}

// represents the player fish
class PlayerFish extends AFish {

  // constructor for the player fish
  PlayerFish(Random rand, int x, int y, Color color, boolean movingRight, int speed, int size) {
    super(rand, x, y, color, movingRight, speed, size);
  }

  // random constructor for the player fish
  PlayerFish(Random rand) {
    super(rand, rand.nextInt(800), rand.nextInt(500), Color.RED, rand.nextBoolean(), 4, 20);
  }

  // moves the player fish
  public AFish move(String key) {
    if (this.x > 800 && this.movingRight) {
      return new PlayerFish(this.rand, 0, this.y, this.color, true, this.speed, this.size);
    }
    else if (this.x < 0 && !this.movingRight) {
      return new PlayerFish(this.rand, 800 , this.y, this.color, true, this.speed, this.size);
    }
    else if (this.y > 500) {
      return new PlayerFish(this.rand, this.x , 500, this.color, true, this.speed, this.size);
    }
    else if (this.y < 0) {
      return new PlayerFish(this.rand, this.x, 0, this.color, true, this.speed, this.size);
    }
    if (key.equals("right")) {
      return new PlayerFish(this.rand, this.x + this.speed, this.y, 
          this.color, true, this.speed, this.size);
    }
    else if (key.equals("left")) {
      return new PlayerFish(this.rand, this.x - this.speed, this.y, 
          this.color, false, this.speed, this.size);
    }
    else if (key.equals("up")) {
      return new PlayerFish(this.rand, this.x, this.y - this.speed, 
          this.color, this.movingRight, this.speed, this.size);
    }
    else if (key.equals("down")) {
      return new PlayerFish(this.rand, this.x, this.y + this.speed, 
          this.color, this.movingRight, this.speed, this.size);
    }
    else {
      return this;
    }
  }

  // this method is only used for background fish, no implementation for this player fish
  public AFish move() {
    return this;
  }

  // checks if this player fish can eat another fish and increases the size if so
  public AFish canEatAndResult(AFish other) {
    return new PlayerFish(this.rand, this.x, this.y, this.color, this.movingRight, 
        this.speed, (this.size + ((5 / 4) * other.size)));
  }

}

// represents the background fish
class BackgroundFish extends AFish {

  // constructor with all field inputs
  BackgroundFish(Random rand, int x, int y, Color color, boolean movingRight, 
      int speed, int size) {
    super(rand, x, y, color, movingRight, speed, size);
  }

  // random constructor of background fish
  BackgroundFish(Random rand) {
    super(rand, rand.nextInt(800), rand.nextInt(500), Color.BLACK, rand.nextBoolean(), 
        rand.nextInt(5), rand.nextInt(10));
  }

  // this method is only used for player fish, no implementation for background fish
  public AFish move(String key) {
    return this;
  }

  // moves a background fish by its speed and direction
  public AFish move() {
    if (this.movingRight) {
      return new BackgroundFish(this.rand, (this.x + this.speed) % 800, this.y, this.color, 
          this.movingRight, this.speed, this.size);
    }
    else if (this.x < 0) {
      return new BackgroundFish(this.rand, 800, this.y, this.color, 
          this.movingRight, this.speed, this.size);
    }
    else {
      return new BackgroundFish(this.rand, this.x - this.speed, this.y, this.color, 
          this.movingRight, this.speed, this.size);
    }
  }

  // background fish do not change when they eat any other fish
  public AFish canEatAndResult(AFish other) {
    return this;
  }
}

// a list of fish
interface ILoFish {

  // moves all fish in the list
  ILoFish moveAll();

  // draws all fish in the list
  WorldScene drawAll(WorldScene scene);

  // removes any eaten fish from the list
  ILoFish removeEaten(AFish player);

  // checks if any fish in the list have eaten the player fish
  boolean anyEatPlayer(AFish player);

  // adds additional background fish to the list
  ILoFish addRandom(AFish player);

  //adds additional background fish to the list
  ILoFish addRandomForTesting(AFish player, Random rand);

  //checks if any fish in the list are bigger than the player fish
  boolean isLargest(AFish player);

  // checks if the player fish can eat any fish in the list and increases its size
  AFish ifEatThenGrow(AFish player);

}

// represents an empty list of fish
class MtLoFish implements ILoFish {

  // cannot move an empty list of fish
  public ILoFish moveAll() {
    return this;
  }

  // cannot draw an empty list of fish
  public WorldScene drawAll(WorldScene scene) {
    return scene;
  }

  // cannot remove any fish from an empty list of fish
  public ILoFish removeEaten(AFish player) {
    return this;
  }

  // no fish can collide in an empty list of fish
  public boolean anyEatPlayer(AFish player) {
    return false;
  }

  // adds a fish to an empty list of fish
  public ILoFish addRandom(AFish player) {
    Random rand = new Random();
    if (rand.nextInt(15) == 1) {
      return new ConsLoFish(new BackgroundFish(new Random(), -1, rand.nextInt(500), 
          Color.BLACK, rand.nextBoolean(), rand.nextInt(5) + 3, rand.nextInt(player.size) + 5), 
          new MtLoFish());
    }
    else {
      return this;
    }
  }

  public ILoFish addRandomForTesting(AFish player, Random rand) {
    if (rand.nextInt(15) == 1) {
      return new ConsLoFish(new BackgroundFish(rand, -1, rand.nextInt(500), 
          Color.BLACK, rand.nextBoolean(), rand.nextInt(5) + 3, rand.nextInt(player.size) + 5), 
          new MtLoFish());
    }
    else {
      return this;
    }
  }

  // checks if any fish are bigger than a given fish
  public boolean isLargest(AFish other) {
    return false;
  }

  //checks if the player fish can eat any fish in the list and increases its size accordingly
  public AFish ifEatThenGrow(AFish player) {
    return player;
  }
}

class ConsLoFish implements ILoFish {
  AFish first;
  ILoFish rest;

  ConsLoFish(AFish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }

  // moves all fish in this list of fish
  public ILoFish moveAll() {
    return new ConsLoFish(this.first.move(), this.rest.moveAll());
  }

  // draws all fish in this list of fish
  public WorldScene drawAll(WorldScene scene) {
    return this.rest.drawAll(this.first.draw(scene));
  }

  // removes any fish that were eaten
  public ILoFish removeEaten(AFish player) {
    if (player.collidesWith(this.first) &&  player.canEat(this.first)) {
      return this.rest.removeEaten(player);
    }
    else {
      return new ConsLoFish(this.first, this.rest.removeEaten(player));
    }
  }

  // checks if any fish have collided and eaten the player fish
  public boolean anyEatPlayer(AFish player) {
    return (this.first.collidesWith(player) && this.first.canEat(player))
        || this.rest.anyEatPlayer(player);
  }

  // adds a random background fish to a list of fish
  public ILoFish addRandom(AFish player) {
    Random rand = new Random();
    if (rand.nextInt(15) == 1) {
      return new ConsLoFish(new BackgroundFish(new Random(), -1, rand.nextInt(500), 
          Color.BLACK, rand.nextBoolean(), rand.nextInt(5) + 3, rand.nextInt(player.size) + 5), 
          this);
    }
    else {
      return this;
    }
  }

  // adds a random background fish to a list of fish
  public ILoFish addRandomForTesting(AFish player, Random rand) {
    if (rand.nextInt(15) == 1) {
      return new ConsLoFish(new BackgroundFish(rand, -1, rand.nextInt(500), 
          Color.BLACK, rand.nextBoolean(), rand.nextInt(5) + 3, rand.nextInt(player.size) + 5),
          this);
    }
    else {
      return this;
    }
  }

  // checks if any fish in the list of fish are greater than a given fish
  public boolean isLargest(AFish other) {
    return other.canEat(this.first) || this.rest.isLargest(other);
  }

  //checks if a fish can eat any fish in the list and increases its size accordingly
  public AFish ifEatThenGrow(AFish player) { 
    if (player.collidesWith(this.first)) {
      return this.rest.ifEatThenGrow(player.canEatAndResult(this.first)); 
    }
    else {
      return this.rest.ifEatThenGrow(player);
    }
  }
}

//represents examples of fishes and the fish world
class ExamplesFish {
  Random rand = new Random();

  int SCENE_WIDTH = 800;
  int SCENE_HEIGHT = 500;

  AFish player = new PlayerFish(this.rand, 200, 200, Color.RED, true, 4, 20);
  AFish player2 = new PlayerFish(this.rand, 801, 200, Color.RED, true, 4, 20);
  AFish player3 = new PlayerFish(this.rand, -1, 200, Color.RED, true, 4, 20);
  AFish player4 = new PlayerFish(this.rand, 200, 501, Color.RED, true, 4, 20);
  AFish player5 = new PlayerFish(this.rand, 200, -1, Color.RED, true, 4, 20);
  AFish bFishRight = new BackgroundFish(this.rand, 150, 150, Color.BLACK, true, 5, 10);
  AFish bFishLeft = new BackgroundFish(this.rand, 50, 50, Color.BLACK, false, 5, 10);
  AFish bFish2 = new BackgroundFish(this.rand, -1, 50, Color.BLACK, false, 5, 10);
  AFish bFish3 = new BackgroundFish(this.rand, 200, 200, Color.BLACK, false, 5, 30);
  AFish bFish4 = new BackgroundFish(this.rand, 200, 200, Color.BLACK, false, 5, 5);

  ILoFish noFish = new MtLoFish();
  ILoFish fishes1 = new ConsLoFish(this.bFishRight, this.noFish);
  ILoFish fishes2 = new ConsLoFish(this.bFishLeft, this.fishes1);
  ILoFish fishes3 = new ConsLoFish(bFish3, this.noFish);
  ILoFish fishes4 = new ConsLoFish(bFish4, this.noFish);

  WorldScene background = new WorldScene(SCENE_WIDTH, SCENE_HEIGHT);


  FishWorld world = new FishWorld(this.player, this.fishes2, this.background);
  FishWorld world2 = new FishWorld(this.player, this.fishes1, this.background);

  // test the main big bang function and run the game
  boolean testBigBang(Tester t) {
    return world.bigBang(SCENE_WIDTH, SCENE_HEIGHT, 0.1);
  }

  // test the move method
  boolean testMove(Tester t) {
    return t.checkExpect(this.player.move("up"),
        new PlayerFish(this.rand, 200, 196, Color.RED, true, 4, 20))
        && t.checkExpect(this.player.move("down"),
            new PlayerFish(this.rand, 200, 204, Color.RED, true, 4, 20))
        && t.checkExpect(this.player.move("left"),
            new PlayerFish(this.rand, 196, 200, Color.RED, false, 4, 20))
        && t.checkExpect(this.player.move("right"),
            new PlayerFish(this.rand, 204, 200, Color.RED, true, 4, 20))
        && t.checkExpect(this.player2.move("right"),
            new PlayerFish(this.rand, 0, 200, Color.RED, true, 4, 20))
        && t.checkExpect(this.player3.move("right"),
            new PlayerFish(this.rand, 3, 200, Color.RED, true, 4, 20))
        && t.checkExpect(this.player4.move("right"),
            new PlayerFish(this.rand, 200, 500, Color.RED, true, 4, 20))
        && t.checkExpect(this.player5.move("right"),
            new PlayerFish(this.rand, 200, 0, Color.RED, true, 4, 20))
        && t.checkExpect(this.player.move(), this.player)
        && t.checkExpect(this.bFishRight.move(),
            new BackgroundFish(this.rand, 155, 150, Color.BLACK, true, 5, 10))
        && t.checkExpect(this.bFishLeft.move(),
            new BackgroundFish(this.rand, 45, 50, Color.BLACK, false, 5, 10))
        && t.checkExpect(this.bFish2.move(),
            new BackgroundFish(this.rand, 800, 50, Color.BLACK, false, 5, 10));
  }

  // test draw method
  boolean testDraw(Tester t) {
    return t.checkExpect(this.player.draw(new WorldScene(800, 500)), 
        new WorldScene(800, 500).placeImageXY(new CircleImage(20, OutlineMode.SOLID, 
            Color.RED), 200, 200)) 
        && t.checkExpect(this.bFishRight.draw(new WorldScene(800, 500)), 
            new WorldScene(800, 500).placeImageXY(new CircleImage(10, OutlineMode.SOLID, 
                Color.BLACK), 150, 150));
  }

  // test onKeyEvent method
  boolean testOnKeyEventForTesting(Tester t) {
    AFish expectedPlayer = new PlayerFish(new Random(), 713, 380, Color.RED, false, 4, 20);
    AFish expectedBackground = new BackgroundFish(new Random(), 90, 246, Color.BLACK, true, 2, 8);
    ILoFish expectedList = new ConsLoFish(expectedBackground, this.noFish);
    FishWorld expectedWorld = new FishWorld(expectedPlayer, expectedList, this.background);

    return t.checkExpect(this.world.onKeyEventForTesting("right", new Random()), 
        new FishWorld(new PlayerFish(this.rand, 204, 200, Color.RED, true, 4, 20),
            this.fishes2, this.background)) 
        && t.checkExpect(this.world.onKeyEventForTesting("left", new Random()), 
            new FishWorld(new PlayerFish(this.rand, 196, 200, Color.RED, false, 4, 20),
                this.fishes2, this.background)) 
        && t.checkExpect(this.world.onKeyEventForTesting("up", new Random()), 
            new FishWorld(new PlayerFish(this.rand, 200, 196, Color.RED, true, 4, 20),
                this.fishes2, this.background)) 
        && t.checkExpect(this.world.onKeyEventForTesting("down", new Random()), 
            new FishWorld(new PlayerFish(this.rand, 200, 204, Color.RED, true, 4, 20), 
                this.fishes2, this.background))
        && t.checkExpect(this.world.onKeyEventForTesting("r", new Random(10)), expectedWorld);
  }

  // test worldEnds method
  boolean testWorldEnds(Tester t) {
    FishWorld largePlayerWorld = new FishWorld(
        new PlayerFish(rand, 200, 200, Color.RED, true, 4, 71), this.fishes2, this.background);
    FishWorld gameOverWorld = new FishWorld(this.player, 
        new ConsLoFish(new BackgroundFish(rand, 200, 200, Color.BLACK, true, 4, 10),
            new MtLoFish()), this.background);

    return t.checkExpect(this.world.worldEnds(), new WorldEnd(false, this.world.makeScene())) 
        && t.checkExpect(largePlayerWorld.worldEnds(), 
            new WorldEnd(true, largePlayerWorld.makeYouWinScene())) 
        && t.checkExpect(gameOverWorld.worldEnds(), 
            new WorldEnd(false, gameOverWorld.makeScene()));
  }

  // test makeGameOverScene method
  boolean testMakeGameOverScene(Tester t) {
    return t.checkExpect(this.world.makeGameOverScene(), 
        this.background.placeImageXY(new TextImage("GAME OVER", 50, FontStyle.BOLD, Color.BLACK),
            400, 250));
  }

  // test makeYouWinScene method
  boolean testMakeYouWinScene(Tester t) {
    return t.checkExpect(this.world.makeYouWinScene(), 
        this.background.placeImageXY(new TextImage("YOU WIN!", 50, FontStyle.BOLD, Color.YELLOW), 
            400, 250));
  }

  // tests collidesWith method
  boolean testCollidesWith(Tester t) {
    return t.checkExpect(this.player.collidesWith(this.player), true) 
        && t.checkExpect(this.player.collidesWith(this.player2), false) 
        && t.checkExpect(this.bFish2.collidesWith(this.player), false);
  }

  // test canEat method
  boolean testCanEat(Tester t) {
    return t.checkExpect(this.player.canEat(this.bFish2), true)
        && t.checkExpect(this.bFish4.canEat(this.player), false)
        && t.checkExpect(this.player.canEat(this.bFishRight), true) 
        && t.checkExpect(this.player.canEat(this.bFishLeft), true) 
        && t.checkExpect(this.player.canEat(this.player), false) ;
  }

  // test canEatAndResult method
  boolean testCanEatAndResult(Tester t) {
    AFish fish1 = new BackgroundFish(new Random(), 200, 200, Color.BLACK, true, 4, 10);
    AFish fish2 = new PlayerFish(this.rand, 200, 200, Color.RED, true, 4, 
        this.player.size + ((5 / 4) * fish1.size));
    AFish fish3 = new PlayerFish(this.rand, 200, 200, Color.RED, true, 4, 
        this.player.size + ((5 / 4) * this.bFishRight.size));
    return t.checkExpect(this.player.canEatAndResult(fish1), fish2)
        && t.checkExpect(this.bFish2.canEatAndResult(this.player), this.bFish2)
        && t.checkExpect(this.player.canEatAndResult(this.bFishRight), 
            fish3);
  }

  // test ILoFish moveAll method
  boolean testILoFishMoveAll(Tester t) {
    ILoFish expectedFishes = new ConsLoFish(this.bFishLeft.move(), 
        new ConsLoFish(this.bFishRight.move(), this.noFish));
    return t.checkExpect(this.fishes2.moveAll(), expectedFishes)
        && t.checkExpect(this.noFish.moveAll(), this.noFish);
  }

  // test ILoFish drawAll method
  boolean testILoFishDrawAll(Tester t) {
    WorldScene expectedScene = this.fishes2.drawAll(new WorldScene(800, 500));
    return t.checkExpect(this.fishes2.drawAll(new WorldScene(800, 500)), expectedScene)
        && t.checkExpect(this.noFish.drawAll(this.background), this.background);
  }

  // test ILoFish removeEaten method
  boolean testILoFishRemoveEaten(Tester t) {
    ILoFish expectedFishes = new ConsLoFish(this.bFishLeft, 
        new ConsLoFish(this.bFishRight, this.noFish));
    return t.checkExpect(this.fishes2.removeEaten(this.player), expectedFishes)
        && t.checkExpect(this.fishes4.removeEaten(this.player), this.noFish)
        && t.checkExpect(this.noFish.removeEaten(this.player), this.noFish);
  }

  // test ILoFish anyEatPlayer method
  boolean testILoFishAnyEatPlayer(Tester t) {
    return t.checkExpect(this.fishes2.anyEatPlayer(this.player), false)
        && t.checkExpect(this.fishes3.anyEatPlayer(this.player), true)
        && t.checkExpect(this.noFish.anyEatPlayer(this.player), false);
  }

  // test ILoFish addRandom method
  boolean testILoFishAddRandomForTesting(Tester t) {
    AFish b1 = new BackgroundFish(new Random(), 50, 50, Color.BLACK, false, 5, 10);
    AFish b2 = new BackgroundFish(new Random(), 150, 150, Color.BLACK, true, 5, 10);
    ILoFish addedRandomFishes = new ConsLoFish(b1, new ConsLoFish(b2, this.noFish));
    return t.checkExpect(this.fishes2.addRandomForTesting(this.player, new Random(10)), 
        addedRandomFishes)
        && t.checkExpect(this.noFish.addRandomForTesting(this.player, new Random(10)), 
            new MtLoFish());
  }

  // test ILoFish isLargest method
  boolean testILoFishIsLargest(Tester t) {
    return t.checkExpect(this.fishes2.isLargest(this.player), true)
        && t.checkExpect(this.fishes3.isLargest(this.player), false)
        && t.checkExpect(this.noFish.isLargest(this.player), false);
  }

  // test ILoFish ifEatThenGrow method
  boolean testILoFishIfEatThenGrow(Tester t) {
    AFish noGrow = new PlayerFish(new Random(), 200, 200, Color.RED, true, 4, 20);
    AFish growFish = new PlayerFish(new Random(), 200, 200, Color.RED, true, 4, 25);
    return t.checkExpect(this.fishes2.ifEatThenGrow(this.player), noGrow)
        && t.checkExpect(this.fishes4.ifEatThenGrow(this.player), growFish)
        && t.checkExpect(this.noFish.ifEatThenGrow(this.player), this.player);
  }

}
