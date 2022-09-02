package com.sikugeon.damda.core.object.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UploadInfo {
    private String name;
    private URI uri;
    private Date uploaded_at;
}
