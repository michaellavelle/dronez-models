/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ml4j.dronez.models.learning;

import java.io.Serializable;

import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;
/**
 * Implementing classes learn a Model for a drone given a state action sequence history.
 * 
 * @author Michael Lavelle
 *
 * @param <S> Initial State
 * @param <A> Action
 * @param <T> Post-Action State
 */
public interface ModelLearner<S extends Serializable,A extends Serializable,T extends Serializable> {

	public Model<S,A,T> learnModel(StateActionSequenceHistory<S,A,T> stateActionStateHistory);
}
