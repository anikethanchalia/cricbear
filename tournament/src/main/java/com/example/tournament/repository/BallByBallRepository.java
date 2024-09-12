package com.example.tournament.repository;

import com.example.tournament.model.BallByBall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallByBallRepository extends JpaRepository<BallByBall, Integer> {
    @Query("SELECT count(distinct(bb.overNumber)) from BallByBall bb where bb.bowler = :bowlerId and bb.iid = :iid")
    public int countByBowlerId(@Param("bowlerId") String bowlerId, @Param("iid") int iid);

    List<BallByBall> findByIid(int iid);
}
