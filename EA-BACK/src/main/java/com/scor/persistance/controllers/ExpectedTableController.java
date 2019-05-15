package com.scor.persistance.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.filters.PageBase;
import com.scor.persistance.services.ExpectedTableService;

@RestController
public class ExpectedTableController {

    @Autowired
    private ExpectedTableService service;

    @RequestMapping("/table")
    public List<RefExpectedTableEntity> getAll()
    {
        return service.getAll();
    }

    @RequestMapping("/table/{id}")
    public RefExpectedTableEntity getOne(@PathVariable("id") int id)
    {
        return service.getOne(id);
    }

    @RequestMapping( value="/table", method = RequestMethod.POST)
    public RefExpectedTableEntity save( @RequestBody RefExpectedTableEntity t)
    {
        return service.save(t);
    }


    @RequestMapping( value="/table/latest", method = RequestMethod.POST)
    public int check( @RequestBody RefExpectedTableEntity t)
    {
        return service.getLatestVersion(t);
    }

    @RequestMapping( value="/table/")
    public PageBase<RefExpectedTableEntity> query(
            @RequestParam("limit") int limit,
            @RequestParam("page") int page,
            @RequestParam("name") Optional<String> name,
            @RequestParam("country") Optional<String> country,
            @RequestParam("version") Optional<String> version,
            @RequestParam("decrement") Optional<String> decrement,
            @RequestParam("type") Optional<String> type,
            @RequestParam("origin") Optional<String> origin,
            @RequestParam("application") Optional<String> application,
            @RequestParam("publication") Optional<String> publication,
            @RequestParam("source") Optional<String> source,
            @RequestParam("code") Optional<String> id,
            @RequestParam("creation_date") Optional<String> creation_date,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("desc") Optional<Boolean> desc,
            @RequestParam("user") Optional<Integer> userId
    )
    {
        return service.searchByQuery(page,limit,country,name,version,decrement,type,origin,application,publication,source,id,creation_date,sort,desc,userId);
    }
    
    @RequestMapping( value="/table/run/exp")
    public PageBase<RefExpectedTableEntity> query(	
            @RequestParam("decrement") Optional<String> decrement,
            @RequestParam("type") Optional<String> type,
            @RequestParam("user") Optional<Integer> userId
    )
    {
        return service.getTablesByDecrementAndType(decrement,type,userId);
    }
    
    @DeleteMapping(value="table/{id}")
    public boolean deleteTable(@PathVariable("id") int id) throws Exception{
    	return service.deleteTable(id);
    }
    
    @GetMapping(value="table/run/{id}/{type}")
    public Set<RunEntity> associationToRun(@PathVariable("id") int id, @PathVariable("type") String type) throws Exception{
    	Set<RunEntity> runs  = service.runsAssociatedToTable(id,type);
		return runs;
    }

    @RequestMapping( value="/table/check/ageattained", method = RequestMethod.POST)
    public Map<String, Boolean> isageAttainedProvided( @RequestBody List<Integer> ids)
    {
        return service.checkAgeAttained(ids);
    }


}
