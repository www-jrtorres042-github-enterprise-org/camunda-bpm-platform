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

import org.camunda.bpm.engine.impl.core.model.DefaultCallableElementTenantIdProvider;
import org.camunda.bpm.engine.impl.el.ElValueProvider;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.scripting.ScriptFactory;
import org.camunda.bpm.engine.impl.scripting.ScriptValueProvider;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListValueProviderTest {

  @Test
  public void shouldBeUnsafeToEvaluateIfItContainsUnsafeProvider() {
    ListValueProvider provider = new ListValueProvider(
        Arrays.asList(
            new DefaultCallableElementTenantIdProvider(),
            new ElValueProvider(new ExpressionManager().createExpression("${unsafe}"))));
    assertTrue(provider.isEvaluationUnsafe());

    provider = new ListValueProvider(
        Arrays.asList(
            new DefaultCallableElementTenantIdProvider(),
            new ScriptValueProvider(new ScriptFactory().createScriptFromResource("javascript", "window.alert()"))));
    assertTrue(provider.isEvaluationUnsafe());
  }

  @Test
  public void shouldBeSafeToEvaluateIfItContainsOnlySafeProviders() {
    ListValueProvider provider = new ListValueProvider(
        Arrays.asList(
            new DefaultCallableElementTenantIdProvider(),
            new ElValueProvider(new ExpressionManager().createExpression("safe1")),
            new NullValueProvider(),
            new ListValueProvider(Arrays.asList(new ConstantValueProvider("safe2")))));
    assertFalse(provider.isEvaluationUnsafe());
  }
}