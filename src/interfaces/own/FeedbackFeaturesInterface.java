package titan.controllers;

import interfaces.given.Vector3dInterface;
import interfaces.own.WindInterface;

/**
 * Base structure to represent the additional features a feedback controller presents
 */
public interface FeedbackFeaturesInterface {

    public Vector3dInterface correctRocketLanding(WindInterface windState);

}
