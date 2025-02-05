package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.ProductFolder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    @Transactional
    public void addFolders(List<String> folderNames, User user) {
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames); //여러개의 조건이 한번에 포함되려면 마지막에 In을 넣는다.

        List<Folder> folderList = new ArrayList<>();

        //이미 생성한 폴더는 제외하고 생성해야 한다.
        for (String folderName : folderNames) {
            if (!isExistFolderName(existFolderList, folderName)) {
                folderList.add(new Folder(folderName, user));
            } else {
                throw new IllegalArgumentException("중복된 폴더명을 제거해주세! 폴더명 : " + folderName);
            }
        }
        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }

    private boolean isExistFolderName(List<Folder> existFolderList, String folderName) {
        for (Folder folder : existFolderList) {
            if (folder.getName().equals(folderName)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsInFolder(Long folderId, User user) {
        // 사용자 소유의 폴더인지 확인
        Folder folder = folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 폴더를 찾을 수 없거나 접근 권한이 없습니다."));

        // ProductFolder 엔티티를 통해 폴더에 속한 상품 조회
        List<ProductResponseDto> responseDtoList = new ArrayList<>();
        for (ProductFolder productFolder : folder.getProductFolders()) {
            responseDtoList.add(new ProductResponseDto(productFolder.getProduct()));
        }

        return responseDtoList;
    }
}

