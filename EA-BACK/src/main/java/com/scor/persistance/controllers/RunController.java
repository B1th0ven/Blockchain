package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.services.BusinessService;
import com.scor.persistance.services.RunService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/run")
public class RunController {

    @Autowired
    private RunService service;

    @RequestMapping(value="", method = RequestMethod.GET)
    public List<RunEntity> getRuns()
    {
        return service.getAll();

    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public RunEntity getRun(@RequestParam("id") int id)
    {
        return service.getByRunId(id);
    }

    @RequestMapping(value="/dataset", method = RequestMethod.GET)
    public List<RunEntity> getRunsByDataset(@RequestParam("id") int id)
    {
        return service.getByDatasetId(id);
    }

    @RequestMapping(value="/study", method = RequestMethod.GET)
    public List<RunEntity> getRunsByStudy(@RequestParam("id") int id)
    {
        return service.getByStudyId(id);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public RunEntity saveRun(@RequestBody  RunEntity run)
    {
        return service.save(run);
    }

    @RequestMapping(value="/exp", method = RequestMethod.POST)
    public RunEntity saveRunExp(@RequestBody  RunEntity run)
    {
        return service.saveExp(run);
    }
    
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public RunEntity saveRunAfterValidation(@RequestBody  RunEntity run)
    {
        return service.saveParameter(run);
    }

    @RequestMapping(value="", method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") int id) throws Exception {
        service.delete(id);
    }

    @RequestMapping(value="/datasets/candelete/{id}", method = RequestMethod.GET)
    public HashMap<String,String> canDeleteDs(@PathVariable("id") int id)
    {
        List<Integer> ll_id = new ArrayList<>();
        List<RunEntity> ll = service.getByDatasetId(id);
        HashMap<String,String> res = new HashMap<String,String>();
        if( ll != null && ll.size() > 0)
        {
            ll.stream().forEach(s -> ll_id.add(s.getRunId()));
            res.put("canDelete",null);
            res.put("runsid",ll_id.toString().replaceAll("\\[","").replaceAll("]",""));
        }
        else
        {
            res.put("canDelete","true");
            res.put("runsid",null);
        }
        return res;
    }
}
