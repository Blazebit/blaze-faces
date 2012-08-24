/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.examples.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import com.blazebit.blazefaces.examples.domain.Car;
import com.blazebit.blazefaces.model.SelectableDataModel;

public class CarDataModel extends ListDataModel<Car> implements
		SelectableDataModel<Car>, Serializable {

	private static final long serialVersionUID = 1L;

	public CarDataModel() {
	}

	public CarDataModel(List<Car> data) {
		super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Car getRowData(String rowKey) {
		// In a real app, a more efficient way like a query by rowKey should be
		// implemented to deal with huge data

		List<Car> cars = (List<Car>) getWrappedData();

		for (Car car : cars) {
			if (car.getModel().equals(rowKey))
				return car;
		}

		return null;
	}

	@Override
	public Object getRowKey(Car car) {
		return car.getModel();
	}
}
