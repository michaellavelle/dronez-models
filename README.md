# dronez-models

The goal of this project is to generate serialized instances of 

``Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction>``

which can be imported into other Dronez projects, eg.

1)  To help create mock Drone implementations, allowing us to obtain predicted next ``DroneStateWithRecentAction``s from current ``DroneStateWithRecentAction``s after a DroneAction is executed.

2)  To allow us to apply Continuous-State Markov Decision Processes to learn optimal policies for maximising state-dependent reward.

The project contains a standalone ModelLearningApplication which uses yet-to-be-written-implementations of

``ModelLearner<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction>``

to generate and serialize Models.
 
The training data for the ModelLearners comes in the form of serialized instances of 

``StateActionSequenceHistory<DroneState,DroneState,DroneAction>``

included with the project.  

The histories are comprised of an aggregation of webcam-estimated position,velocity and command sequences from actual ARDrone flights.

These histories are converted into instances of 

``StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction>``

which are passed to the ModelLearners before the training can begin.
