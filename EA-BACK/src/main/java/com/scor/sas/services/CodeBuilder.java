package com.scor.sas.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class CodeBuilder implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 451197430642040416L;

    private String code;

    @Value("${sas-run-repo}")
    private String run_folder;
    
    @Value("${sas-lib-env}")
    private String liv_env;
    
    @Value("${sas-status-url}")
    private String host_name;

    public CodeBuilder() {
    }

    private void buildCode(String id, String drop_dim, String type, int rclcid) {


        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get(CodeBuilder.class.getClassLoader().getResource("data/run.sas").toURI()), StandardCharsets.ISO_8859_1);
            StringBuilder sb = new StringBuilder();

            lines.forEach(line -> {
                if (!line.equals("")) {
                    String line_changed_id = line.replaceAll("&id", id);
                    String line_change_id_input = line_changed_id.replaceAll("&input", run_folder + id + "\\\\input");
                    String line_change_id_output = line_change_id_input.replaceAll("&output", run_folder + id + "\\\\output");
                    String line_change_dim = line_change_id_output.replaceAll("&dropdim", drop_dim);
                    String line_change_type = line_change_dim.replaceAll("&type", type);
                    String line_change_rclcid = line_change_type.replaceAll("&rclcid", Integer.toString(rclcid));
                    String line_change_path = line_change_rclcid.replaceAll("&path", run_folder);
                    String line_change_lib_env = line_change_path.replaceAll("&libEnv", liv_env);
                    String line_change_url_status = line_change_lib_env.replaceAll("&hostName", host_name);
                    sb.append(line_change_url_status);
                    sb.append("\n");

                }
            });
            code = sb.toString();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public String getCode(String id, String dim, String type, int rclcid) {
        buildCode(id, dim, type, rclcid);
        //System.out.println(code);
        return code;
    }
}
