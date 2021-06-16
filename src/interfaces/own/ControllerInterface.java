package titan.controllers;

import interfaces.own.RocketInterface;



/**
 * Base structure to represent the controllers including the open and feedback controllers
 */
public interface ControllerInterface {

    public void moveUp(RocketInterface r);
    public void moveDown(RocketInterface r);
    public void moveLeft(RocketInterface r);
    public void moveRight(RocketInterface r);



    // The following methods will increase the scale

    public void boostMoveUp(RocketInterface r);
    public void boostMoveDown(RocketInterface r);
    public void boostMoveLeft(RocketInterface r);
    public void boostMoveRight(RocketInterface r);

    // Distance left to land on Titan
    public double distanceToTitan();





}
