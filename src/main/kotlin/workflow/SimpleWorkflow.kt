package workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SimpleWorkflow {
    @WorkflowMethod
    fun process(payload: WorkflowPayload): String
}