package org.scoula.board.common.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class UploadFiles {

    // 파일 업로드를 처리하는 메서드
    public static String upload(String baseDir, MultipartFile part) throws IOException {
        // 기본 디렉토리가 존재하는지 확인하고, 없으면 생성합니다.
        File base = new File(baseDir);
        if(!base.exists()) {
            base.mkdirs(); // 중간에 존재하지 않는 디렉토리까지 모두 생성합니다.
        }

        String fileName = part.getOriginalFilename(); // 업로드된 파일의 원래 이름을 가져옵니다.
        // 파일을 저장할 위치와 파일명을 지정합니다. 파일명은 고유한 이름으로 변경됩니다.
        File dest = new File(baseDir, UploadFileName.getUniqueName(fileName));
        // 업로드된 파일을 지정한 경로로 이동시킵니다.
        part.transferTo(dest);

        return dest.getPath(); // 저장된 파일의 경로를 리턴합니다.
    }

    // 파일 크기를 사람이 읽기 쉬운 형식으로 변환하는 메서드
    public static String getFormatSize(Long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "Bytes", "KB", "MB", "GB", "TB" }; // 파일 크기를 표현하는 단위 배열
        // 파일 크기의 로그를 계산하여 적절한 단위를 선택합니다.
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        // 파일 크기를 해당 단위로 변환하여 포맷된 문자열로 리턴합니다.
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    // 파일 다운로드를 처리하는 메서드
    public static void download(HttpServletResponse response, File file, String orgName) throws Exception {
        response.setContentType("application/download"); // 응답의 콘텐츠 타입을 다운로드로 설정합니다.
        response.setContentLength((int)file.length()); // 응답의 콘텐츠 길이를 파일 크기로 설정합니다.
        // 다운로드될 파일의 이름을 UTF-8로 인코딩하여 헤더에 설정합니다.
        String filename = URLEncoder.encode(orgName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=\"" + filename + "\"");

        // 파일을 응답 스트림으로 전송합니다.
        try(OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os)) {
            Files.copy(Paths.get(file.getPath()), bos); // 파일을 복사하여 출력 스트림으로 전송합니다.
        }
    }
}