package com.example.yamyam16.auth.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
aop에서 사용할 annotation 정의 클래스가 있어야 한다.
내가 서비스 로직에서 공통으로 처리하고 싶은 작업이 있을 때, 그 로직은 AOP 클래스에 따로 작성해. 그런데 이 공통 로직을 언제, 어떤 메서드에서 실행할지를 지정해줘야 하잖아? 그 역할을 하는 게 바로 포인트컷이고, 이걸 어노테이션으로 지정할 수 있어.
그래서 먼저 AOP에서 사용할 어노테이션을 직접 정의한 클래스가 필요해. 예를 들어 @CheckUserDeleted 같은 어노테이션을 만들어두는 거야.

그 다음, AOP 클래스에서는 “이 어노테이션이 붙은 메서드가 실행되기 전에, 내가 정의한 공통 로직을 실행해줘” 라고 설정해놓는 거지.
서비스 로직 쪽에서는 어노테이션 하나만 붙여주면 AOP가 자동으로 해당 로직을 실행해줘.

즉, AOP는 "이 어노테이션이 있으면 내가 이 로직을 대신 실행해줄게요" 라고 공통 처리를 담당하고, 서비스 메서드에서는 "이 메서드에서 AOP 실행해주세요" 라는 의미로 어노테이션을 붙이는 거야. 그 어노테이션은 미리 정의한 클래스에서 만들어둬야 하고
 */

@Target(ElementType.METHOD) // 매서드에 붙일 수 있도록
@Retention(RetentionPolicy.RUNTIME) // 런타임에서 aop를 인식할 수 있도록
public @interface CheckStoreByStoreId { //@interface는 annotation을 만들기 위한 키워드이다. 근데 왜 interface이냐 annotation도 자바에서는 일종의 인터페이스이다.
}
