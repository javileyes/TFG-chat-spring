package es.javiergimenez.chat.security.aspect

import es.javiergimenez.chat.config.exception.ForbiddenException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.expression.BeanFactoryResolver
import org.springframework.core.annotation.Order
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component


@Aspect
@Order(2)
@Component
class AuthorizeAspect {

    @Autowired lateinit var applicationContext: ApplicationContext


    @Before("@annotation(es.javiergimenez.chat.security.aspect.Authorize)")
    fun authorizedBefore(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val authorize = method.getAnnotation(Authorize::class.java)
        val result: Boolean = getDynamicValue(methodSignature.parameterNames, joinPoint.args, authorize.value)
        if (!result) throw ForbiddenException()
    }

    fun getDynamicValue(parameterNames: Array<String>, args: Array<Any>, value: String): Boolean {
        val spelExpressionParser = SpelExpressionParser()
        val standardEvaluationContext = StandardEvaluationContext()
        standardEvaluationContext.setBeanResolver(BeanFactoryResolver(applicationContext))
        for (i in parameterNames.indices) {
            standardEvaluationContext.setVariable(parameterNames[i], args[i])
        }
        return spelExpressionParser.parseExpression(value).getValue<Boolean>(standardEvaluationContext, Boolean::class.java)!!
    }

}