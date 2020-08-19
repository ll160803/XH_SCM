package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;
import cc.mrbird.febs.scm.service.IScmBPurcharseorderService;
import cc.mrbird.febs.scm.service.IScmCacheService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IScmCacheService")
public class ScmCacheServiceImpl implements IScmCacheService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private IScmBPurcharseorderService iScmBPurcharseorderService;

    @Autowired
    private ObjectMapper mapper;
    @Override
   public List<ScmBPurcharseorder> getPurcharseList() throws Exception{
        String purcharseorderListString = this.redisService.get(FebsConstant.GOBAL_ORDER_CACHE_PREFIX);
        if (StringUtils.isBlank(purcharseorderListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, ScmBPurcharseorder.class);
            return this.mapper.readValue(purcharseorderListString, type);
        }

   }

   @Override
    public  void  savePurcharseList() throws Exception{
       List<ScmBPurcharseorder> purcharseorderList = this.iScmBPurcharseorderService.findscmbpurcharseorderlastmonth();
       if (!purcharseorderList.isEmpty()) {
           this.redisService.del(FebsConstant.GOBAL_ORDER_CACHE_PREFIX);
           redisService.set(FebsConstant.GOBAL_ORDER_CACHE_PREFIX, mapper.writeValueAsString(purcharseorderList));
       }
   }
}
