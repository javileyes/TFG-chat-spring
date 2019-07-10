package es.javiergimenez.chat.config.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse


@Component
class HeaderFilter: Filter {

    @Value("\${spring.cloud.client.hostname}") lateinit var hostname: String


    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.setHeader("Microservice-Id", hostname)
        chain!!.doFilter(request, response)
    }

}