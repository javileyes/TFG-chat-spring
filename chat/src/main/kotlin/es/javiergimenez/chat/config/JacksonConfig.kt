package es.javiergimenez.chat.config

import com.fasterxml.jackson.datatype.joda.JodaModule
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.DateTimeParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Configuration
class JacksonConfig {


//    @Bean
//    fun hibernate5Module(): Hibernate5Module {
//        val hibernate5Module = Hibernate5Module()
//        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true)
//        return hibernate5Module
//    }

    @Bean
    fun jodaModule(): JodaModule {
        return JodaModule()
    }

    @Component
    class DateTimeConfig : Converter<String, DateTime> {

        override fun convert(source: String): DateTime {
            return formatter.parseDateTime(source)
        }

        companion object {
            private val parsers = arrayOf<DateTimeParser>(
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parser, // 2018-03-01T11:08:45.000Z
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").parser,     // 2018-03-01T11:08:45Z
                    DateTimeFormat.forPattern("yyyy-MM-dd' 'HH:mm:ss.SSSZ").parser,
                    DateTimeFormat.forPattern("yyyy-MM-dd' 'HH:mm:ssZ").parser
            )
            private val formatter = DateTimeFormatterBuilder().append(null, parsers).toFormatter()
        }
    }
}