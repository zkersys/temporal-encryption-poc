package workflow

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DataConverter
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class WorkflowClientProducer(
    private val dataConverter: DataConverter,
) {

    @Produces
    fun workflowClient(): WorkflowClient {
        println("HERE")
        println(dataConverter)
        return WorkflowClient.newInstance(
            WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.getDefaultInstance()),
            WorkflowClientOptions.newBuilder()
                .setDataConverter(
                    dataConverter
                )
                .build()
        )
    }
}