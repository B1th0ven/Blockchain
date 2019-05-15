package com.scor.persistance.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.scor.dataProcessing.bulkInsert.service.UsersService;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RoleExceptionEntity;
import com.scor.persistance.repositories.RefScopeRepository;
import com.scor.persistance.repositories.RoleExceptionRepository;
import com.scor.persistance.repositories.UserRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class UserService implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5191634484853905906L;

	@Autowired
    private UserRepository repository;

    @Autowired
    private RefScopeRepository rs;

    @Autowired
    RoleExceptionRepository re;
    
//    @Autowired
//    private UsersService usersService;


    
    public List<RefUserEntity> getAll()
    {
        List<RefUserEntity> list = new ArrayList<>();

        for ( RefUserEntity run : repository.findAll())
        {
            list.add(run);
        }
        return list;
    }

    public RefUserEntity login(String login)
    {
        System.out.println("Login->>> "+login+" ----> " +repository.findFirstByRuLogin(login));

        return repository.findFirstByRuLogin(login);
    }

    public RefUserEntity save(RefUserEntity user){
        RefUserEntity userSaved = repository.save(user);
//        usersService.save(userSaved);
        
		return userSaved;
    }

    public HashMap<String,String> delete(int id){
        RefUserEntity user = repository.findOne(id);
        if (user == null) {
        	return null;
        }
		List<RoleExceptionEntity> ref = re.findByUreRuId(user);
        for (RoleExceptionEntity roleExceptionEntity : ref) {
        	re.delete(roleExceptionEntity.getUreId());
		}
//         ref.stream().forEach(s -> re.delete(s.getUreId()));
        repository.delete(id);
//        usersService.delete(user);
        HashMap<String,String> res = new HashMap<>();
        res.put("message","User with id : "+id+" has been deleted");
         return res;
    }
    
//    public void synchronize() {
//    	List<RefUserEntity> refUserEntities = getAll();
//    	refUserEntities.forEach(userEntity -> {
//    		usersService.save(userEntity);
//    	});
//    }

}
