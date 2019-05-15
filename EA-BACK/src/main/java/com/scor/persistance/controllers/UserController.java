package com.scor.persistance.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scor.dataProcessing.common.StudyRoles;
import com.scor.dataProcessing.common.TableRoles;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RoleExceptionEntity;
import com.scor.persistance.services.RoleManagerService;
import com.scor.persistance.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {
	private final static Logger LOG = Logger.getLogger(UserController.class);
    @Autowired
    private UserService service;

    @Autowired
    private RoleManagerService rms;

    @RequestMapping(value="", method = RequestMethod.GET)
    public List<RefUserEntity> getUsers()
    {
        return service.getAll();

    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public RefUserEntity loginUser(@RequestBody String login)
    {
        return service.login(login);
    }

    @RequestMapping(value="/roles/{st_id}/{user_id}", method = RequestMethod.GET)
    public StudyRoles getRoles(@PathVariable("st_id") int st_id,@PathVariable("user_id") int user_id) throws Exception {
        return rms.StudyPrevligesCalCulator(user_id,st_id);
    }

    @RequestMapping(value="/roles/exceptions", method = RequestMethod.POST)
    public RoleExceptionEntity getRoles(HttpEntity<String> req) throws Exception {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
        int st_id = Integer.parseInt((String) req_map.get("st_id")) ;
        int usr_id = Integer.parseInt((String) req_map.get("usr_id"));
        String role = (String) req_map.get("role");
        return rms.addException(st_id,usr_id,role);
    }

    @RequestMapping(value="/roles/exceptions/{st_id}/{user_id}", method = RequestMethod.GET)
    public RoleExceptionEntity getExceptions(@PathVariable("st_id") int st_id,@PathVariable("user_id") int user_id)  {
        return rms.getByStIdAndUsrId(st_id,user_id);
    }

    @RequestMapping(value="/roles/exceptions/{st_id}", method = RequestMethod.GET)
    public List<RoleExceptionEntity> getExceptionsByStId(@PathVariable("st_id") int st_id)  {
        return rms.getExceptionsByStId(st_id);
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public RefUserEntity saveUser(@RequestBody  RefUserEntity user)  {
        return service.save(user);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public HashMap<String,String> deleteUser(@PathVariable("id") int id)  {
        return service.delete(id);
    }

    @RequestMapping(value="/roles/{id}", method = RequestMethod.GET)
    public List<RoleExceptionEntity> getRoles(@PathVariable("id") int id) throws Exception {
        return rms.getRoles(id);
    }

    @RequestMapping(value="/roles/table/{tableId}/{userId}", method = RequestMethod.GET)
    public TableRoles getTableRoles(@PathVariable("tableId") int tableId,@PathVariable("userId") int userId) throws Exception {
        return rms.tableRolesCalculator(tableId, userId);
    }

    @RequestMapping(value = "/kerb", method = RequestMethod.GET)
    @ResponseBody
    public RefUserEntity login() {
       String UserID;
       LOG.info("KERB endpoint start");
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            LOG.info("KERB endpoint  auth not null");
            UserID = SecurityContextHolder.getContext().getAuthentication().getName().split("@")[0];
            LOG.info("KERB endpoint  getName");
            LOG.info(SecurityContextHolder.getContext().getAuthentication().getName());
            if(service.login(UserID) != null) {
                LOG.info("KERB endpoint user found");
            	return  service.login(UserID);
            }
            else {
                LOG.info("KERB endpoint  user not found");
            	return null;
            }
        } else
            return null;
    }

//    @RequestMapping(value="/synchronize", method = RequestMethod.GET)
//    public void saveUser()  {
//        service.synchronize();
//    }
}
