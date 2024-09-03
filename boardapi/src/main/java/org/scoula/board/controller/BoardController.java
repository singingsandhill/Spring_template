package org.scoula.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoula.board.service.BoardService;
import org.scoula.board.dto.BoardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    // BoardService 인스턴스를 주입받아 사용
    private final BoardService service;

    /**
     * 전체 게시글 목록을 조회합니다.
     * GET 요청을 처리하며, /api/board 엔드포인트로 접근합니다.
     *
     * @return 전체 게시글 목록을 담고 있는 List<BoardDTO>를 ResponseEntity로 반환
     */
//    @GetMapping("")
//    public List<BoardDTO> getList() {
//        return service.getList();
//    }
    @GetMapping("")
    public ResponseEntity<List<BoardDTO>> getList() {
        // 서비스 메서드를 호출하여 게시글 목록을 조회한 후, HTTP 200 상태 코드와 함께 반환
        return ResponseEntity.ok(service.getList());
    }

    /**
     * 특정 게시글을 조회합니다.
     * GET 요청을 처리하며, /api/board/{no} 엔드포인트로 접근합니다.
     *
     * @param no 조회할 게시글의 고유 번호 (ID)
     * @return 해당 번호의 게시글 정보를 담은 BoardDTO를 ResponseEntity로 반환
     */

    @GetMapping("/{no}")
    public ResponseEntity<BoardDTO> get(@PathVariable Long no) {
        // 서비스 메서드를 호출하여 특정 ID에 해당하는 게시글을 조회한 후, HTTP 200 상태 코드와 함께 반환
        return ResponseEntity.ok(service.get(no));
    }

    /**
     * 새로운 게시글을 생성합니다.
     * POST 요청을 처리하며, /api/board 엔드포인트로 접근합니다.
     *
     * @param board 생성할 게시글 정보를 담고 있는 BoardDTO
     * @return 생성된 게시글 정보를 담은 BoardDTO를 ResponseEntity로 반환
     */
    @PostMapping("")
    public ResponseEntity<BoardDTO> create(@RequestBody BoardDTO board) {
        // 서비스 메서드를 호출하여 게시글을 생성한 후, HTTP 200 상태 코드와 함께 생성된 게시글을 반환
        return ResponseEntity.ok(service.create(board));
    }

    /**
     * 특정 게시글을 수정합니다.
     * PUT 요청을 처리하며, /api/board/{no} 엔드포인트로 접근합니다.
     *
     * @param no    수정할 게시글의 고유 번호 (ID)
     * @param board 수정할 게시글 정보를 담고 있는 BoardDTO
     * @return 수정된 게시글 정보를 담은 BoardDTO를 ResponseEntity로 반환
     */
    @PutMapping("/{no}")
    public ResponseEntity<BoardDTO> update(@PathVariable ("no") Long no, @RequestBody BoardDTO board) {
        // 서비스 메서드를 호출하여 게시글을 수정한 후, HTTP 200 상태 코드와 함께 수정된 게시글을 반환
        return ResponseEntity.ok(service.update(board));
    }

    /**
     * 특정 게시글을 삭제합니다.
     * DELETE 요청을 처리하며, /api/board/{no} 엔드포인트로 접근합니다.
     *
     * @param no 삭제할 게시글의 고유 번호 (ID)
     * @return 삭제된 게시글 정보를 담은 BoardDTO를 ResponseEntity로 반환
     */
    @DeleteMapping("/{no}")
    public ResponseEntity<BoardDTO> delete(@PathVariable ("no") Long no) {
        // 서비스 메서드를 호출하여 게시글을 삭제한 후, HTTP 200 상태 코드와 함께 삭제된 게시글을 반환
        return ResponseEntity.ok(service.delete(no));
    }
}
