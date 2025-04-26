package com.example.yamyam16.auth.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.entity.UserType;
import com.example.yamyam16.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	// OAuth2UserService 란? OAuth2UserRequest를 받아서 로그인한 사용자(OAuth2User)를 만들어 주는 역할을 하는 인터페이스
	// OAuth2UserRequest 란? 카카오서버에서 우리서버로 콜백 줄 때, 담겨오는 로그인 인증 정보 객체
	// OAuth2User 란? 카카오 서버에서 사용자 조회 API(https://kapi.kakao.com/v2/user/me)로 가져온 유저 정보를 담는 객체
	// 안에 attributes 맵이 들어 있음 -> 유저 이메일, 닉네임 등
	// 왜 OAuth가 아니고 OAuth2라고 하지?
	// OAuth 1.0은과거에 사용되었고, 지금 표준은 OAuth2.0이여서 그렇게 네이밍 됨.

	private final UserRepository userRepository;

	// OAuth2UserService에 있는 메서드 같은데 이건 어떤 형식으로 사용하는건지
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
		// OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
		// DefaultOAuth2USerService란? Spring이 제공하는 기본 OAuth2 사용자 조회기, 자동으로 카카오 API접근해서 사용자 정보를 가져옴. loadUser만 호줄하면 됨
		// OAuth2UserRequest 보니까 이런 변수가 있던데, 각각 카카오에서 보내주는 정보 값들인지, 간략하게 설명
		//     private final ClientRegistration clientRegistration; // 카카오 앱의 등록 정보(clientId, secret, scope 등)
		//     private final OAuth2AccessToken accessToken; // 로그인 성공하고 받은 액세스 토큰
		//     private final Map<String, Object> additionalParameters; // 토큰 외에 추가적으로 카카오가 준 기타 데이터

		//카카오 응답 구조 해석
		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println("👉 attributes = " + attributes);
		// 이렇게 하면 attributes는 무슨 값을 갖고 있는지?
		// 왜 OAuth2UserRequest에서 안불러오고 유저 객체를 만들어서 쓰는지
		// OAuth2UserRequest는 토큰 정보만 있고 사용자 정보는 없음.
		// 이 OAuth2User의 Attrinutes구조는 아래와 같음
		// {
		// 	"id": 1234567890,
		// 	"kakao_account": {
		// 	"email": "test@example.com"
		// },
		// 	"properties": {
		// 	"nickname": "testuser"
		// }
		// }
		// 그래서 attributes.get("kakao_account") 이런식으로 추출
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		String email = (String)kakaoAccount.get("email"); // 위의 구조로 인해 이렇게 꺼냄
		String nickname = (String)profile.get("nickname");

		// 기존 유저 확인 또는 새로 저장
		User user = userRepository.findByEmail(email)
			.orElseGet(
				() -> userRepository.save(
					new User(UserType.USER, email, "KAKAO", nickname))); // 카카오 로그인 유저는 일반 로그인 비번은 의미 없어서 더미 값 저장.

		// DefaultOAuth2User랑 OAuth2User랑 무슨 차이길래 왜 또 DefaultOAuth2User로 보내는지
		// OAuth2User-> 인터페이스, DefaultOAuth2User -> Spring이 만들어놓은 기본 구현체, 실제 리턴은 실제 객체를 리턴해야함.
		return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER")), attributes, "id");
		// 아니 왜 ROLE_USER는 List.of로 전달하는데? 객체 하나 반환하는거 아닌가?
		// Spring Security는 유저 권한을 목록 형태로 관리함.
		// 그리고 권한은 무조건 SimpleGrantedAuthority 객체로 감싸야 함. 그래서 List.of로 전달
		// 여기서 말하는 id는 무슨 id니 갑자기 뜬금없네
		// attributes에서 id를 기본 유저명으로 사용한다는 의미
		// OAuth2User.getName() 호출 시, id 값을 가져오게 설정 하는 것.

		// 아니 밑에 경로 다 GET 하고 쓰면 접근 된다는거지?? 근데 KakaoOAuth2Service는 언제 발동 되는데?? OAuth2SuccessHandler는 언제 발동되고?
		// spring.security.oauth2.client.provider.kakao.authorization-uri=https://kauth.kakao.com/oauth/authorize
		// spring.security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
		// spring.security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
		// spring.security.oauth2.client.provider.kakao.user-name-attribute=id
		// 사용자가 /oauth2/authorization/kakao로 로그인 시도 -> spring Security가 내부적으로 kakaoOAuth2Service.loadUser() 호출 -> 사용자 정보를 가져옴.
		// 사용자가 정상 로그인 성공 시, OAuth2SuccessHandler.onAuthenticationSuccess() 실행 -> JWT 발급, 쿠키 저장, 응답을 전송 함.

	}

	/*Spring Security OAuth2는 내부적으로 로그인 과정을 이렇게 진행해:
		1.	사용자가 /oauth2/authorization/kakao 요청을 보냄
		2.	Spring Security가 authorization-uri 로 리다이렉트해서 카카오 로그인 화면 띄움
		➔ (https://kauth.kakao.com/oauth/authorize)
		3.	사용자가 카카오 로그인 성공하면 카카오가 코드(Authorization Code)를 우리 서버로 보내줌 (redirect-uri에)
		4.	Spring Security가 받은 코드로 token-uri 에 요청해서 AccessToken을 받아옴
		➔ (https://kauth.kakao.com/oauth/token)
		5.	AccessToken을 들고, user-info-uri 로 사용자 정보 조회 API를 호출함
		➔ (https://kapi.kakao.com/v2/user/me)*/
	// 	이 3가지 통신은 모두 Spring Security OAuth2 Client가 알아서 처리
	//  KakaoOAuth2Service는 이 결과만 받아서
	// 	•	attributes 꺼내고
	// 	•	이메일/닉네임 파싱하고
	// 	•	우리 시스템 유저로 변환하는 “후처리”
	// 즉, KakaoOAuth2Service는 직접 API 호출하지 않아!
	// 이미 Spring Security가 “user-info-uri”로 호출한 결과를 받아서 attributes만 꺼냄.
}
