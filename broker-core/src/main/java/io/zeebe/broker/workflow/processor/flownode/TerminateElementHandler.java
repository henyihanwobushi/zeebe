/*
 * Zeebe Broker Core
 * Copyright © 2017 camunda services GmbH (info@camunda.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.zeebe.broker.workflow.processor.flownode;

import io.zeebe.broker.logstreams.processor.TypedBatchWriter;
import io.zeebe.broker.logstreams.processor.TypedRecord;
import io.zeebe.broker.workflow.data.WorkflowInstanceRecord;
import io.zeebe.broker.workflow.model.ExecutableFlowNode;
import io.zeebe.broker.workflow.processor.BpmnStepContext;
import io.zeebe.broker.workflow.processor.BpmnStepHandler;
import io.zeebe.protocol.intent.WorkflowInstanceIntent;

public class TerminateElementHandler implements BpmnStepHandler<ExecutableFlowNode> {

  @Override
  public void handle(BpmnStepContext<ExecutableFlowNode> context) {
    final TypedRecord<WorkflowInstanceRecord> terminatingRecord = context.getRecord();

    final TypedBatchWriter batch = context.getStreamWriter().newBatch();

    addTerminatingRecords(context, batch);

    batch.addFollowUpEvent(
        terminatingRecord.getKey(),
        WorkflowInstanceIntent.ACTIVITY_TERMINATED,
        terminatingRecord.getValue());

    context.destroyElementInstance();
  }

  // to be overriden by subclasses
  protected void addTerminatingRecords(
      BpmnStepContext<ExecutableFlowNode> context, TypedBatchWriter batch) {}
}
