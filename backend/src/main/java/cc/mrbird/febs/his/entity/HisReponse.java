package cc.mrbird.febs.his.entity;

import lombok.Data;

import java.util.List;

@Data
public class HisReponse {
    private String code;
    private String msg;
    private List<ScmWSale> data;
}
