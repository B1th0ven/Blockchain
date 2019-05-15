//package com.scor.dataProcessing.bulkInsert.service;
//
//import java.io.Serializable;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.scor.dataProcessing.bulkInsert.models.User;
//import com.scor.dataProcessing.bulkInsert.repository.UsersRepository;
//import com.scor.persistance.entities.RefUserEntity;
//
//@Service
//public class UsersService implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5476409587665481909L;
//
//	@Autowired
//	private UsersRepository repository;
//	
//	@Autowired UserHabilitationService habilitationService;
//
//	public void save(RefUserEntity userEntity) {
//		try {
//			User user = mapRefUserToUser(userEntity);
//			User userFinded = repository.findOne(user.getId());
//			if(userFinded != null) {
//				repository.update(user);
//			} else {
//				repository.save(user);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
////	private User mapRefUserToUser(RefUserEntity userEntity) throws Exception {
////		if (userEntity == null || userEntity.getRuId() == 0) {
////			throw new Exception();
////		}
////		Integer rcId = userEntity.getCountry() != null ? userEntity.getCountry().getRcId() : null;
////		Integer rsId = userEntity.getScope() != null ? userEntity.getScope().getRsId() : null;
////		User user = new User(userEntity.getRuId(), userEntity.getRuFirstName(), userEntity.getRuLastName(),
////				userEntity.getRuRmId(), userEntity.getRuLogin(), rcId, rsId, userEntity.getRuFunction(),
////				userEntity.getRuRole());
////		return user;
////	}
//
////	public void delete(RefUserEntity userEntity) {
////		User user = repository.findOne(userEntity.getRuId());
////		if(user == null) return;
////		try {
////			habilitationService.deleteByUserId(user.getId());
////			repository.delete(user.getId());
////		} catch (Exception e) {
////		}	
////	}
//
//}
