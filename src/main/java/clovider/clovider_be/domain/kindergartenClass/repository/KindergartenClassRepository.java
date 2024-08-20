package clovider.clovider_be.domain.kindergartenClass.repository;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KindergartenClassRepository extends JpaRepository<KindergartenClass, Long> {

    List<KindergartenClass> findByKindergartenId(Long kindergartenId);

    void deleteAllByKindergartenId(Long kindergartenId);

    @Query("select k.className from KindergartenClass k where k.kindergarten.id = :kindergartenId and k.ageClass = :ageClass")
    String findClassNameById(@Param("kindergartenId") Long kindergartenId,
            @Param("ageClass") int ageClass);

    @Query("select kc from KindergartenClass kc join fetch kc.kindergarten order by kc.ageClass")
    List<KindergartenClass> findAllWithKindergarten();
}
