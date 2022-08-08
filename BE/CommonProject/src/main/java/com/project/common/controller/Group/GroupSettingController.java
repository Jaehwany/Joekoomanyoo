package com.project.common.controller.Group;


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.common.config.Jwt.JwtTokenProvider;
import com.project.common.dto.Group.GroupSettingDto;
import com.project.common.service.Group.GroupSettingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor   
@RequestMapping("/api/group")

@Api(tags = {"모임 설정 API"})
public class GroupSettingController {
    private final GroupSettingService groupSettingService;
    private final JwtTokenProvider jwtTokenProvider;
    
    //모임 상태 변경 (R(모집), O(진행), F(종료))
    @ApiOperation(value = "모임 상태 변경 - R(모집), O(진행), F(종료)")
    @PutMapping("/{groupSeq}/status")
    public ResponseEntity<String> changeStatus(HttpServletRequest request,@PathVariable("groupSeq") int groupSeq, @RequestBody GroupSettingDto groupSettingDto){
    	String token = request.getHeader("X-AUTH-TOKEN");
   	 	if (token == null || !jwtTokenProvider.validateToken(token)) return null;
    	return new ResponseEntity<>(groupSettingService.changeStatus(groupSeq,groupSettingDto),HttpStatus.OK);
    }
    
    //모임 활성화/비활성화
    @ApiOperation(value = "모임 활성화 여부 - Y(활성화), N(비활성화)")
    @PutMapping("/{groupSeq}/active")
    public ResponseEntity<String> changeActive(HttpServletRequest request,@PathVariable("groupSeq") int groupSeq, @RequestBody GroupSettingDto groupSettingDto){
    	String token = request.getHeader("X-AUTH-TOKEN");
   	 	if (token == null || !jwtTokenProvider.validateToken(token)) return null;
    	return new ResponseEntity<>(groupSettingService.changeActive(groupSeq,groupSettingDto),HttpStatus.OK);
    }
    
}
