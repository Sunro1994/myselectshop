package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder,Long> {
    //쿼리를 직접 짜보기
    // select * from folder where user_id = ? and name in (?,?,...); in ()안에는 여러개가 올 수 있다.
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);

    List<Folder> findAllByUser(User user);

    Optional<Folder> findByIdAndUser(Long folderId, User user);
}
