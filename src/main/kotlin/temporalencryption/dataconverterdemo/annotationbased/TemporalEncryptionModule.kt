package temporalencryption.dataconverterdemo.annotationbased

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.module.SimpleModule

class TemporalEncryptionModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        val introspector = object : AnnotationIntrospector() {
            override fun version(): Version = Version.unknownVersion()
            
            override fun findSerializer(
                am: Annotated
            ): Any? =
                if (am.hasAnnotation(TemporalEncrypted::class.java)) TemporalEncryptedSerializer::class.java else null

            override fun findDeserializer(
                am: Annotated
            ): Any? =
                if (am.hasAnnotation(TemporalEncrypted::class.java)) TemporalEncryptedDeserializer::class.java else null
        }
        context.insertAnnotationIntrospector(introspector)
    }
}