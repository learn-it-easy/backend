package com.system.learn.repository;

import com.system.learn.entity.Folder;
import com.system.learn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUser(User user);
    long countByUser(User user);

    @Query(value = "SELECT * FROM folders WHERE user_id = :userId ORDER BY id ASC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY",
            nativeQuery = true)
    List<Folder> findByUserWithOffsetAndLimit(@Param("userId") Long userId,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);
    Optional<Folder> findByIdAndUser(Long id, User user);
    @Query("SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.cards WHERE f.user.id = :userId ORDER BY f.id ASC")
    List<Folder> findByUserWithCards(@Param("userId") Long userId);

}