package com.example.yamyam16.auth.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

/*
AOP를 사용하는 이유는 애플리케이션의 핵심 로직과 별개로 로깅, 트랙잭션 처리, 보안 등과 같은 공통된 로직을 분리해서 관리하는 기술이다.
프록시: 프록시 객체가 핵심 로직 앞뒤로 공통된 로직을 처리한다.
로직: 1. 내가 수행하는 service 클래스가 있다.
2. 근데 모든 매서드가 실행 될 때마다 로그를 찍고 싶다 -> 공통된 로직이 발생
3. 프록시 객체를 만들어서 거기에 로그를 찍는 매서드를 생성한다.
4. service 클래스에 생성자로 의존성을 주입한 후 프록시 객체에 있는 매서드를 service 클래스에 있는 모든 매서드 시작 전과 후에 넣는다 => 그러면 코드 중복이 많아진다.
5. 그래서 aop라는 클래스를 만들고 그걸 bean으로 등록해서 해당 service 매서드 실행 앞에 annotation을 통해 간편하게 실행되게 한다.
 */
@Aspect// aop를 정의하는 클래스임을 나타냄
@Component // 해당 클래스를 bean으로 등록 -> controller, service, repository에 해당 안 하는 클래스에 많이 사용함
@RequiredArgsConstructor// 생성자 주입
public class StoreCheckAspect {

	private final StoreRepository storeRepository;
	/*
	aop annotation
	1. @Pointcut : 어떤 매소드에 대해 aop를 적용할지 정의함
	2. @Before : 매소드 실행 전에 실행될 aop를 정의
	3. @After : 매소드 실행 후에 실행될 aop를 정의
	4. @AfterReturning : 매소드가 정상적으로 종료될 때
	5. @AfterThrowing : 매소드에서 예외가 발생할 때
	6. @Around : Before + After 모두 제어 (예외 발생하더라도 실행 됨)

	@pointcut Expression : aop가 어떤 매서드에서 동작할지 지정하는 문장이다.
	@Before을 적어서 @pointcut에 적어둔 매서드 앞에서 실행하겠다는 의미이다.
	@Before("@annotaion(com.example.CheckStoreByStoreId) : 이 annotation이 붙은 대상으로 한다.
	-> service에서 @CheckStoreByStoreId 붙어있는 매서드에서 실행
	&&args(storeId) -> 매서드의 매개변수 중 첫 번째 storeId를 aop로 가져와서 사용하겠다.
	args(..) : 매서드의 매개변수를 모두 받겠다는 의미
	=> 정리 : @CheckStoreByStoreId가 있고, 이게 붙어있는 매서드의 첫 번째 매개변수가 storeId인 경우 그 매서드가 실행되기 전에 이 로직을 싱행 해줘

	 */

	@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckStoreByStoreId) && args(StoreId)")
	public void checkStoreByStoreId(Long StoreId) {
		storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

	}

}
