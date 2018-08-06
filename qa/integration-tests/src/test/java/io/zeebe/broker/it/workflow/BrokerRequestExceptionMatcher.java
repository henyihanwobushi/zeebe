/*
 * Copyright © 2017 camunda services GmbH (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package io.zeebe.broker.it.workflow;

import io.zeebe.gateway.cmd.BrokerErrorException;
import io.zeebe.protocol.clientapi.ErrorCode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class BrokerRequestExceptionMatcher extends BaseMatcher<BrokerErrorException> {

  protected ErrorCode expectedDetailCode;

  public static BrokerRequestExceptionMatcher brokerException(ErrorCode expectedDetailCode) {
    final BrokerRequestExceptionMatcher matcher = new BrokerRequestExceptionMatcher();
    matcher.expectedDetailCode = expectedDetailCode;
    return matcher;
  }

  @Override
  public boolean matches(Object item) {
    if (item == null || !(item instanceof BrokerErrorException)) {
      return false;
    }

    final BrokerErrorException exception = (BrokerErrorException) item;

    return expectedDetailCode == exception.getErrorCode();
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(BrokerErrorException.class.getSimpleName());
    description.appendText(" with detail code " + expectedDetailCode);
  }
}
