package com.sikugeon.damda.web.object.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UploadRequest {
    @NotNull
    String accessKeyId;
    String secretAccessKey;
    List<MultipartFile> images;

    public Map<String, String> getMap(){
        Map<String, String> map=new HashMap<>();
        map.put("accessKeyId", accessKeyId);
        map.put("secretAccessKey", secretAccessKey);

        return map;
    }
}
