package workflow

import io.quarkiverse.temporal.TemporalWorkflow
import io.temporal.workflow.Workflow

@TemporalWorkflow(workers = ["simple-task-queue"])
class SimpleWorkflowImpl : SimpleWorkflow {
    override fun process(payload: WorkflowPayload): String {
        println("Payload in workflow: $payload")
        Workflow.getLogger(SimpleWorkflowImpl::class.java).info("Processing: $payload")
        return "Received payload"
    }
}