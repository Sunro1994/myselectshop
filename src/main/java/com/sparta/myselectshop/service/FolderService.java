package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
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
            }else{
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

}
