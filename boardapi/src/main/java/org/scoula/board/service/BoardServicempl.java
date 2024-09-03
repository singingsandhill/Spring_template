package org.scoula.board.service;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.scoula.board.common.util.UploadFiles;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.board.domain.BoardVO;
import org.scoula.board.dto.BoardDTO;
import org.scoula.board.mapper.BoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.scoula.board.common.util.UploadFiles.upload;

@Log4j // 로그 출력을 위한 어노테이션
@Service // 스프링 서비스 컴포넌트로 등록
@RequiredArgsConstructor // final 또는 @NonNull 필드를 포함한 생성자를 자동으로 생성
public class BoardServicempl implements BoardService {

    final private BoardMapper mapper; // BoardMapper 인터페이스를 의존성 주입
    private final static String BASE_DIR = "c:/upload/board"; // 파일 업로드 기본 경로


    @Override
    public List<BoardDTO> getList() {
        log.info("getList-------------------"); // 로그 출력
        return mapper.getList().stream() // 데이터베이스에서 게시물 목록을 가져옴
                .map(BoardDTO::of) // BoardVO를 BoardDTO로 변환
                .toList(); // List<BoardDTO> 형태로 반환
    }

    @Override
    public BoardDTO get(Long no) {
        log.info("get----------------------"+no); // 로그 출력
        BoardDTO board = BoardDTO.of(mapper.get(no)); // 게시물 번호로 데이터를 가져와 BoardDTO로 변환
        return Optional.ofNullable(board) // null인지 체크
                .orElseThrow(NoSuchElementException::new); // null이면 예외 발생
    }

    // 2개 이상의 insert 문이 실행될 수 있으므로 트랜잭션 처리 필요
    // RuntimeException 경우만 자동 rollback.
    @Transactional // 트랜잭션 처리를 위한 어노테이션
    @Override
    public BoardDTO create(BoardDTO board) {
        log.info("create----------------------"+board); // 로그 출력

        BoardVO boardVO = board.toVO(); // BoardDTO를 BoardVO로 변환
        mapper.create(boardVO); // 게시물 데이터를 데이터베이스에 저장
//        board.setNo(vo.getNo()); // 필요에 따라 게시물 번호를 DTO에 설정

        // 파일 업로드 처리
        List<MultipartFile> files = board.getFiles(); // 업로드된 파일 리스트 가져오기
        if(files != null && !files.isEmpty()) { // 첨부 파일이 있는 경우
            upload(boardVO.getNo(), files); // 파일 업로드 처리
        }
        return get(boardVO.getNo());
    }

    @Override
    public BoardDTO update(BoardDTO board) {
        log.info("update----------------------"+board); // 로그 출력
        mapper.update(board.toVO());

        return get(board.getNo()); // 게시물 업데이트 실행 후 성공 여부 반환
    }

    @Override
    public BoardDTO delete(Long no) {
        log.info("delete-----------------------"+no); // 로그 출력
        BoardDTO board = get(no);

        mapper.delete(no);
        return board; // 게시물 삭제 실행 후 성공 여부 반환
    }

    // 파일 업로드 처리 메서드
    private void upload(Long bno, List<MultipartFile> files) {
        for(MultipartFile part: files) { // 첨부된 각 파일에 대해 처리
            if(part.isEmpty()) continue; // 파일이 비어 있으면 무시
            try {
                String uploadPath = UploadFiles.upload(BASE_DIR, part); // 파일을 지정된 경로에 업로드
                BoardAttachmentVO attach = BoardAttachmentVO.of(part, bno, uploadPath); // 업로드된 파일 정보를 VO로 변환
                mapper.createAttachment(attach); // 파일 정보를 데이터베이스에 저장
            } catch (IOException e) {
                throw new RuntimeException(e); // 예외 발생 시 런타임 예외로 감싸서 던짐. 트랜잭션 롤백 처리
            }
        }
    }

    // 첨부파일 한 개 얻기
    @Override
    public BoardAttachmentVO getAttachment(Long no) {
        return mapper.getAttachment(no); // 첨부 파일 정보 가져오기
    }

    // 첨부파일 삭제
    @Override
    public boolean deleteAttachment(Long no) {
        return  mapper.deleteAttachment(no) == 1; // 첨부 파일 삭제 후 성공 여부 반환
    }
}
