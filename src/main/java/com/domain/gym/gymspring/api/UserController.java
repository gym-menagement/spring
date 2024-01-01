package com.domain.gym.gymspring.api;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.domain.gym.gymspring.api.response.*;
import com.domain.gym.gymspring.domain.User;
import com.domain.gym.gymspring.domain.UserSpecs.SearchKey;
import com.domain.gym.gymspring.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "user API", description = "user crud api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService service;

    @Operation(summary = "사용자 등록", description = "사용자를 등록됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<? extends BasicResponse> insert(@RequestBody User item) {
        Optional<User> opt = service.findByLoginid(item.getLoginid());

        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("이미 등록된 사용자입니다"));
        }

        User result = service.insert(item);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("등록중 오류가 발생했습니다"));
        }

        return ResponseEntity.ok().body(new CommonResponse<User>(result));
    }

    @Operation(summary = "사용자 수정", description = "사용자를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PutMapping("/{id}")
    public ResponseEntity<? extends BasicResponse> update(@PathVariable int id, @RequestBody User item) {
        Optional<User> opt = service.findById(id);

        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("데이터를 찾을수가 없습니다"));
        }

        User result = service.update(item);
        return ResponseEntity.ok().body(new CommonResponse<User>(result));
    }

    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        Optional<User> opt = service.findById(id);

        if (opt.isPresent()) {
            service.delete(opt.get());
        }

        return ResponseEntity.ok().body(new CommonResponse<String>("OK"));
    }

    @Operation(summary = "사용자 확인", description = "사용자를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{id}")
    public ResponseEntity<? extends BasicResponse> findById(@PathVariable int id) {
        Optional<User> opt = service.findById(id);

        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("데이터를 찾을수가 없습니다"));
        }

        User item = opt.get();
        item.setPasswd("");

        return ResponseEntity.ok().body(new CommonResponse<User>(item));
    }

    @Operation(summary = "사용자 확인", description = "사용자를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("")
    public ResponseEntity<? extends BasicResponse> findAll(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "orderby", required = false) String orderby,
            @RequestParam(value = "gym", defaultValue = "0", required = false) int gym,
            @RequestParam(value = "loginid", required = false) String loginid,
            @RequestParam(value = "name", required = false) String name) {
        if (page > 0) {
            page--;
        }

        Map<SearchKey, Object> searchKeys = new HashMap<>();
        if (gym > 0) {
            searchKeys.put(SearchKey.valueOf("GYM"), gym);
        }

        if (!StringUtils.isEmpty(loginid)) {
            searchKeys.put(SearchKey.valueOf("LOGINID"), loginid);
        }

        if (!StringUtils.isEmpty(name)) {
            searchKeys.put(SearchKey.valueOf("NAME"), name);
        }

        if (size == 0) {
            List<User> result = service.findAll();
            return ResponseEntity.ok().body(new CommonResponse<List<User>>(result));
        } else {
            Page<User> result = service.findAll(searchKeys, orderby, page, size);
            return ResponseEntity.ok().body(new CommonResponse<Page<User>>(result));
        }
    }

}
