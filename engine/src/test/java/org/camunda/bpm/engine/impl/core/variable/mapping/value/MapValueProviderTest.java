
/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.engine.impl.core.variable.mapping.value;

import org.camunda.bpm.engine.impl.el.ElValueProvider;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapValueProviderTest {

  @Test
  public void shouldHaveSafeEvaluationForSafeKVs() {
    TreeMap<ParameterValueProvider, ParameterValueProvider> treeMap = new TreeMap<>();
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("safeKey")),
        new ElValueProvider(new ExpressionManager().createExpression("safeValue")));
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("safeKey2")),
        new ElValueProvider(new ExpressionManager().createExpression("safeValue2")));

    MapValueProvider provider = new MapValueProvider(treeMap);
    assertFalse(provider.isEvaluationUnsafe());
  }

  @Test
  public void shouldHaveUnsafeEvaluationIfContainsUnsafeValues() {
    TreeMap<ParameterValueProvider, ParameterValueProvider> treeMap = new TreeMap<>();
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("safeKey")),
        new ElValueProvider(new ExpressionManager().createExpression("safeValue")));
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("safeKey2")),
        new ElValueProvider(new ExpressionManager().createExpression("#{unsafe}")));
    MapValueProvider provider = new MapValueProvider(treeMap);
    assertTrue(provider.isEvaluationUnsafe());
  }

  @Test
  public void shouldHaveUnsafeEvaluationIfContainsUnsafeKeys() {
    TreeMap<ParameterValueProvider, ParameterValueProvider> treeMap = new TreeMap<>();
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("safeValue")),
        new ElValueProvider(new ExpressionManager().createExpression("safeKey")));
    treeMap.put(new ElValueProvider(new ExpressionManager().createExpression("${unsafe}")),
        new ElValueProvider(new ExpressionManager().createExpression("safeKey2")));
    MapValueProvider provider = new MapValueProvider(treeMap);
    assertTrue(provider.isEvaluationUnsafe());
  }
}