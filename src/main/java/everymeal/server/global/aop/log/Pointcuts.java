package everymeal.server.global.aop.log;


import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* everymeal.server.meal.*.*(..))")
    public void all() {}

    @Pointcut("execution(* everymeal.server..*Service.*(..))")
    public void allService() {}

    @Pointcut(
            "execution(* everymeal.server..*Repository.*(..)) || execution(* everymeal.server..*RepositoryImpl.*(..))")
    public void allQuery() {}
}
