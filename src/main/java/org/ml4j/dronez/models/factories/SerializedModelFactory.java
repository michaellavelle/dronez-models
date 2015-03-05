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
package org.ml4j.dronez.models.factories;

import java.util.HashMap;
import java.util.Map;

import org.ml4j.mdp.Model;
import org.ml4j.util.SerializationHelper;

/**
 * Deserialize a Model from a model id
 * 
 * @author Michael LAvelle
 *
 * @param <S> Initial State
 * @param <A> Action
 * @param <T> Post-Action State
 */
public class SerializedModelFactory<S,A,T> implements ModelFactory<S,A,T> {

	private SerializationHelper serializationHelper = new SerializationHelper(SerializedModelFactory.class.getClassLoader(),"org/ml4j/dronez/models");
	
	private Map<String,Class<? extends Model<S,A,T>>> modelClassesById = new HashMap<String,Class<? extends Model<S,A,T>>>();
		
	public <M extends Model<S,A,T>> void registerModel(Class<M> modelClass, String modelId)
	{
		modelClassesById.put(modelId, modelClass);
	}
	
	
	@Override
	public Model<S, A, T> createModel(String modelId) {
		
		if (!modelClassesById.containsKey(modelId)) throw new IllegalArgumentException("Model with id " + modelId + " has not been registered");
		try
		{
			return serializationHelper.deserialize(modelClassesById.get(modelId), modelId);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to deserialize model with id:" + modelId + " -  has a model been generated yet with ModelLearningApplication?");
		}
	}

}
