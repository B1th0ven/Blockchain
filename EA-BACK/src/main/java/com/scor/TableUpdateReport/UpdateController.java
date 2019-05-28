package com.scor.TableUpdateReport;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController {

    @Autowired
    UpdateService updateService;


    @GetMapping("/updateControlReports/{id}")
    public void update(@PathVariable("id") int id){
        updateService.updateControls(id);
    }

    @GetMapping("/updateControlReport")
    public void update(){
        updateService.updateReports();
    }


}
