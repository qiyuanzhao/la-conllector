//package com.lavector.collector.entity.user;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author tao
// */
//public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {
//
//    @Query("SELECT r FROM ResetPasswordToken r WHERE r.token=:token")
//    ResetPasswordToken findByToken(@Param("token") String token);
//
//    @Query("SELECT r FROM ResetPasswordToken r WHERE r.user =:user ORDER BY r.timeCreated DESC")
//    List<ResetPasswordToken> findUserPasswordTokens(@Param("user") User user);
//
//    @Query("SELECT r FROM ResetPasswordToken r WHERE r.id = :id AND r.timeCreated > :validTimeThreshHold")
//    ResetPasswordToken findByIdAndValid(@Param("id") Long passwordTokenId, @Param("validTimeThreshHold") Date earliestTime);
//}
