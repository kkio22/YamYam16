package com.example.yamyam16.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateStoreResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.MenuListResponseDto;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreService extends CommonAuthforOwner {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	public final MenuRepository menuRepository;

	//가게생성
	@Transactional
	public CreateStoreResponseDto createStore(CreateStoreRequestDto dto, User user) {

		//권환확인
		validateOwnerRole(user);
		//가게 갯수 확인
		if (storeRepository.countByUserAndIsDeleteFalse(user) > 3) {
			throw new StoreCustomException(StoreCustomErrorCode.OVER_CREATE);
		}
		//가게생성
		Store store = new Store(dto, user);
		//저장
		Store createStore = storeRepository.save(store);
		return CreateStoreResponseDto.fromStoreToDto(createStore);

	}

	//todo : 이거 수정할 것
	//가게전체조회 : 폐업된 가게 조회 안함
	@Transactional
	public Page<SearchStoreResponseDto> getAllStores(String storeName, Pageable pageable) {
		return storeRepository.findAllByIsDeleteFalseAndNameContainingOrderByCreatedAtDesc(storeName, pageable)
			.map(SearchStoreResponseDto::fromStoreToDto); // map (반환데이터 값 :: 객체(new나 메소드))

	}

	//    //가게목록조회
	//    @Transactional
	//    public Page<SearchStoreResponseDto> getStoresByCategory() {
	//
	//    }

	// 자신이 운영하는 가게 찾기
	public List<Store> getAllStoresByOwner(Long userId) {
		List<Store> stores = storeRepository.findAllByIsDeleteFalseAndUser_Id(userId);

		if (stores.isEmpty()) {
			throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_FOUND);
		}
		return stores;
	}

	public List<MenuListResponseDto> findMenuByPage(Long storeId, Long page, Long size) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException((StoreCustomErrorCode.STORE_NOT_FOUND))); // 해당 가게가 있는지 확인

		//pageable 객체 생성 => 페이지 번호, 페이지 갯수, 정렬 나타냄
		Pageable pageable = PageRequest.of(page.intValue() - 1, size.intValue(),
			Sort.by("menuPrice").ascending()); // 데이터 베이스 기준으로 페이지 번호는 0부터 시작한다. 내가 데이터베이스에서 값을 가져와야  하기에 이 로직 필요

		//가게 id에 대한 메뉴 목록 페이징 조회 (페이징에 대한 정보 + 메뉴 목록)
		Page<Menu> menuPage = menuRepository.findAllByStoreId(storeId,
			pageable); // 데이터 베이스에서 entity 값을 가지고 나오는데 매개변수가 pageable이니 page로 감싼다.

		//메뉴 목록만 조회
		List<Menu> menuList = new ArrayList<>(menuPage.getContent());

		//entity -> dto로 변환해서 반환 => stream이나 for문 사용해야 함
		return menuList.stream() //컬렉션인 List를 스트림으로 변환
			.map(menu -> new MenuListResponseDto(menu.getId(), menu.getMenuName(),//스트릶에 있는 menu객체를 하나씩 꺼내옴
				menu.getMenuPrice())) // entity -> dto로 변경 (MenuListResponseDto :: new)
			.toList(); // 다시 List로 변환
	}

	//가게수정
	@Transactional
	public UpdateStoreResponseDto updateStoreById(Long id, UpdateStoreRequestDto dto, User user) {
		//권환확인
		validateOwnerRole(user);
		//가게 찾기
		Store store = storeRepository.findByIdOrElseThrow(id);
		//해당 객체 가져와서 엔티티에서 수정
		store.update(dto);
		return new UpdateStoreResponseDto(store);
	}

	//가게삭제
	@Transactional
	public DeactivateStoreResponseDto deactivateStoreById(Long id, User user) {

		//권환확인
		validateOwnerRole(user);
		//가게 존재 여부
		Store findStore = storeRepository.findByIdOrElseThrow(id);
		findStore.deactivate();
		return new DeactivateStoreResponseDto(findStore);
	}

	// 오더에서 사용합니다
	public String findStoreName(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
		return store.getName();
	}

}
