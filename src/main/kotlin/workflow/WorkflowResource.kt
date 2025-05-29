package org.example.workflow

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType.APPLICATION_JSON


@ApplicationScoped
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/workflow")
class WorkflowResource(
    private val workflowClient: WorkflowClient
) {

    @POST
    fun start(workflowPayload: WorkflowPayload) {
        val stub = workflowClient.newWorkflowStub(
            SimpleWorkflow::class.java,
            WorkflowOptions.newBuilder()
                .setTaskQueue("simple-task-queue")
                .build()
        )

        WorkflowClient.start(
            stub::process,
            workflowPayload
        )
    }
}