package com.example.yamyam16.store.controller;

import com.example.yamyam16.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store/ownercomment")
@RequiredArgsConstructor
public class CommentConroller {

    private final StoreService ownercommentService;

}
